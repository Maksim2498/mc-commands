package space.moontalk.mc.commands.completion;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface PatternAutomata {
    @NotNull List<String> evalCompletions(@NotNull String[] args);
}
