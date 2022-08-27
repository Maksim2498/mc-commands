package space.moontalk.mc.commands.placeholder;

import java.util.List;

import org.jetbrains.annotations.NotNull;

public interface Placeholder<T> {
    @NotNull List<@NotNull String> evalVariants();

    @NotNull T variantToObject(@NotNull String variant) throws Exception; 

    char getShortName();
}
