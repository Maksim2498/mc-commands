package space.moontalk.mc.commands.completion;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public class DefaultPatternAutomata implements PatternAutomata {
    private final @NotNull PatternNode  ast;
    private       @NotNull List<String> playersNames;

    public DefaultPatternAutomata(@NotNull PatternNode ast) {
        this.ast = ast;
    }

    @Override
    public @NotNull List<String> evalCompletions(@NotNull String[] args) {
        if (args.length == 0)
            return Collections.emptyList();

        updatePlayersNames();

        val completionNodes = evalCompletionNodes(args);
        val completions     = completionNodesToCompletions(completionNodes);
        val lastArg         = args[args.length - 1];
        
        removeNotStartingWith(completions, lastArg);

        return completions;
    }

    private void updatePlayersNames() {
        playersNames = Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    private @NotNull List<PatternNode> evalCompletionNodes(@NotNull String[] args) {
        val completionNodes = new LinkedList<PatternNode>();
        addCompletionNodes(completionNodes, ast, args, 0);
        return completionNodes;
    }

    private int addCompletionNodes(
        @NotNull List<PatternNode> completionNodes,
        @NotNull PatternNode       node,
        @NotNull String[]          args,
        int                        argIndex
    ) {
        if (argIndex >= args.length)
            return argIndex;

        return switch (node.getType()) {
            case NONE -> argIndex;
            case ANY  -> argIndex + 1;

            case EXACT -> {
                if (argIndex == args.length - 1) {
                    completionNodes.add(node);
                    yield argIndex + 1;
                }

                val exact  = (PatternNode.Exact) node;
                val string = exact.getString();
                val arg    = args[argIndex];
                
                yield arg.equals(string) ? argIndex + 1 : -1;
            }

            case PLAYER -> {
                if (argIndex == args.length - 1) {
                    completionNodes.add(node);
                    yield argIndex + 1;
                }

                val arg = args[argIndex];

                yield playersNames.contains(arg) ? argIndex + 1 : -1;
            }

            case OR -> {
                val or = (PatternNode.Or) node;

                val leftArgIndex  = addCompletionNodes(completionNodes, or.getLeftChild(),  args, argIndex);
                val rightArgIndex = addCompletionNodes(completionNodes, or.getRightChild(), args, argIndex);

                yield Math.max(leftArgIndex, rightArgIndex);
            }

            case CONCAT -> {
                val concat = (PatternNode.Concat) node;

                argIndex = addCompletionNodes(completionNodes, concat.getLeftChild(),  args, argIndex);

                if (argIndex != -1)
                    argIndex = addCompletionNodes(completionNodes, concat.getRightChild(), args, argIndex);

                yield argIndex;
            }

            default -> throw new IllegalArgumentException();
        };
    }

    private @NotNull List<String> completionNodesToCompletions(@NotNull List<PatternNode> nodes) {
        val completions = new LinkedList<String>();

        for (val node : nodes)
            switch (node.getType()) {
                case EXACT: 
                    val exact  = (PatternNode.Exact) node;
                    val string = exact.getString();

                    completions.add(string);

                    break;

                case PLAYER: 
                    completions.addAll(playersNames);
                    break;

                default: 
                    throw new IllegalArgumentException();
            };

        return completions;
    }

    private static void removeNotStartingWith(@NotNull List<String> strings, @NotNull String prefix) {
        val lowerPrefix = prefix.toLowerCase(); 
        strings.removeIf(s -> !s.toLowerCase().startsWith(lowerPrefix));
    }
}
