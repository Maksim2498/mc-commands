package space.moontalk.mc.commands.placeholder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.CommandCall;
import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.message.placeholder.OfflinePlayerNotFoundMessageProvider;

@Getter
@AllArgsConstructor
public class OfflinePlayerPlaceholder implements Placeholder<OfflinePlayer> {
    private final @NotNull MessageProviderManager messageProviderManager;

    @Override
    public @NotNull List<String> evalVariants(@NotNull CommandCall call) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                     .map(p -> p.getName())
                     .collect(Collectors.toList());
    }

    @Override
    public @NotNull OfflinePlayer variantToObject(
        @NotNull CommandCall call,
        @NotNull String      variant
    ) throws OfflinePlayerNotFoundException {
        val player = Bukkit.getOfflinePlayer(variant);

        if (player == null) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(OfflinePlayerNotFoundMessageProvider.class);
            val message         = messageProvider.makeOfflinePlayerNotFoundMessage(variant);
            throw new OfflinePlayerNotFoundException(variant, message);
        }

        return player;
    }
}
