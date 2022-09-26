package space.moontalk.mc.commands.route;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.CommandCall;

public interface Router {   
    // Add:

    default void addRoute(@NotNull RouteNode tree, @NotNull RouteHandler handler) {
        addRoute(new Route(tree, handler));
    }

    void addRoute(@NotNull Route route);

    // Route:

    void route(@NotNull CommandCall call) throws Exception;

    // Completions:

    @NotNull List<String> evalCompletions(@NotNull CommandCall call);
}
