package space.moontalk.mc.commands.route;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

public abstract class AbstractParsingRouter implements ParsingRouter {
    @Getter
    protected final @NotNull RouteParser routeParser;
    protected final @NotNull List<Route> routes = new LinkedList<>();

    protected AbstractParsingRouter(@NotNull RouteParser routeParser) {
        this.routeParser = routeParser;
    }

    @Override
    public void addRoute(@NotNull Route route) {
        routes.add(route);
    }
}
