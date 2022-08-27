package space.moontalk.mc.commands.message;

import org.jetbrains.annotations.NotNull;

public interface GameModeNotFoundMessageProvider {
    default @NotNull String makeGameModeNotFoundMessage(@NotNull String gameMode) {
        return String.format("Game mode %s not found", gameMode);
    }
}
