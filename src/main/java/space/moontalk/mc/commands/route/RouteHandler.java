package space.moontalk.mc.commands.route;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public interface RouteHandler {
    default boolean canRoute(@NotNull CommandSender sender) {
        val permission = getPermission();

        if (!permission.isBlank()
         && !sender.hasPermission(permission))
            return false;

        val senderClass = sender.getClass();
        val classes     = getClasses();

        return classes.isEmpty()
            || classes.stream()
                      .anyMatch(c -> c.isAssignableFrom(senderClass));
    }

    default @NotNull String getPermission() {
        return "";
    }

    default @NotNull List<Class<?>> getClasses() {
        return Collections.emptyList();
    }

    void onRoute(@NotNull RouteCall call) throws Exception;
}
