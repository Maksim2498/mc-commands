package space.moontalk.mc.commands.message.placeholder;

import org.jetbrains.annotations.NotNull;

public interface AnyPlayerNotFoundMessageProvider {
    default @NotNull String makeAnyPlayerNotFoundMessage(@NotNull String name) {
        return String.format("Online/offline player %s not found", name);
    }
}
