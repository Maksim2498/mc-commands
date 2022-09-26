package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissingPermissionException extends CommandException {
    private @NotNull String permission;

    public MissingPermissionException(@NotNull String permission) {
        this(permission, String.format("missing %s permission", permission));
    }

    public MissingPermissionException(@NotNull String permission, @NotNull String message) {
        super(message);
        this.permission = permission;
        setReturnCode(true);
    }
}
