package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.placeholder.PlaceholderManager;

public interface RouteParser {
    @NotNull RouteNode parseRoute(@NotNull String route) throws InvalidRouteException;    

    @NotNull PlaceholderManager getPlaceholderManager();
}
