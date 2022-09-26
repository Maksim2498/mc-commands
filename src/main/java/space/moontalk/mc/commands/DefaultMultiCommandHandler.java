package space.moontalk.mc.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.message.DefaultMessageProviderManager;
import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.placeholder.DefaultPlaceholderManager;
import space.moontalk.mc.commands.placeholder.PlaceholderManager;
import space.moontalk.mc.commands.route.DefaultRouteParser;
import space.moontalk.mc.commands.route.DefaultRouter;
import space.moontalk.mc.commands.route.ParsingRouter;
import space.moontalk.mc.commands.route.Route;
import space.moontalk.mc.commands.route.RouteParser;

public class DefaultMultiCommandHandler implements ParsingMultiCommandHandler<ParsingRouter> {
    private final @NotNull Map<String, DefaultCommandHandler> commandHandlers = new HashMap<>();

    @Getter
    private final @NotNull JavaPlugin plugin;

    @Getter
    private final @NotNull MessageProviderManager messageProviderManager;

    @Getter
    private final @NotNull PlaceholderManager placeholderManager;

    @Getter
    private final @NotNull RouteParser routeParser;

    public DefaultMultiCommandHandler(@NotNull JavaPlugin plugin) {
        this.plugin            = plugin;
        messageProviderManager = new DefaultMessageProviderManager();
        placeholderManager     = new DefaultPlaceholderManager(messageProviderManager);
        routeParser            = new DefaultRouteParser(placeholderManager);
    }

    public DefaultMultiCommandHandler(
        @NotNull JavaPlugin             plugin,
        @NotNull MessageProviderManager messageProviderManager,
        @NotNull PlaceholderManager     placeholderManager,
        @NotNull RouteParser            routeParser
    ) {
        this.plugin                 = plugin;
        this.messageProviderManager = messageProviderManager;
        this.placeholderManager     = placeholderManager;
        this.routeParser            = routeParser;
    }

    @Override
    public @NotNull MultiCommandHandler<ParsingRouter> addCommandRoute(@NotNull String commandName, @NotNull Route route) {
        val commandHandler = getOrCreateCommandHandler(commandName);
        val router         = commandHandler.getRouter();

        router.addRoute(route);

        return this;
    }

    @Override
    public @Nullable CommandHandler<ParsingRouter> getCommandHandlerOrNull(@NotNull String commandName) {
        return commandHandlers.get(commandName);
    }

    @Override
    public @NotNull CommandHandler<ParsingRouter> getOrCreateCommandHandler(@NotNull String commandName) {
        return commandHandlers.computeIfAbsent(commandName, k -> {
            val router         = new DefaultRouter(routeParser);
            val commandHandler = new DefaultCommandHandler(commandName, plugin, router, messageProviderManager);
            return commandHandler;
        });
    }

    @Override
    public int getCommandHandlersCount() {
        return commandHandlers.size();
    }

    @Override
    public @NotNull Set<String> getCommandsNamesSet() {
        return Collections.unmodifiableSet(commandHandlers.keySet());
    }

    @Override
    public @NotNull Collection<CommandHandler<ParsingRouter>> getCommandHandlers() {
        return Collections.unmodifiableCollection(commandHandlers.values());
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Set<Map.Entry<String, CommandHandler<ParsingRouter>>> getCommandHandleresEntrySet() {
        final Object set = Collections.unmodifiableSet(commandHandlers.entrySet());
        return (Set<Map.Entry<String, CommandHandler<ParsingRouter>>>) set;
    }
}
