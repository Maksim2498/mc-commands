package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public interface PlaceholderManager {
    @Nullable Placeholder<?> addPlaceholder(@NotNull Placeholder<?> placeholder);

    @NotNull Placeholder<?> getPlaceholder(char shortName) throws PlaceholderNotFoundException;

    @Nullable Placeholder<?> removePlaceholder(char shortName);
}
