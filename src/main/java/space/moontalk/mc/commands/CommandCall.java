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
}
