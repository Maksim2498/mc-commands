package space.moontalk.mc.commands.route;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.CommandCall;
import space.moontalk.mc.commands.InvalidClassException;
import space.moontalk.mc.commands.MissingPermissionException;

public class DefaultRouter extends AbstractParsingRouter {
    public DefaultRouter() {
        this(new DefaultRouteParser());
    }

    public DefaultRouter(@NotNull RouteParser routeParser) {
        super(routeParser);
    }

    @Override
    public void route(@NotNull CommandCall call) throws Exception {
        val possiblePaths = evalPossibleNodePaths(); 

        filterPossiblePaths(possiblePaths, call);

        if (possiblePaths.size() != 1)
            throw new MissingRouteException();

        val path = possiblePaths.stream()
                                .findFirst()
                                .get();

        routeCall(path, call);
    }

    private @NotNull List<PathAndHandler> evalPossibleNodePaths() {
        val pahs = new LinkedList<PathAndHandler>();

        for (val route : routes) {
            val tree    = route.getTree();
            val path    = new LinkedList<RouteNode.Nullary>();
            val handler = route.getHandler();
            val pah     = new PathAndHandler(path, handler);
            val argPahs = new LinkedList<PathAndHandler>();

            argPahs.add(pah);

            val resPahs = addPossibbleNodePath(tree, argPahs);

            pahs.addAll(resPahs);
        }
        
        return pahs; 
    }

    private @NotNull List<PathAndHandler> addPossibbleNodePath(
        @NotNull RouteNode            node,
        @NotNull List<PathAndHandler> pahs 
    ) {
        val type = node.getType();

        return switch (type) {
            case NONE -> pahs;

            case ANY, EXACT, PLACEHOLDER -> {
                val nullary = (RouteNode.Nullary) node;

                for (val pah : pahs) {
                    val path = pah.getPath();
                    path.add(nullary);
                }

                yield pahs;
            }

            case OR -> {
                val or = (RouteNode.Or) node;

                val pahsClone = clonePaths(pahs);
                val leftPahs  = addPossibbleNodePath(or.getLeftChild(), pahs);
                val rightPahs = addPossibbleNodePath(or.getRightChild(), pahsClone);

                leftPahs.addAll(rightPahs);

                yield leftPahs;
            }

            case CONCAT -> {
                val concat = (RouteNode.Concat) node;

                pahs = addPossibbleNodePath(concat.getLeftChild(),  pahs);
                pahs = addPossibbleNodePath(concat.getRightChild(), pahs);

                yield pahs;
            }

            default -> throw new IllegalArgumentException();
        };
    }

    private @NotNull List<PathAndHandler> clonePaths(@NotNull List<PathAndHandler> paths) {
        val clone = new LinkedList<PathAndHandler>();

        for (val path : paths)
            clone.add(path.clone());

        return clone;
    }

    private void filterPossiblePaths(@NotNull List<PathAndHandler> pahs, @NotNull CommandCall call) {
        val args = call.getArgs();

        pahs.removeIf(pah -> {
            val path = pah.getPath();

            if (path.size() != args.length)
                return true;

            int i = 0;

            for (val node : path)
                if (node instanceof RouteNode.Exact exactNode) {
                    val string = exactNode.getString();
                    val arg    = args[i++];

                    if (!string.equals(arg))
                        return true;
                }
            
            return false;
        });
    }

    private void routeCall(@NotNull PathAndHandler pah, @NotNull CommandCall call) throws Exception {
        val handler = pah.getHandler();
        val sender  = call.getCommandSender();

        checkSender(sender, handler);

        val path        = pah.getPath();
        val placeholded = placehold(path, call);
        val routeCall   = new RouteCall(call, placeholded);

        handler.onRoute(routeCall);
    }

    private void checkSender(@NotNull CommandSender sender, @NotNull RouteHandler handler) throws Exception {
        if (!handler.hasPermission(sender)) {
            val permission = handler.getPermission();
            throw new MissingPermissionException(permission);
        }

        if (!handler.isOfClass(sender)) {
            val classes = handler.getClasses();
            throw new InvalidClassException(classes);
        }
    }

