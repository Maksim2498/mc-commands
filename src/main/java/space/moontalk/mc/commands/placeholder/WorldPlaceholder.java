package space.moontalk.mc.commands.placeholder;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.CommandCall;
import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.message.MessageProviderManagerHolder;
import space.moontalk.mc.commands.message.placeholder.WorldNotFoundMessageProvider;

@Getter
@AllArgsConstructor
public class WorldPlaceholder implements Placeholder<World>,
                                         MessageProviderManagerHolder {
    private final @NotNull MessageProviderManager messageProviderManager;

    @Override
    public @NotNull List<String> evalVariants(@NotNull CommandCall call) {
        return Bukkit.getWorlds().stream()
                                 .map(w -> w.getName())
                                 .collect(Collectors.toList());
    }

    @Override
    public @NotNull World variantToObject(@NotNull CommandCall call, @NotNull String variant) throws WorldNotFoundException {
        val world = Bukkit.getWorld(variant);

        if (world == null) {
            val messageProvider = messageProviderManager.getCompatibleMessageProvider(WorldNotFoundMessageProvider.class);
            val message         = messageProvider.makeWorldNotFoundMessage(variant);
            throw new WorldNotFoundException(variant, message);
        }

        return world;
    }
}
