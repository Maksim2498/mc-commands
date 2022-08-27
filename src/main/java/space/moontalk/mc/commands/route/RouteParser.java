package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

public interface RouteParser {
    @NotNull RouteNode parseRoute(@NotNull String route) throws InvalidRouteException;    
}
