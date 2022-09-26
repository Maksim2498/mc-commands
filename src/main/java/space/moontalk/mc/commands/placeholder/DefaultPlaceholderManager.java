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
        val gameModePlaceholder = new GameModePlaceholder(messageProviderManager);
        setPlaceholder('g', gameModePlaceholder);

        val playerPlaceholder = new PlayerPlaceholder(messageProviderManager);
        setPlaceholder('p', playerPlaceholder);

        val worldPlaceholder = new WorldPlaceholder(messageProviderManager);
        setPlaceholder('w', worldPlaceholder);
    }
}
