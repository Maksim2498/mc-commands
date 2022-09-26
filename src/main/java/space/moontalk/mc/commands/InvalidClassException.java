package space.moontalk.mc.commands;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidClassException extends CommandException {
    private @NotNull Set<Class<?>> classes;

    public InvalidClassException(@NotNull Set<Class<?>> classes) {
        this(classes, "missing required class");
    }

    public InvalidClassException(@NotNull Set<Class<?>> classes, @NotNull String message) {
        super(message);
        this.classes = classes;
        setReturnCode(true);
    }
}
