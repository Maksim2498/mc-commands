package space.moontalk.mc.commands.placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.GameMode;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.CommandCall;
import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.message.placeholder.GameModeNotFoundMessageProvider;

@Getter
@AllArgsConstructor
public class GameModePlaceholder implements Placeholder<GameMode> {
    private final @NotNull MessageProviderManager messageProviderManager;

    @Override
    public @NotNull List<String> evalVariants(@NotNull CommandCall call) {
        return Arrays.stream(GameMode.values())
                     .map(gm -> gm.name().toLowerCase())
                     .collect(Collectors.toList());
    }

    @Override
    public @NotNull GameMode variantToObject(@NotNull CommandCall call, @NotNull String variant) throws GameModeNotFoundException {
        try {
            return GameMode.valueOf(variant.toUpperCase());
        } catch (IllegalArgumentException exception) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(GameModeNotFoundMessageProvider.class);
            val message         = messageProvider.makeGameModeNotFoundMessage(variant);
            throw new GameModeNotFoundException(variant, message);
        }
    }
}