    private @NotNull Object[] placehold(@NotNull List<RouteNode.Nullary> path, @NotNull CommandCall call) throws Exception {
        val placeholdedList = new LinkedList<Object>();
        val args            = call.getArgs();
        int i               = 0;

        for (val node : path) {
            if (node instanceof RouteNode.Placeholder placeholderNode) {
                val placeholder = placeholderNode.getPlaceholder();
                val arg         = args[i];
                val placeholded = placeholder.variantToObject(call, arg);
                placeholdedList.add(placeholded);
            }

            ++i;
        }

        val placeholdedArray = placeholdedList.toArray();
        return placeholdedArray;
    }

    @Override
    public @NotNull List<String> evalCompletions(@NotNull CommandCall call) {
        val args = call.getArgs();

        if (args.length == 0)
            return Collections.emptyList();

        val completionNodes = evalCompletionNodes(call);
        val completions     = completionNodesToCompletions(completionNodes, call);
        val lastArg         = args[args.length - 1];

        removeNotStartingWith(completions, lastArg);

        return completions;
    }

    private @NotNull List<RouteNode> evalCompletionNodes(@NotNull CommandCall call) {
        val completionNodes = new LinkedList<RouteNode>();
        val sender          = call.getCommandSender();

        for (val route : routes) {
            val handler = route.getHandler();

            if (!handler.canRoute(sender))
                continue;

            val tree = route.getTree();

            addCompletionNodes(completionNodes, tree, call, 0);
        }

        return completionNodes;
    }

    private int addCompletionNodes(
        @NotNull List<RouteNode> completionNodes,
        @NotNull RouteNode       node,
        @NotNull CommandCall     call,
                 int             argIndex
    ) {
        val args = call.getArgs();

        if (argIndex >= args.length)
            return argIndex;

        return switch (node.getType()) {
            case NONE -> argIndex == args.length - 1 ? 0 : -1;
            case ANY  -> 1;

            case EXACT -> {
                if (argIndex == args.length - 1) {
                    completionNodes.add(node);
                    yield 1;
                }

                val exactNode = (RouteNode.Exact) node;
                val string    = exactNode.getString();
                val arg       = args[argIndex];
                
                yield arg.equals(string) ? 1 : -1;
            }

            case PLACEHOLDER -> {
                if (argIndex == args.length - 1) {
                    completionNodes.add(node);
                    yield 1;
                }

                val placeholderNode = (RouteNode.Placeholder) node;
                val placeholder     = placeholderNode.getPlaceholder();
                val variants        = placeholder.evalVariants(call);
                val arg             = args[argIndex];

                yield variants.contains(arg) ? 1 : -1;
            }

            case OR -> {
                val orNode = (RouteNode.Or) node;

                val left  = addCompletionNodes(completionNodes, orNode.getLeftChild(),  call, argIndex);
                val right = addCompletionNodes(completionNodes, orNode.getRightChild(), call, argIndex);
                val min   = Math.min(left, right);

                yield min != -1 ? min : Math.max(left, right);
            }

            case CONCAT -> {
                val concatNode = (RouteNode.Concat) node;

                val left = addCompletionNodes(completionNodes, concatNode.getLeftChild(), call, argIndex);

                if (left == -1)
                    yield -1;

                val right = addCompletionNodes(completionNodes, concatNode.getRightChild(), call, argIndex + left);

                yield right;
            }

            default -> throw new IllegalArgumentException();
        };
    } 

    private @NotNull List<String> completionNodesToCompletions(
        @NotNull List<RouteNode> nodes,
        @NotNull CommandCall     call
    ) {
        val completions = new LinkedList<String>();

        for (val node : nodes)
            switch (node.getType()) {
                case EXACT -> {
                    val exactNode = (RouteNode.Exact) node;
                    val string    = exactNode.getString();

                    completions.add(string);
                }

                case PLACEHOLDER -> {
                    val placeholderNode = (RouteNode.Placeholder) node;
                    val placeholder     = placeholderNode.getPlaceholder();
                    val variants        = placeholder.evalVariants(call);

                    completions.addAll(variants);
                }

                default -> throw new IllegalArgumentException();
            }

        return completions;
    }

    private static void removeNotStartingWith(@NotNull List<String> completions, @NotNull String prefix) {
        completions.removeIf(c -> !c.startsWith(prefix));
    }

    @Getter
    @AllArgsConstructor
    private static class PathAndHandler implements Cloneable {
        private final @NotNull List<RouteNode.Nullary> path;
        private final @NotNull RouteHandler            handler;

        @Override
        public @NotNull PathAndHandler clone() {
            val newPath = new LinkedList<>(path);
            return new PathAndHandler(newPath, handler);
        }
    }
}
