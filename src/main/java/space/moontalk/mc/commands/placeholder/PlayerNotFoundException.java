package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerNotFoundException extends PlaceholderException {
    private @NotNull String playerName;

    public PlayerNotFoundException(@NotNull String playerName) {
        this(playerName, String.format("player %s not found", playerName));
    }

    public PlayerNotFoundException(@NotNull String playerName, @NotNull String message) {
        super(message);
        this.playerName = playerName;
    }
}
