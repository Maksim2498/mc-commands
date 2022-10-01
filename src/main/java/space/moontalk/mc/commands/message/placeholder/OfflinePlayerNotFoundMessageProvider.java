package space.moontalk.mc.commands.message.placeholder;

import org.jetbrains.annotations.NotNull;

public interface OfflinePlayerNotFoundMessageProvider {
    default @NotNull String makeOfflinePlayerNotFoundMessage(@NotNull String name) {
        return String.format("Offline player %s not found", name);
    }
}
