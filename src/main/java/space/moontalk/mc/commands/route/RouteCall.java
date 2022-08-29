package space.moontalk.mc.commands.route;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import space.moontalk.mc.commands.CommandCall;

@Getter
public class RouteCall extends CommandCall {
    private final @NotNull Object[] placeholded;

    public RouteCall(
        @NotNull CommandSender commandSender,
        @NotNull Command       command,
        @NotNull String        label,
        @NotNull String[]      args,
        @NotNull Object[]      placeholded
    ) {
        super(commandSender, command, label, args);
        this.placeholded = placeholded;
    }

    public RouteCall(@NotNull CommandCall commandCall, @NotNull Object[] placeholded) {
        super(
            commandCall.getCommandSender(),
            commandCall.getCommand(),
            commandCall.getLabel(),
            commandCall.getArgs()
        );

        this.placeholded = placeholded;
    }

    public <T> @NotNull T getPlaceholdedAtOrNull(int index) {
        return 0 <= index && index < placeholded.length ? getPlaceholdedAt(index) : null;
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull T getPlaceholdedAt(int index) {
        return (T) placeholded[index];
    }
}
