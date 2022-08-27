package space.moontalk.mc.commands.placeholder;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.message.PlayerNotFoundMessageProvider;

public class PlayerNotFoundException extends PlaceholderException {
    public PlayerNotFoundException(
        @NotNull PlayerNotFoundMessageProvider messageProvider,
        @NotNull String                        playerName
    ) {
        super(messageProvider.makePlayerNotFoundMessage(playerName)); 
    }
}
