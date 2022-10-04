package space.moontalk.mc.commands.message.route;

import java.util.Set;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

public interface InvalidClassMessageProvider {
    default @NotNull String makeInvalidClassMessage(@NotNull CommandSender sender, @NotNull Set<Class<?>> classes) {
        return String.format("You cannot run this command");
    }
}
