package space.moontalk.mc.commands.placeholder;

import java.util.Map;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.val;

public class TreePlaceholderManager implements PlaceholderManager {
    private final @NotNull Map<Character, Placeholder<?>> placeholders = new TreeMap<>();

    @Override
    public @Nullable Placeholder<?> addPlaceholder(@NotNull Placeholder<?> placeholder) {
        return placeholders.put(placeholder.getShortName(), placeholder);
    }

    @Override
    public @NotNull Placeholder<?> getPlaceholder(
        char shortName
    ) throws PlaceholderNotFoundException {
        val placeholder = placeholders.get(shortName);

        if (placeholder == null)
            throw new PlaceholderNotFoundException(shortName);

        return placeholder;
    }

    @Override
    public @Nullable Placeholder<?> removePlaceholder(char shortName) {
        return placeholders.remove(shortName);
    }
}
