package space.moontalk.mc.commands.message;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.val;

public interface MessageProviderManager {
    // Set:

    <T> @NotNull T setMessageProvider(@NotNull Class<T> c, @NotNull T messageProvider);
    
    // Get:

    default <T> T getMessageProvider(Class<T> c) {
        T messageProvider = getMessageProviderOrNull(c);

        if (messageProvider == null) {
            val name    = c.getName();
            val message = String.format("message provider for class %s not found", name);
            throw new IllegalArgumentException(message);
        }

        return messageProvider;
    }

    <T> @Nullable T getMessageProviderOrNull(@NotNull Class<T> c);

    default <T> @Nullable T getCompatibleMessageProvider(@NotNull Class<T> c) {
        val messageProvider = getCompatibleMessageProviderOrNull(c);

        if (messageProvider == null) {
            val name    = c.getName();
            val message = String.format("compatible message provider for class %s not found", name);
            throw new IllegalArgumentException(message);
        }

        return messageProvider;
    }

    default <T> @Nullable T getCompatibleMessageProviderOrNull(@NotNull Class<T> c) {
        val compatible = getCompatibleMessageProviders(c); 
        val first      = compatible.stream().findFirst();
        return first.orElse(null);
    }

    // Remove:

    <T> @Nullable T removeMessageProvider(@NotNull Class<T> c);
    void clearMessageProviders();

    // Has:

    <T> boolean hasMessageProvider(@NotNull Class<T> c);

    default boolean hasMessageProviders() {
        return getMessageProvidersCount() != 0;
    }

    // Count:

    int getMessageProvidersCount();

    // Sets:

    default <T> @NotNull Set<Class<T>> getCompatibleMessageProviderClassSet(@NotNull Class<T> c) {
        return getCompatibleMessageProvidersEntrySet(c).stream()
                                                       .map(e -> e.getKey())
                                                       .collect(Collectors.toSet());
    }

    default <T> @NotNull Collection<T> getCompatibleMessageProviders(@NotNull Class<T> c) {
        return getCompatibleMessageProvidersEntrySet(c).stream()
                                                       .map(e -> e.getValue())
                                                       .collect(Collectors.toList());
    }

    @NotNull Set<?> getMessageProviderClassSet();
    @NotNull Collection<?> getMessageProviders();
    @NotNull Set<Map.Entry<Class<?>, Object>> getMessageProvidersEntrySet();
    @NotNull <T> Set<Map.Entry<Class<T>, T>> getCompatibleMessageProvidersEntrySet(@NotNull Class<T> c);
}
