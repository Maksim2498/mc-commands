package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
@AllArgsConstructor
public class InvalidRouteException extends Exception {
    private final @NotNull String route;
    private final int             position;

    @Override
    public @NotNull String getLocalizedMessage() {
        return getMessage();
    }

    @Override
    public @NotNull String getMessage() {
        val header = "Invalid route syntax.";
        val prefix = "Route: ";
        val offset = prefix.length() + position + 1;
        val format = "%s\n%s\"%s\"\n%" + offset + "c";

        return String.format(format, header, prefix, route, '^');
    }
}
