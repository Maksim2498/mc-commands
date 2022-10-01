package space.moontalk.mc.commands.message;

import lombok.val;

import space.moontalk.mc.commands.message.placeholder.*;
import space.moontalk.mc.commands.message.route.*;

public class DefaultMessageProviderManager extends HashMessageProviderManager {
    public DefaultMessageProviderManager() {
        val gameModeNotFound = new GameModeNotFoundMessageProvider() {};
        setMessageProvider(GameModeNotFoundMessageProvider.class, gameModeNotFound);

        val offlinePlayerNotFound = new OfflinePlayerNotFoundMessageProvider() {};
        setMessageProvider(OfflinePlayerNotFoundMessageProvider.class, offlinePlayerNotFound);

        val playerNotFound = new PlayerNotFoundMessageProvider() {};
        setMessageProvider(PlayerNotFoundMessageProvider.class, playerNotFound);

        val worldNotFound = new WorldNotFoundMessageProvider() {};
        setMessageProvider(WorldNotFoundMessageProvider.class, worldNotFound);

        val missingPermission = new MissingPermissionMessageProvider() {};
        setMessageProvider(MissingPermissionMessageProvider.class, missingPermission);

        val invalidClass = new InvalidClassMessageProvider() {};
        setMessageProvider(InvalidClassMessageProvider.class, invalidClass);
    }
}
