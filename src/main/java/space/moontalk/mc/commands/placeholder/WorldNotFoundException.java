package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.message.WorldNotFoundMessageProvider;

public class WorldNotFoundException extends PlaceholderException {
    public WorldNotFoundException(
        @NotNull WorldNotFoundMessageProvider messageProvider,
        @NotNull String                       worldName
    ) {
        super(messageProvider.makeWorldNotFoundMessage(worldName));
    }
}
