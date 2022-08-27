package space.moontalk.mc.commands.placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import space.moontalk.mc.commands.message.GameModeNotFoundMessageProvider;

@Getter
public class GameModePlaceholder extends AbstractPlaceholder<GameMode> {
    private final @NotNull GameModeNotFoundMessageProvider messageProvider;

    public GameModePlaceholder(@NotNull GameModeNotFoundMessageProvider messageProvider) {
        super('g');
        this.messageProvider = messageProvider;
    }

    @Override
    public @NotNull List<@NotNull String> evalVariants() {
        return Arrays.stream(GameMode.values())
                     .map(gm -> gm.name().toLowerCase())
                     .collect(Collectors.toList());
    }

    @Override
    public @NotNull GameMode variantToObject(@NotNull String variant) throws GameModeNotFoundException {
        try {
            return GameMode.valueOf(variant.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new GameModeNotFoundException(messageProvider, variant);
        }
    }
}
