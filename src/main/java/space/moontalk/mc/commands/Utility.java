package space.moontalk.mc.commands;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import lombok.experimental.UtilityClass;

@UtilityClass
class Utility {
    @NotNull String extractSubcommand(@NotNull String[] args) throws MissingSubcommandException {
        if (args.length == 0)
            throw new MissingSubcommandException();

        return args[0].toLowerCase();
    }

    @NotNull String[] extractSubargs(@NotNull String[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
