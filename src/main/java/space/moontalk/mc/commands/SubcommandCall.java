package space.moontalk.mc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubcommandCall {
    private final @NotNull Command       command;
    private final @NotNull CommandSender sender;
    private final @NotNull String        label;
    private final @NotNull String[]      subargs;
    private final @NotNull String        subcommand;
}
