package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.message.GameModeNotFoundMessageProvider;

public class GameModeNotFoundException extends PlaceholderException {
    public GameModeNotFoundException(
        @NotNull GameModeNotFoundMessageProvider messageProvider,
        @NotNull String                          worldName
    ) {
        super(messageProvider.makeGameModeNotFoundMessage(worldName));
    }
}
