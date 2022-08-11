package space.moontalk.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public class RootCommandExecutor implements CommandExecutor, SubcommandExecutorsProvider {
    private final @NotNull SubcommandExecutorsProvider executors = new SubcommandExecutorMap();

    @Getter
    @Setter
    private @Nullable ErrorMessageProvider errorMessageProvider;

    public RootCommandExecutor() {

    }

    public RootCommandExecutor(ErrorMessageProvider errorMessageProvider) {
        this.errorMessageProvider = errorMessageProvider;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        
        try {
            val subcommand = Utility.extractSubcommand(args); 
            val executor   = getSubcommandExecutor(subcommand);
            val subargs    = Utility.extractSubargs(args);
            
            if (executor instanceof SubcommandExecutorWithMeta withMeta) {
                withMeta.checkArgsNum(subargs.length);
                withMeta.checkPermissions(sender);
            }

            val call = SubcommandCall.builder()
                                     .sender(sender)
                                     .command(command)
                                     .label(label)
                                     .subargs(subargs)
                                     .subcommand(subcommand)
                                     .build();

            executor.onSubcommand(call);
        } catch (Exception exception) {
            return handleException(exception, sender);
        }

        return true;
    }

    private boolean handleException(@NotNull Exception commonException, @NotNull CommandSender sender) {
        if (errorMessageProvider != null)
            if (commonException instanceof InvalidArgsNumException exception) {
                val required = exception.getRequired();
                val provided = exception.getProvided();
                val message  = errorMessageProvider.makeInvalidArgsNumMessage(required, provided);

                sender.sendMessage(message);
            } else if (commonException instanceof InvalidSubcommandException exception) {
                val subcommand = exception.getSubcommand();
                val message    = errorMessageProvider.makeInvalidSubcommandMessage(subcommand);

                sender.sendMessage(message);
            } else if (commonException instanceof MissingPermissionException exception) {
                val permission = exception.getRequiredPermission();
                val message    = errorMessageProvider.makeMissingPermissionMessage(permission);

                sender.sendMessage(message);
            } else if (commonException instanceof MissingSubcommandException exception) {
                val message = errorMessageProvider.makeMissingSubcommandMessage();

                sender.sendMessage(message);
            } else {
                val message = commonException.getMessage();

                if (message != null)
                    sender.sendMessage(message);
            }

        if (commonException instanceof CommandException commandException)
            return commandException.getReturnValue();

        return true;
    }

    @Override
    public void addSubcommandExecutor(@NotNull String subcommand, @NotNull SubcommandExecutor executor) {
        executors.addSubcommandExecutor(subcommand, executor); 
    }

    @Override
    public @NotNull SubcommandExecutor getSubcommandExecutor(@NotNull String subcommand) 
                                                             throws InvalidSubcommandException {
        return executors.getSubcommandExecutor(subcommand);
    }
}
