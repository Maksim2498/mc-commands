package space.moontalk.mc.commands.route;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import space.moontalk.mc.commands.CommandCall;

public class RouteCall extends CommandCall {
    @Getter
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

    @SuppressWarnings("unchecked")
    public <T> @NotNull T getPlaceholded(int index) {
        return (T) placeholded[index];
    }
}
