package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

public interface PatternAutomataBuilder {
    @NotNull PatternAutomata buildAutomata(@NotNull PatternNode ast);
}
