package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.Nullable;

import lombok.val;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

public interface PlaceholderManager {
    // Set:

    @Nullable Placeholder<?> setPlaceholder(char name, @NotNull Placeholder<?> placeholder);

    // Get:

    default @NotNull Placeholder<?> getPlaceholder(char name) {
        val placeholder = getPlaceholderOrNull(name);

        if (placeholder == null) {
            val message = String.format("placeholder with short name %s not found", name);
            throw new IllegalArgumentException(message);
        }

        return placeholder;
    }

    @Nullable Placeholder<?> getPlaceholderOrNull(char name);

    // Remove:

    @Nullable Placeholder<?> removePlaceholder(char name);
    void clearPlaceholders();

    // Has:

    boolean hasPlaceholder(char name);

    default boolean hasPlaceholders() {
        return getPlaceholdersCount() != 0;
    }
    
    // Count:

    int getPlaceholdersCount();

    // Sets:

    @NotNull Set<Character> getNamesSet();
    @NotNull Collection<Placeholder<?>> getPlaceholders();
    @NotNull Set<Map.Entry<Character, Placeholder<?>>> getPlaceholdersEntrySets();
}
