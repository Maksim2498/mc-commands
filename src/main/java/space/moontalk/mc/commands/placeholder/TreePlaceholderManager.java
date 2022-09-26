package space.moontalk.mc.commands.placeholder;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreePlaceholderManager implements PlaceholderManager {
    private final @NotNull Map<Character, Placeholder<?>> placeholders = new TreeMap<>();

    @Override
    public @Nullable Placeholder<?> setPlaceholder(char name, @NotNull Placeholder<?> placeholder) {
        return placeholders.put(name, placeholder);
    }

    @Override
    public @Nullable Placeholder<?> getPlaceholderOrNull(char name) {
        return placeholders.get(name);
    }

    @Override
    public @Nullable Placeholder<?> removePlaceholder(char name) {
        return placeholders.remove(name);
    }

    @Override
    public void clearPlaceholders() {
        placeholders.clear();
    }

    @Override
    public boolean hasPlaceholder(char name) {
        return placeholders.containsKey(name);
    }

    @Override
    public int getPlaceholdersCount() {
        return placeholders.size();
    }

    @Override
    public @NotNull Set<Character> getNamesSet() {
        return placeholders.keySet();
    }

    @Override
    public @NotNull Collection<Placeholder<?>> getPlaceholders() {
        return placeholders.values();
    }

    @Override
    public @NotNull Set<Entry<Character, Placeholder<?>>> getPlaceholdersEntrySets() {
        return placeholders.entrySet();
    }
}
