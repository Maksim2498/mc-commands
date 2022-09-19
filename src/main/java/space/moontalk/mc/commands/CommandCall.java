package space.moontalk.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandCall {
    private final @NotNull CommandSender commandSender;
    private final @NotNull Command       command;
    private final @NotNull String        label;
    private final @NotNull String[]      args;

    @SuppressWarnings("unchecked")
    public <T extends CommandSender> @NotNull T getCommandSender() {
        return (T) commandSender;
    }

    public @NotNull String getArgAtOrNull(int index) {
        return 0 <= index && index < args.length ? args[index] : null;
    }
}
