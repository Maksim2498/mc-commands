package space.moontalk.mc.commands.message;

import lombok.val;

import space.moontalk.mc.commands.message.placeholder.*;
import space.moontalk.mc.commands.message.route.*;

public class DefaultMessageProviderManager extends HashMessageProviderManager {
    public DefaultMessageProviderManager() {
        val gameModeNotFoundMessageProvider = new GameModeNotFoundMessageProvider() {};
        setMessageProvider(GameModeNotFoundMessageProvider.class, gameModeNotFoundMessageProvider);

        val playerNotFoundMessageProvider = new PlayerNotFoundMessageProvider() {};
        setMessageProvider(PlayerNotFoundMessageProvider.class, playerNotFoundMessageProvider);

        val worldNotFoundMessageProvider = new WorldNotFoundMessageProvider() {};
        setMessageProvider(WorldNotFoundMessageProvider.class, worldNotFoundMessageProvider);

        val missingPermissionMessageProvider = new MissingPermissionMessageProvider() {};
        setMessageProvider(MissingPermissionMessageProvider.class, missingPermissionMessageProvider);

        val invalidClassMessageProvider = new InvalidClassMessageProvider() {};
        setMessageProvider(InvalidClassMessageProvider.class, invalidClassMessageProvider);
    }
}
