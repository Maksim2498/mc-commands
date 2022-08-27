package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract sealed class RouteNode permits RouteNode.Nullary,
                                               RouteNode.Binary {
    private final @NotNull Type        type;
    private final @NotNull Cardinality cardinality;

    public enum Type {
        OR,
        CONCAT,

        NONE,
        ANY,
        EXACT,
        PLACEHOLDER
    }

    public enum Cardinality {
        NULLARY,
        BINARY
    }

    public static @NotNull RouteNode makeRepeating(@NotNull RouteNode node, int times) {
        return makeRepeating(node, times, times);
    }

    public static @NotNull RouteNode makeRepeating(@NotNull RouteNode node, int from, int to) {
        if (from > to)
            throw new IllegalArgumentException("<from> cannot be greater than <to>");

        RouteNode result = node;

        for (int i = 0; i < from; ++i) 
            result = new Concat(result, node);

        int optinalTimes = to - from;
        val optinal      = RouteNode.makeOptional(result);

        for (int i = 0; i < optinalTimes; ++i)
            result = new RouteNode.Concat(result, optinal);

        return result;
    }

    public static @NotNull RouteNode makeOptional(@NotNull RouteNode node) {
        return new Or(node, None.INSTANCE);
    }

    public static abstract sealed class Nullary extends RouteNode 
                                                permits None,
                                                        Any,
                                                        Exact,
                                                        Placeholder {
        protected Nullary(@NotNull Type type) {
            super(type, Cardinality.NULLARY); 
        }
    }
    
    @Getter
    public static abstract sealed class Binary extends RouteNode
                                               permits Or,
                                                       Concat {
        private final @NotNull RouteNode leftChild;
        private final @NotNull RouteNode rightChild;

        protected Binary(
            @NotNull Type type,
            @NotNull RouteNode leftChild,
            @NotNull RouteNode rightChild
        ) {
            super(type, Cardinality.BINARY); 

            this.leftChild  = leftChild;
            this.rightChild = rightChild;
        }

        @Override
        @NotNull String toString(@NotNull String prefix) {
            val superString      = super.toString(prefix);
            val childrenPrefix   = prefix + "    ";
            val leftChildString  = leftChild.toString(childrenPrefix);
            val rightChildString = rightChild.toString(childrenPrefix);
            val result           = String.format("%s:\n%s\n%s", superString, leftChildString, rightChildString);

            return result;
        }
    }

    public static final class None extends Nullary {
        public static final None INSTANCE = new None();

        private None() {
            super(Type.NONE);
        }
    }

    public static final class Any extends Nullary {
        public static final Any INSTANCE = new Any();

        private Any() {
            super(Type.ANY);
        }
    }

    @Getter
    public static final class Exact extends Nullary {
        private final @NotNull String string;

        public Exact(@NotNull String string) {
            super(Type.EXACT);
            this.string = string;
        }

        @Override
        @NotNull String toString(@NotNull String prefix) {
            return String.format("%s(%s)", super.toString(prefix), string);
        }
    }

    @Getter
    public static final class Placeholder extends Nullary {
        private final @NotNull space.moontalk.mc.commands.placeholder.Placeholder<?> placeholder;

        public Placeholder(@NotNull space.moontalk.mc.commands.placeholder.Placeholder<?> placeholder) {
            super(Type.PLACEHOLDER);
            this.placeholder = placeholder;
        }

        @Override
        @NotNull String toString(@NotNull String prefix) {
            return String.format("%s(%%%c)", super.toString(prefix), placeholder.getShortName());
        }
    }

    public static final class Or extends Binary {
        public Or(@NotNull RouteNode leftChild, @NotNull RouteNode rightChild) {
            super(Type.OR, leftChild, rightChild);
        }
    }

    public static final class Concat extends Binary {
        public Concat(@NotNull RouteNode leftChild, @NotNull RouteNode rightChild) {
            super(Type.CONCAT, leftChild, rightChild);
        }
    }

    public @NotNull String toString() {
        return toString("");
    }

    @NotNull String toString(@NotNull String prefix) {
        return prefix + type.name();
    }
}
