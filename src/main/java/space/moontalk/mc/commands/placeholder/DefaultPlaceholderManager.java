package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import space.moontalk.mc.commands.message.DefaultDefaultPlaceholderManagerMessageProvider;
import space.moontalk.mc.commands.message.DefaultPlaceholderManagerMessageProvider;

@Getter
public class DefaultPlaceholderManager extends TreePlaceholderManager {
    private @NotNull DefaultPlaceholderManagerMessageProvider messageProvider;

    public DefaultPlaceholderManager() {
        this(DefaultDefaultPlaceholderManagerMessageProvider.INSTANCE);
    }
    
    public DefaultPlaceholderManager(@NotNull DefaultPlaceholderManagerMessageProvider messageProvider) {
        this.messageProvider = messageProvider;

        addPlaceholder(new GameModePlaceholder(messageProvider));
        addPlaceholder(new PlayerPlaceholder(messageProvider));
        addPlaceholder(new WorldPlaceholder(messageProvider));
    }
}
