package space.moontalk.mc.commands;

import org.jetbrains.annotations.NotNull;

import space.moontalk.ranges.IntegerRange;

public interface ErrorMessageProvider {
    @NotNull String makeInvalidArgsNumMessage(@NotNull IntegerRange required, int provided);
    @NotNull String makeInvalidSubcommandMessage(@NotNull String subcommand);
    @NotNull String makeMissingPermissionMessage(@NotNull String permission);
    @NotNull String makeMissingSubcommandMessage();
}
