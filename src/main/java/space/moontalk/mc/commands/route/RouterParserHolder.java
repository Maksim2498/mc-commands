package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

public interface RouterParserHolder {
    @NotNull RouteParser getRouteParser();
}
