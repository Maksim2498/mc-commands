package space.moontalk.mc.commands.placeholder;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.CommandCall;
import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.message.placeholder.PlayerNotFoundMessageProvider;

@Getter
@AllArgsConstructor
public class PlayerPlaceholder implements Placeholder<Player> {
    private final @NotNull MessageProviderManager messageProviderManager;

    @Override
    public @NotNull List<String> evalVariants(@NotNull CommandCall call) {
        return Bukkit.getOnlinePlayers().stream()
                                        .map(p -> p.getName())
                                        .collect(Collectors.toList());
    }

    @Override
    public @NotNull Player variantToObject(@NotNull CommandCall call, @NotNull String variant) throws PlayerNotFoundException {
        val player = Bukkit.getPlayer(variant);

        if (player == null) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(PlayerNotFoundMessageProvider.class);
            val message         = messageProvider.makePlayerNotFoundMessage(variant);
            throw new PlayerNotFoundException(variant, message);
        }

        return player;
    }
}
