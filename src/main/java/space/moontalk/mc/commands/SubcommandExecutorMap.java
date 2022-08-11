package space.moontalk.mc.commands;

import java.util.Map;
import java.util.TreeMap;

import org.jetbrains.annotations.NotNull;

import lombok.val;

public class SubcommandExecutorMap implements SubcommandExecutorsProvider {
    private final @NotNull Map<@NotNull String, @NotNull SubcommandExecutor> executors = new TreeMap<>();

    @Override
    public void addSubcommandExecutor(@NotNull String subcommand, @NotNull SubcommandExecutor executor) {
        subcommand = subcommand.toLowerCase();    
        executors.put(subcommand, executor);
    }

    @Override
    public @NotNull SubcommandExecutor getSubcommandExecutor(@NotNull String subcommand) 
                                                             throws InvalidSubcommandException {
        val executor = executors.get(subcommand);

        if (executor == null)
            throw new InvalidSubcommandException(subcommand);

        return executor;
    }
}
