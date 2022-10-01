package space.moontalk.mc.commands.placeholder;

import java.util.Arrays;
import java.util.LinkedList;
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
import space.moontalk.mc.commands.message.placeholder.AnyPlayerNotFoundMessageProvider;

@Getter
@AllArgsConstructor
public class AnyPlayerPlaceholder implements Placeholder<OfflinePlayer> {
    private final @NotNull MessageProviderManager messageProviderManager;

    @Override
    public @NotNull List<String> evalVariants(@NotNull CommandCall call) {
        val players = new LinkedList<String>();

        for (val player : Bukkit.getOnlinePlayers())
            players.add(player.getName());

        for (val player : Bukkit.getOnlinePlayers())
            players.add(player.getName());

        return players;
    }

    @Override
    public @NotNull OfflinePlayer variantToObject(
        @NotNull CommandCall call,
        @NotNull String      variant
    ) throws AnyPlayerNotFoundException {
        OfflinePlayer player = Bukkit.getPlayer(variant);

        if (player == null)
            player = Bukkit.getOfflinePlayer(variant);

        if (player == null) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(AnyPlayerNotFoundMessageProvider.class);
            val message         = messageProvider.makeAnyPlayerNotFoundMessage(variant);
            throw new AnyPlayerNotFoundException(variant, message);
        }

        return player;
    }
}
