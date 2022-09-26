package space.moontalk.mc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.message.route.InvalidClassMessageProvider;
import space.moontalk.mc.commands.message.route.MissingPermissionMessageProvider;
import space.moontalk.mc.commands.route.ParsingRouter;

@Getter
public class DefaultCommandHandler implements CommandHandler<ParsingRouter> {
    private final @NotNull PluginCommand          command;
    private final @NotNull ParsingRouter          router;
    private final @NotNull MessageProviderManager messageProviderManager;

    public DefaultCommandHandler(
        @NotNull PluginCommand          command, 
        @NotNull ParsingRouter          router,
        @NotNull MessageProviderManager messageProviderManager
        ) {
        this.command                = command;
        this.router                 = router;
        this.messageProviderManager = messageProviderManager;

        attach();
    }

    public DefaultCommandHandler(
        @NotNull String                 commandName, 
        @NotNull JavaPlugin             plugin,
        @NotNull ParsingRouter          router,
        @NotNull MessageProviderManager messageProviderManager
    ) {
        val command = plugin.getCommand(commandName);

        if (command == null) {
            val message = String.format("command %s not found", commandName);
            throw new IllegalArgumentException(message);
        }

        this.command                = command;
        this.router                 = router;
        this.messageProviderManager = messageProviderManager;

        attach();
    }

    private void attach() {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public @Nullable List<String> onTabComplete(
        @NotNull CommandSender sender,
        @NotNull Command       command,
        @NotNull String        label, 
        @NotNull String[]      args
    ) {
        val call = new CommandCall(sender, command, label, args);
        return router.evalCompletions(call);
    }

    @Override
    public boolean onCommand(
        @NotNull CommandSender sender,
        @NotNull Command       command, 
        @NotNull String        label,
        @NotNull String[]      args
    ) {
        val call = new CommandCall(sender, command, label, args);

        try {
            router.route(call);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (InvalidClassException exception) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(InvalidClassMessageProvider.class); 
            val classes         = exception.getClasses();
            val message         = messageProvider.makeInvalidClassMessage(classes);
            sender.sendMessage(message);
            return exception.getReturnCode();
        } catch (MissingPermissionException exception) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(MissingPermissionMessageProvider.class); 
            val permission      = exception.getPermission();
            val message         = messageProvider.makeMissingPermissionMessage(sender, permission);
            sender.sendMessage(message);
            return exception.getReturnCode();
        } catch (CommandException exception) {
            sendMessageIfHas(sender, exception);
            return exception.getReturnCode();
        }catch (Exception exception) {
            sendMessageIfHas(sender, exception);
        }

        return true;
    }

    private void sendMessageIfHas(@NotNull CommandSender sender, @NotNull Exception exception) {
        val message = exception.getMessage();

        if (message != null && !message.isBlank())
            sender.sendMessage(message);
    }
}
