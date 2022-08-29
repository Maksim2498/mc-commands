package space.moontalk.mc.commands.route;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.val;

public interface RouteHandler {
    default boolean canRoute(@NotNull CommandSender sender) {
        val permission = getPermission();

        if (permission != null
         && !sender.hasPermission(permission))
            return false;

        val senderClass = sender.getClass();
        val classes     = getClasses();

        return classes.isEmpty()
            || classes.stream().anyMatch(c -> senderClass.isInstance(c));
    }

    default @Nullable String getPermission() {
        return null;
    }

    default @NotNull List<Class<?>> getClasses() {
        return Collections.emptyList();
    }

    void onRoute(@NotNull RouteCall call) throws Exception;
}
