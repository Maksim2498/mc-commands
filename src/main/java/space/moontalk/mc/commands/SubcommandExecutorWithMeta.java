package space.moontalk.mc.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import lombok.val;

public interface SubcommandExecutorWithMeta extends SubcommandExecutor, SubcommandExecutorMetaHolder {
    default void checkArgsNum(int provided) throws InvalidArgsNumException {
        val meta      = getSubcommandExecutorMeta();
        val argsRange = meta.getArgsRange(); 

        if (!argsRange.contains(provided))
            throw new InvalidArgsNumException(argsRange, provided);
    }

    default void checkPermissions(@NotNull CommandSender sender) throws MissingPermissionException {
        val meta                = getSubcommandExecutorMeta();
        val requiredPermissions = meta.getRequiredPermissions();

        for (val requiredPermission : requiredPermissions)
            if (!sender.hasPermission(requiredPermission))
                throw new MissingPermissionException(requiredPermission);
    }
}
