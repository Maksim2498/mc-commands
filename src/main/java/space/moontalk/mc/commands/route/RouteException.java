package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.Nullable;

import space.moontalk.mc.commands.CommandException;

public class RouteException extends CommandException {
    public RouteException() {}

    public RouteException(@Nullable String message) {
        super(message);
    }

    public RouteException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }
}
