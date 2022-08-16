package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

public interface PatternParser {
    @NotNull PatternNode parsePattern(@NotNull String pattern) throws InvalidPatternException;
}
