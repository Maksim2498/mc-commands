package space.moontalk.mc.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.route.Router;

public interface CommandHandler<T extends Router> extends TabCompleter, 
                                                          CommandExecutor {
    @NotNull T getRouter();
    @NotNull PluginCommand getCommand();
}
