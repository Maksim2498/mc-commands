package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.val;

import space.moontalk.mc.commands.message.DefaultMessageProviderManager;
import space.moontalk.mc.commands.message.MessageProviderManager;

public class DefaultPlaceholderManager extends TreePlaceholderManager {
    public DefaultPlaceholderManager() {
        this(new DefaultMessageProviderManager());
    }

    public DefaultPlaceholderManager(@NotNull MessageProviderManager messageProviderManager) {
        val anyPlayer = new AnyPlayerPlaceholder(messageProviderManager);
        setPlaceholder('a', anyPlayer);

        val gameMode = new GameModePlaceholder(messageProviderManager);
        setPlaceholder('g', gameMode);

        val offlinePlayer = new OfflinePlayerPlaceholder(messageProviderManager);
        setPlaceholder('o', offlinePlayer);

        val player = new PlayerPlaceholder(messageProviderManager);
        setPlaceholder('p', player);

        val world = new WorldPlaceholder(messageProviderManager);
        setPlaceholder('w', world);
    }
}
