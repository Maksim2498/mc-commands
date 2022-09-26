package space.moontalk.mc.commands.message.route;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

public interface MissingPermissionMessageProvider {
    default @NotNull String makeMissingPermissionMessage(@NotNull CommandSender sender, @NotNull String permission) {
        return String.format("You have no permission %s", permission);
    }
}
