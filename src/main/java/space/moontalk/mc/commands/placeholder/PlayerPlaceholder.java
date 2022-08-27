package space.moontalk.mc.commands.placeholder;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.message.PlayerNotFoundMessageProvider;

@Getter
public class PlayerPlaceholder extends AbstractPlaceholder<Player> {
    private final @NotNull PlayerNotFoundMessageProvider messageProvider;

    public PlayerPlaceholder(@NotNull PlayerNotFoundMessageProvider messageProvider) {
        super('p');
        this.messageProvider = messageProvider;
    }

    @Override
    public @NotNull List<String> evalVariants() {
        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
    }

    @Override
    public @NotNull Player variantToObject(@NotNull String variant) throws PlayerNotFoundException {
        val player = Bukkit.getPlayer(variant);

        if (player == null)
            throw new PlayerNotFoundException(messageProvider, variant);

        return player;
    }
}
