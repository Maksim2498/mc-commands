package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public interface ParsingRouter extends Router {
    default void addRoute(@NotNull String routePath, @NotNull RouteHandler handler) throws InvalidRouteException {
        val parser = getRouteParser();     
        val tree   = parser.parseRoute(routePath);
        
        addRoute(tree, handler);
    }

    @NotNull RouteParser getRouteParser();
}
