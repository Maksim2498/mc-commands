package space.moontalk.mc.commands;

import org.checkerframework.checker.nullness.qual.Nullable;

public class CommandException extends Exception {
    private boolean returnValue;

    public CommandException() {

    }
    
    public CommandException(@Nullable String message) {
        super(message);
    }

    public CommandException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public CommandException(@Nullable Throwable cause) {
        super(cause);
    }

    public boolean getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(boolean value) {
        returnValue = value;
    }
}
