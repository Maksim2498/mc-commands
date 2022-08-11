package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import space.moontalk.ranges.IntegerRange;

@Getter
@Setter
@AllArgsConstructor
public class InvalidArgsNumException extends CommandException {
    private final @NotNull IntegerRange required;
    private final int                   provided;
}
