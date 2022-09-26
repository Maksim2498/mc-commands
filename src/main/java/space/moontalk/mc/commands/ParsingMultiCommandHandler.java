package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import lombok.val;

import space.moontalk.mc.commands.route.ParsingRouter;
import space.moontalk.mc.commands.route.RouteHandler;
import space.moontalk.mc.commands.route.RouterParserHolder;

public interface ParsingMultiCommandHandler<T extends ParsingRouter> extends MultiCommandHandler<T>,
                                                                             RouterParserHolder {
    default @NotNull ParsingMultiCommandHandler<T> addCommandRoute(
        @NotNull String       commandRoutePath,
        @NotNull RouteHandler handler
    ) {
        val splitted       = commandRoutePath.split("\s", 2);
        val commandName    = splitted[0];
        val routePath      = splitted.length > 1 ? splitted[1] : "";
        val commandHandler = getOrCreateCommandHandler(commandName);
        val router         = commandHandler.getRouter();

        router.addRoute(routePath, handler);

        return this;
    }
}
