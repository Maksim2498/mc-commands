package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameModeNotFoundException extends PlaceholderException {
    private @NotNull String gameModeName;

    public GameModeNotFoundException(@NotNull String gameModeName) {
        this(gameModeName, String.format("game mode %s not found", gameModeName));
    }

    public GameModeNotFoundException(@NotNull String gameModeName, @NotNull String message) {
        super(message);
        this.gameModeName = gameModeName;
    }
}
