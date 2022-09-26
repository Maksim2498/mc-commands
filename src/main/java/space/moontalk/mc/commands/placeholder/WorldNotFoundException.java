package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorldNotFoundException extends PlaceholderException {
    private @NotNull String worldName;

    public WorldNotFoundException(@NotNull String worldName) {
        this(worldName, String.format("world %s not found", worldName));
    }

    public WorldNotFoundException(@NotNull String worldName, @NotNull String message) {
        super(message);
        this.worldName = worldName;
    }
}
