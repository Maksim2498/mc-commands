package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

public abstract sealed class PatternNode permits PatternNode.Nullary, PatternNode.Binary {
    public static enum Type {
        // Nullary:
        
        NONE,
        ANY,
        EXACT,
        PLAYER,

        // Binary:

        OR,
        CONCAT,
    }
    
    public static enum Cardinality {
        NULLARY,
        BINARY
    }

    public static abstract sealed class Nullary extends PatternNode permits None, Any, Exact, Player {
        @Override
        public @NotNull Cardinality getCardinaliry() {
            return Cardinality.NULLARY;
        }
    }

    @Getter
    @AllArgsConstructor
    public static abstract sealed class Binary extends PatternNode permits Or, Concat {
        private final @NotNull PatternNode leftChild;
        private final @NotNull PatternNode rightChild;

        @Override
        public @NotNull Cardinality getCardinaliry() {
            return Cardinality.BINARY;
        }

        @Override
        @NotNull String toString(int offset) {
            val superString      = super.toString(offset);
            val childrenOffset   = offset + 4;
            val leftChildString  = leftChild.toString(childrenOffset);
            val rightChildString = rightChild.toString(childrenOffset);
            val result           = String.format("%s:\n%s\n%s", superString, leftChildString, rightChildString);

            return result;
        }
    }

    public static final class None extends Nullary {
        public static final None INSTANCE = new None();

        private None() {}

        @Override
        @NotNull public Type getType() {
            return Type.NONE;
        }
    }

    public static final class Any extends Nullary {
        public static final Any INSTANCE = new Any();

        private Any() {}

        @Override
        @NotNull public Type getType() {
            return Type.ANY;
        }
    }

    @Getter
    @AllArgsConstructor
    public static final class Exact extends Nullary {
        private final @NotNull String string;

        @Override
        @NotNull public Type getType() {
            return Type.EXACT;
        }

        @Override
        @NotNull String toString(int offset) {
            return String.format("%s(%s)", super.toString(offset), string);
        }
    }

    public static final class Player extends Nullary {
        public static final Player INSTANCE = new Player();

        private Player() {}

        @Override
        @NotNull public Type getType() {
            return Type.PLAYER;
        }
    }

    public static final class Or extends Binary {
        public Or(@NotNull PatternNode leftChild, @NotNull PatternNode rightChild) {
            super(leftChild, rightChild);
        }
        
        @Override
        @NotNull public Type getType() {
            return Type.OR;
        }
    }

    public static final class Concat extends Binary {
        public Concat(@NotNull PatternNode leftChild, @NotNull PatternNode rightChild) {
            super(leftChild, rightChild);
        }
        
        @Override
        @NotNull public Type getType() {
            return Type.CONCAT;
        }
    }

    public abstract @NotNull Type getType();
    public abstract @NotNull Cardinality getCardinaliry();

    @Override
    public @NotNull String toString() {
        return toString(0);
    }

    @NotNull String toString(int offset) {
        return makeOffset(offset) + getType().name();
    }

    static @NotNull String makeOffset(int offset) {
        val builder = new StringBuilder();

        while (offset-- > 0)
            builder.append(' ');

        return builder.toString();
    }
}
