package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvalidSubcommandException extends CommandException {
    private @NotNull String subcommand;
}
