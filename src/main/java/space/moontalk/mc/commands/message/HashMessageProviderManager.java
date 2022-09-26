package space.moontalk.mc.commands.message;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.val;

public class HashMessageProviderManager implements MessageProviderManager {
    private final Map<Class<?>, Object> messageProviders = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> @NotNull T setMessageProvider(@NotNull Class<T> c, @NotNull T messageProvider) {
        return (T) messageProviders.put(c, messageProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T getMessageProviderOrNull(@NotNull Class<T> c) {
        return (T) messageProviders.get(c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T removeMessageProvider(@NotNull Class<T> c) {
        return (T) messageProviders.remove(c);
    }

    @Override
    public void clearMessageProviders() {
        messageProviders.clear();
    }

    @Override
    public <T> boolean hasMessageProvider(@NotNull Class<T> c) {
        return messageProviders.containsKey(c);
    }

    @Override
    public int getMessageProvidersCount() {
        return messageProviders.size();
    }

    @Override
    public @NotNull Set<?> getMessageProviderClassSet() {
        return messageProviders.keySet();
    }

    @Override
    public @NotNull Collection<?> getMessageProviders() {
        return messageProviders.values();
    }

    @Override
    public @NotNull Set<Entry<Class<?>, Object>> getMessageProvidersEntrySet() {
        return messageProviders.entrySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> Set<Map.Entry<Class<T>, T>> getCompatibleMessageProvidersEntrySet(@NotNull Class<T> c) {
        val compatible = new HashSet<Map.Entry<Class<T>, T>>();

        for (val entry : messageProviders.entrySet()) {
            val ec = entry.getKey();

            if (c.isAssignableFrom(ec)) 
                compatible.add((Map.Entry<Class<T>, T>) (Object) entry);
        }

        return compatible;
    }
}
