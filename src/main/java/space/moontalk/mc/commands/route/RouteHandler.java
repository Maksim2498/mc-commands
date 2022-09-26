package space.moontalk.mc.commands.route;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public interface RouteHandler {
    default boolean canRoute(@NotNull CommandSender sender) {
        return hasPermission(sender) && isOfClass(sender);
    }

    default boolean hasPermission(@NotNull CommandSender sender) {
        val permission = getPermission();

        return permission.isBlank() 
            || sender.hasPermission(permission);
    }

    default @NotNull String getPermission() {
        return "";
    }

    default boolean isOfClass(@NotNull CommandSender sender) {
        val classes = getClasses();

        if (classes.isEmpty())
            return true;

        val senderClass = sender.getClass();

        return classes.stream().anyMatch(c -> c.isAssignableFrom(senderClass));
    }

    default @NotNull Set<Class<?>> getClasses() {
        return Collections.unmodifiableSet(new HashSet<>());
    }

    void onRoute(@NotNull RouteCall call) throws Exception;
}
