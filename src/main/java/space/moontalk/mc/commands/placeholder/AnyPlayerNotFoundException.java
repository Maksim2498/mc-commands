package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnyPlayerNotFoundException extends PlaceholderException {
    private @NotNull String playerName;

    public AnyPlayerNotFoundException(@NotNull String playerName) {
        this(playerName, String.format("online/offline player %s not found", playerName));
    }

    public AnyPlayerNotFoundException(@NotNull String playerName, @NotNull String message) {
        super(message);
        this.playerName = playerName;
    }
}
