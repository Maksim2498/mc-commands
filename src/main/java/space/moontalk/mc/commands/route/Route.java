package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Route {
    private final @NotNull RouteNode    tree;
    private final @NotNull RouteHandler handler;
}
