package space.moontalk.mc.commands.route;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.CommandCall;

public interface Router {   
    default void addRoute(@NotNull RouteNode tree, @NotNull RouteHandler handler) {
        addRoute(new Route(tree, handler));
    }

    void addRoute(@NotNull Route route);

    void route(@NotNull CommandCall call) throws RouteException;

    @NotNull List<String> evalCompletions(@NotNull CommandCall call);
}
