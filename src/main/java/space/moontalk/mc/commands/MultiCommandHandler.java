package space.moontalk.mc.commands;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.val;

import space.moontalk.mc.commands.message.MessageProviderManagerHolder;
import space.moontalk.mc.commands.placeholder.PlaceholderManagerHolder;
import space.moontalk.mc.commands.route.Route;
import space.moontalk.mc.commands.route.RouteHandler;
import space.moontalk.mc.commands.route.RouteNode;
import space.moontalk.mc.commands.route.Router;

public interface MultiCommandHandler<T extends Router> extends PlaceholderManagerHolder,
                                                               MessageProviderManagerHolder {
    // Add Route:

    default @NotNull MultiCommandHandler<T> addCommandRoute(
        @NotNull String       commandName, 
        @NotNull RouteNode    tree,
        @NotNull RouteHandler handler
    ) {
        val route = new Route(tree, handler);
        return addCommandRoute(commandName, route);
    }

    @NotNull MultiCommandHandler<T> addCommandRoute(@NotNull String commandName, @NotNull Route route); 

    // Has:

    default boolean hasCommandHandler(@NotNull String commandName) {
        return getCommandHandlerOrNull(commandName) != null;
    }

    default boolean hasCommandHandlers() {
        return getCommandHandlersCount() != 0;
    }

    // Get:

    default CommandHandler<T> getCommandHandler(@NotNull String commandName) {
        val commandHandler = getCommandHandlerOrNull(commandName);

        if (commandHandler == null) {
            val message = String.format("command handler for command %s not found", commandName);
            throw new IllegalArgumentException(message);
        }

        return commandHandler;
    }

    @Nullable CommandHandler<T> getCommandHandlerOrNull(@NotNull String commandName);
    @NotNull CommandHandler<T> getOrCreateCommandHandler(@NotNull String commandName);

    // Count:

    int getCommandHandlersCount();

    // Sets:

    @NotNull Set<String> getCommandsNamesSet();
    @NotNull Collection<CommandHandler<T>> getCommandHandlers();
    @NotNull Set<Map.Entry<String, CommandHandler<T>>> getCommandHandleresEntrySet();

    // Plugin:

    @NotNull JavaPlugin getPlugin();
}
