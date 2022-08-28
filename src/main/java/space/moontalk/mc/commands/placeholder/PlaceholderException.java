package space.moontalk.mc.commands.placeholder;

import org.bukkit.command.CommandException;

import org.jetbrains.annotations.Nullable;

public class PlaceholderException extends CommandException {
    public PlaceholderException() {
        super();
        setReturnCode(true);
    }

    public PlaceholderException(@Nullable String message) {
        super(message);
        setReturnCode(true);
    }

    public PlaceholderException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
        setReturnCode(true);
    }
}
