package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

public class DefaultPatternAutomataBuilder implements PatternAutomataBuilder {
    public static final DefaultPatternAutomataBuilder INSTANCE = new DefaultPatternAutomataBuilder();

    private DefaultPatternAutomataBuilder() {}

    @Override
    public @NotNull PatternAutomata buildAutomata(@NotNull PatternNode ast) {
        return new DefaultPatternAutomata(ast);
    }
}
