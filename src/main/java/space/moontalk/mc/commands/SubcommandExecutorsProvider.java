package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

public interface SubcommandExecutorsProvider {
    void addSubcommandExecutor(@NotNull String subcommand, @NotNull SubcommandExecutor executor);
    @NotNull SubcommandExecutor getSubcommandExecutor(@NotNull String subcommand) throws InvalidSubcommandException;
}
