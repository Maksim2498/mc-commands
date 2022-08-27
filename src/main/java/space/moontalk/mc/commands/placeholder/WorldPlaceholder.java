package space.moontalk.mc.commands.placeholder;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.message.WorldNotFoundMessageProvider;

@Getter
public class WorldPlaceholder extends AbstractPlaceholder<World> {
    private final @NotNull WorldNotFoundMessageProvider messageProvider;

    public WorldPlaceholder(@NotNull WorldNotFoundMessageProvider messageProvider) {
        super('w');
        this.messageProvider = messageProvider;
    }

    @Override
    public @NotNull List<String> evalVariants() {
        return Bukkit.getWorlds().stream().map(w -> w.getName()).collect(Collectors.toList());
    }

    @Override
    public @NotNull World variantToObject(@NotNull String variant) throws WorldNotFoundException {
        val world = Bukkit.getWorld(variant);

        if (world == null) 
            throw new WorldNotFoundException(messageProvider, variant);

        return world;
    }
}
