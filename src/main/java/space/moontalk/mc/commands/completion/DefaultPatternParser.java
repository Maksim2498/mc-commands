package space.moontalk.mc.commands.completion;

import org.jetbrains.annotations.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

/* Syntax:
 *
 * - Nonterminals:
 *
 *   Pattern         -> TopPattern ['|' Pattern]
 *   TopPattern      -> BottomPattern [TopPattern]
 *   BottomPattern   -> '(' Pattern ')'
 *                    | Operand 
 *
 * - Terminals:
 *
 *   Operand      -> Escape | Exact
 *   Escape       -> Player | Any | EscapedExact
 *   Player       -> "%p"
 *   Any          -> "%s"
 *   EscapedExact -> '%' AnyCharacter
 *   Exact        -> JavaIdentifier
 */

public class DefaultPatternParser implements PatternParser {
    private @NotNull String pattern;
    private int             position;

    @Override
    public @NotNull PatternNode parsePattern(@NotNull String pattern) throws InvalidPatternException {
        this.pattern = pattern;
        position     = 0;

        val ast = getNextPattern();

        if (getNextToken().getType() != Token.Type.END)
            throw new InvalidPatternException(pattern, position - 1);

        return ast;
    }

    private @NotNull PatternNode getNextPattern() throws InvalidPatternException {
        var node  = getNextTopPattern();
        val token = getNextToken();
        val type  = token.getType();
        
        if (type == Token.Type.OR) {
            val nextNode = getNextPattern();
            val orNode   = new PatternNode.Or(node, nextNode);

            node = orNode;
        } else
            retractToken(token);

        return node;
    }

    private @NotNull PatternNode getNextTopPattern() throws InvalidPatternException {
        var node        = getNextBottomPattern();
        val oldPosition = position;

        try {
            val nextNode   = getNextTopPattern();
            val concatNode = new PatternNode.Concat(node, nextNode);

            node = concatNode;
        } catch (InvalidPatternException exception) {
            position = oldPosition;
        }

        return node;
    }

    private @NotNull PatternNode getNextBottomPattern() throws InvalidPatternException {
        val token = getNextToken();
        val type  = token.getType();

        return switch (type) {
            case OPEN -> {
                val pattern = getNextPattern();
                checkNextTokenType(Token.Type.CLOSE);
                yield pattern;
            }

            case PLAYER -> PatternNode.Player.INSTANCE;

            case EXACT -> {
                val exactToken = (Token.Exact) token;
                val string     = exactToken.getString();
                val node       = new PatternNode.Exact(string);

                yield node;
            }

            case ANY -> PatternNode.Any.INSTANCE;

            default -> throw new InvalidPatternException(pattern, position - 1);
        };
    }

    private void checkNextTokenType(@NotNull Token.Type type) throws InvalidPatternException {
        val token = getNextToken();

        if (token.getType() != type)
            throw new InvalidPatternException(pattern, position - 1);
    }

    private @NotNull void retractToken(@NotNull Token token) {
        position -= token.getLength();
    }

    private @NotNull Token getNextToken() throws InvalidPatternException {
        skipWhitespace();

        try {
            return switch (getNextChar()) {
                case '(' -> Token.Open.INSTANCE;
                case ')' -> Token.Close.INSTANCE;
                case '|' -> Token.Or.INSTANCE;
                case '%' -> getNextEscapeToken(); 
                default  -> {
                    retractChar();
                    yield getNextWordToken();
                }
            };
        } catch (IndexOutOfBoundsException exception) {
            return Token.End.INSTANCE;
        }
    }

    private @NotNull Token getNextEscapeToken() throws InvalidPatternException {
        try {
            val c = getNextChar();

            return switch (c) {
                case 's' -> Token.Any.INSTANCE;
                case 'p' -> Token.Player.INSTANCE;
                default  -> Token.Exact.fromEscape(c);
            };
        } catch (IndexOutOfBoundsException exception) {
            return Token.Exact.fromWord("%"); 
        }
    }

    private void skipWhitespace() {
        try {
            while (Character.isWhitespace(getNextChar()));
        } catch (Exception exception) {}

        retractChar();
    }

    private @NotNull Token.Exact getNextWordToken() throws InvalidPatternException {
        char c = getNextChar();

        if (!Character.isJavaIdentifierStart(c))
            throw new InvalidPatternException(pattern, position - 1);

        val builder = new StringBuilder().append(c);

        while (true) {
            try {
                c = getNextChar();
            } catch (Exception exception) {
                break;
            }

            if (!Character.isJavaIdentifierPart(c)) 
                break;

            builder.append(c);
        }

        retractChar();

        val string = builder.toString();
        val token  = Token.Exact.fromWord(string);

        return token;
    }

    private void retractChar() {
        --position;
    }

    private char getNextChar() {
        return pattern.charAt(position++);
    }

    private static abstract sealed class   Token
                                   permits Token.Open,
                                           Token.Close,
                                           Token.Or,
                                           Token.Player, 
                                           Token.Exact, 
                                           Token.Any,
                                           Token.End {
        enum Type {
            // Operators:

            OPEN,
            CLOSE,
            OR,

            // Operands:

            PLAYER,
            EXACT,
            ANY,

            // Special:
            
            END;
        }

        static final class Open extends Token {
            static final Open INSTANCE = new Open();

            private Open() {}

            @Override
            @NotNull Type getType() {
                return Type.OPEN;
            }

            @Override
            int getLength() {
                return 1;
            }
        }

        static final class Close extends Token {
            static final Close INSTANCE = new Close();

            private Close() {}

            @Override
            @NotNull Type getType() {
                return Type.CLOSE;
            }

            @Override
            int getLength() {
                return 1;
            }
        }

        static final class Or extends Token {
            static final Or INSTANCE = new Or();

            private Or() {}

            @Override
            @NotNull Type getType() {
                return Type.OR;
            }

            @Override
            int getLength() {
                return 1;
            }
        }

        static final class Player extends Token {
            static final Player INSTANCE = new Player();

            private Player() {}

            @Override
            @NotNull Type getType() {
                return Type.PLAYER;
            }

            @Override
            int getLength() {
                return 2;
            }
        }

        @Getter
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        static final class Exact extends Token {
            private final @NotNull String string;
            private final boolean         escape;

            static @NotNull Exact fromWord(@NotNull String word) {
                return new Exact(word, false);
            }

            static @NotNull Exact fromEscape(char c) {
                return new Exact(Character.toString(c), true);
            }

            @Override
            @NotNull Type getType() {
                return Type.EXACT;
            }

            @Override
            int getLength() {
                return (escape ? 1 : 0) + string.length();
            }
        }

        static final class Any extends Token {
            static final Any INSTANCE = new Any();

            private Any() {}

            @Override
            @NotNull Type getType() {
                return Type.ANY;
            }

            @Override
            int getLength() {
                return 2;
            }
        }

        static final class End extends Token {
            static final End INSTANCE = new End();

            private End() {}

            @Override
            @NotNull Type getType() {
                return Type.END;
            }

            @Override
            int getLength() {
                return 0;
            }
        }

        abstract @NotNull Type getType();
        abstract int getLength();
    }
}
