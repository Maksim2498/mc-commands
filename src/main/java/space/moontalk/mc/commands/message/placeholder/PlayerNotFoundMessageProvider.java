package space.moontalk.mc.commands.message.placeholder;

import org.jetbrains.annotations.NotNull;

public interface PlayerNotFoundMessageProvider {
    default @NotNull String makePlayerNotFoundMessage(@NotNull String name) {
        return String.format("Player %s not found", name);
    }
}
