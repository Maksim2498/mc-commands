package space.moontalk.mc.commands;

import org.jetbrains.annotations.Nullable;

public class CommandException extends Exception {
    private boolean returnCode = true;

    public CommandException() {

    }

    public CommandException(@Nullable String message) {
        super(message);
    }

    public CommandException(@Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public boolean getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(boolean val) {
        returnCode = val;
    }
}
