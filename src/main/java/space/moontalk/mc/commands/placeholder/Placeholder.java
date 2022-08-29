package space.moontalk.mc.commands.placeholder;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import space.moontalk.mc.commands.CommandCall;

public interface Placeholder<T> {
    @NotNull List<@NotNull String> evalVariants(@NotNull CommandCall call);

    @NotNull T variantToObject(@NotNull CommandCall call, @NotNull String variant) throws Exception; 

    char getShortName();
}
