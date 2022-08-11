package space.moontalk.mc.commands;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import space.moontalk.ranges.IntegerRange;

@Getter
@Builder
public class SubcommandExecutorMeta {
    private final @NotNull IntegerRange argsRange; 

    @Singular
    private final @NotNull List<@NotNull String> requiredPermissions;
}
