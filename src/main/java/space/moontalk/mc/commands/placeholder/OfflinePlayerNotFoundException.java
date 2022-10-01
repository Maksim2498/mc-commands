package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflinePlayerNotFoundException extends PlaceholderException {
    private @NotNull String playerName;

    public OfflinePlayerNotFoundException(@NotNull String playerName) {
        this(playerName, String.format("offline player %s not found", playerName));
    }

    public OfflinePlayerNotFoundException(@NotNull String playerName, @NotNull String message) {
        super(message);
        this.playerName = playerName;
    }
}
