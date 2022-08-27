package space.moontalk.mc.commands.placeholder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractPlaceholder<T> implements Placeholder<T> {
    private final char shortName;
}
