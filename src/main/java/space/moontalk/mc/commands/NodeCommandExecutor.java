package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public class NodeCommandExecutor implements SubcommandExecutor, SubcommandExecutorsProvider {
    private final @NotNull SubcommandExecutorsProvider executors = new SubcommandExecutorMap();

    @Override
    public void onSubcommand(@NotNull SubcommandCall call) throws Exception {
        val args       = call.getSubargs();
        val subcommand = Utility.extractSubcommand(args);
        val executor   = getSubcommandExecutor(subcommand);
        val subargs    = Utility.extractSubargs(args);
            
        if (executor instanceof SubcommandExecutorWithMeta withMeta) {
            withMeta.checkArgsNum(subargs.length);
            withMeta.checkPermissions(call.getSender());
        }

        val subcall = SubcommandCall.builder()
                                    .sender(call.getSender())
                                    .command(call.getCommand())
                                    .label(call.getLabel())
                                    .subargs(subargs)
                                    .subcommand(subcommand)
                                    .build();

        executor.onSubcommand(subcall);
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
