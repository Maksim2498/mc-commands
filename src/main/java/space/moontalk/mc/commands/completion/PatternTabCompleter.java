package space.moontalk.mc.commands.completion;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

public class PatternTabCompleter implements TabCompleter {
    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Config {
        private final @NotNull PatternParser          patternParser;
        private final @NotNull PatternAutomataBuilder patternAutomataBuilder;
        private final @NotNull List<String>           patterns;

        public static @NotNull Config makeDefault(@NotNull List<String> patterns) {
            return Config.builder()
                         .patternParser(new DefaultPatternParser())
                         .patternAutomataBuilder(DefaultPatternAutomataBuilder.INSTANCE)
                         .patterns(patterns)
                         .build();
        }
    }

    private final @NotNull PatternAutomata automata; 

    public PatternTabCompleter(@NotNull String ...patterns) throws InvalidPatternException {
        this(Arrays.asList(patterns));
    }

    public PatternTabCompleter(@NotNull List<String> patterns) throws InvalidPatternException {
        this(Config.makeDefault(patterns));
    }

    public PatternTabCompleter(@NotNull Config config) throws InvalidPatternException {
        val builder = config.getPatternAutomataBuilder();
        val ast     = patternsToAST(config);

        automata = builder.buildAutomata(ast); 
    }

    private static @NotNull PatternNode patternsToAST(@NotNull Config config) throws InvalidPatternException {
        PatternNode root     = PatternNode.None.INSTANCE;
        val         patterns = config.getPatterns();
        val         parser   = config.getPatternParser();

        for (val pattern : patterns) {
            val parsed  = parser.parsePattern(pattern);
            val newRoot = new PatternNode.Or(root, parsed);

            root = newRoot;
        }

        return root;
    }

    @Override
    public @NotNull List<String> onTabComplete(
        @NotNull CommandSender sender, 
        @NotNull Command       command,
        @NotNull String        label, 
        @NotNull String[]      args 
    ) {
        return automata.evalCompletions(args);
    }
}
