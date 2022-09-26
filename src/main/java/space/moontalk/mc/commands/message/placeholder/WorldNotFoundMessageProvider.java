package space.moontalk.mc.commands.message.placeholder;

import org.jetbrains.annotations.NotNull;

public interface WorldNotFoundMessageProvider {
    default @NotNull String makeWorldNotFoundMessage(@NotNull String name) {
        return String.format("World %s not found", name);
    }
}
