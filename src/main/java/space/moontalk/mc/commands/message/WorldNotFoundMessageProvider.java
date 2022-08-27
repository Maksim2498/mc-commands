package space.moontalk.mc.commands.message;

import org.jetbrains.annotations.NotNull;

public interface WorldNotFoundMessageProvider {
    default @NotNull String makeWorldNotFoundMessage(@NotNull String name) {
        return String.format("World %s not found", name);
    }
}
