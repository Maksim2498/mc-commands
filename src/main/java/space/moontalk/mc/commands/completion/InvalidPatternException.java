package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
@AllArgsConstructor
public class InvalidPatternException extends Exception {
    private @NotNull String pattern; 
    private int             position;

    @Override
    public @NotNull String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public @NotNull String getMessage() {
        val header = "Invalid pattern syntax.";
        val prefix = "Pattern: ";
        val offset = prefix.length() + position + 1;
        val format = "%s\n%s%s\n%" + offset + "c";

        return String.format(format, header, prefix, pattern, '^');
    }
}
