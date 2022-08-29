package space.moontalk.mc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.route.ComputingRouter;
import space.moontalk.mc.commands.route.ParsingRouter;

@Getter
@AllArgsConstructor
public class CommandHandler implements TabCompleter,
                                       CommandExecutor {
    private final @NotNull ParsingRouter router;

    public CommandHandler() {
        this(new ComputingRouter());
    }

    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull CommandSender sender,
        @NotNull Command       command,
        @NotNull String        label, 
        @NotNull String[]      args
    ) {
        val commandCall = new CommandCall(sender, command, label, args);
        return router.evalCompletions(commandCall);
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command       command, 
        @NotNull String        label,
        @NotNull String[]      args
    ) {
        val commandCall = new CommandCall(sender, command, label, args);

        try {
            router.route(commandCall);
        } catch (CommandException exception) {
            sendMessageIfHas(sender, exception);
            return exception.getReturnCode();
        } catch (Exception exception) {
            sendMessageIfHas(sender, exception);
        }

        return true;
    }

    private void sendMessageIfHas(@NotNull CommandSender sender, @NotNull Exception exception) {
        val message = exception.getMessage();

        if (message != null && !message.isBlank())
            sender.sendMessage(message);
    }

    public void attach(@NotNull PluginCommand command) {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
