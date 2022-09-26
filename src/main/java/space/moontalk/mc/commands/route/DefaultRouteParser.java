package space.moontalk.mc.commands.route;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import space.moontalk.mc.commands.message.MessageProviderManager;
import space.moontalk.mc.commands.placeholder.DefaultPlaceholderManager;
import space.moontalk.mc.commands.placeholder.PlaceholderManager;

/* Syntax:
 *
 *  - Nonterminals:
 *
 *    Route       -> [TopRoute ['|' Route]]
 *    TopRoute    -> MiddleRoute [TopRoute]
 *    MiddleRoute -> BottomRoute ['?' | Range]
 *    BottomRoute -> '(' TopRoute ')'
 *                 | Operand 
 *
 * - Terminals:
 *
 *   Operand     -> Any
 *                | Exact
 *                | Placeholder
 *   Any         -> '*'
 *   Exact       -> JavaIdentifier
 *   Placeholder -> '%' (a-z | A-Z) 
 *   Range       -> '{' Number [',' Number] '}'
 */

public class DefaultRouteParser implements RouteParser {
    @Getter
    private final @NotNull PlaceholderManager placeholderManager;

    private @NotNull String route;
    private          int    position;

    public DefaultRouteParser() {
        this(new DefaultPlaceholderManager());
    }

    public DefaultRouteParser(@NotNull MessageProviderManager messageProviderManager) {
        this(new DefaultPlaceholderManager(messageProviderManager));
    }

    public DefaultRouteParser(@NotNull PlaceholderManager placeholderManager) {
        this.placeholderManager = placeholderManager;
    }

    @Override
    public @NotNull RouteNode parseRoute(@NotNull String route) throws InvalidRouteException {
        resetRoute(route);

        if (isEmpty())
            return RouteNode.None.INSTANCE;
        
        val tree = getNextRoute();

        checkEnd();

        return tree;
    }

    private boolean isEmpty() throws InvalidRouteException {
        val token = getNextToken();
        val type  = token.getType();

        if (type == Token.Type.END)
            return true;

        retractToken(token);

        return false;
    }

    private void resetRoute(@NotNull String route) {
        this.route = route;
        position   = 0;
    }

    private @NotNull RouteNode getNextRoute() throws InvalidRouteException {
        var node  = getNextTopRoute();
        val token = getNextToken();
        val type  = token.getType();
        
        if (type == Token.Type.OR) {
            val nextNode = getNextRoute();
            val orNode   = new RouteNode.Or(node, nextNode);

            node = orNode;
        } else
            retractToken(token);

        return node;
    }

    private @NotNull RouteNode getNextTopRoute() throws InvalidRouteException {
        var node        = getNextMiddleRoute();
        val oldPosition = position;

        try {
            val nextNode   = getNextTopRoute();
            val concatNode = new RouteNode.Concat(node, nextNode);

            node = concatNode;
        } catch (InvalidRouteException exception) {
            position = oldPosition;
        }

        return node;
    }

    private @NotNull RouteNode getNextMiddleRoute() throws InvalidRouteException {
        val node  = getNextBottomRoute();
        val token = getNextToken();
        val type  = token.getType();
        
        return switch (type) {
            case OPTIONAL -> RouteNode.makeOptional(node);

            case RANGE -> {
                val range = (Token.Range) token;
                val from  = range.getFrom();
                val to    = range.getTo();

                yield RouteNode.makeRepeating(node, from, to);
            }

            default -> {
                retractToken(token); 
                yield node;
            }
        };
    }

    private @NotNull RouteNode getNextBottomRoute() throws InvalidRouteException {
        val token = getNextToken();
        val type  = token.getType();

        return switch (type) {
            case OPEN -> {
                val route = getNextRoute(); 
                checkNextTokenType(Token.Type.CLOSE);
                yield route;
            }
            
            case PLACEHOLDER -> {
                val placeholderToken = (Token.Placeholder) token;
                val name             = placeholderToken.getName();
                val placeholder      = placeholderToken.getPlaceholder();
                val placeholderNode  = new RouteNode.Placeholder(name, placeholder);

                yield placeholderNode;
            }

            case EXACT -> {
                val exactToken = (Token.Exact) token;
                val exact      = exactToken.getString();
                val exactNode  = new RouteNode.Exact(exact);

                yield exactNode;
            }

            case ANY -> RouteNode.Any.INSTANCE;

            default -> throw new InvalidRouteException(route, position - 1);
        };
    }

    private void checkEnd() throws InvalidRouteException {
        checkNextTokenType(Token.Type.END);
    }

    private void checkNextTokenType(@NotNull Token.Type type) throws InvalidRouteException {
        val token = getNextToken();

        if (token.getType() != type)
            throw new InvalidRouteException(route, position - 1);
    }

    private @NotNull Token getNextToken() throws InvalidRouteException {
        skipWhitespace();

        char c;

        try { 
            c = getNextChar();
        } catch (IndexOutOfBoundsException exception) {
            return Token.End.INSTANCE;
        }

        return switch (c) {
            case '(' -> Token.Open.INSTANCE;
            case ')' -> Token.Close.INSTANCE;
            case '|' -> Token.Or.INSTANCE;
            case '*' -> Token.Any.INSTANCE;
            case '?' -> Token.Optional.INSTANCE;
            case '%' -> getNextPlaceholderToken();
            case '{' -> {
                retractChar();
                yield getNextRangeToken();
            }
            default  -> {
                retractChar();
                yield getNextExactToken();
            }
        };
    }

    private void skipWhitespace() {
        try {
            while (Character.isWhitespace(getNextChar()));
            retractChar();
        } catch (IndexOutOfBoundsException exception) {}
    }

    private @NotNull Token.Range getNextRangeToken() throws InvalidRouteException {
        checkNextChar('{');

        int  from = getNextInt();
        char c    = getNextCharSafe();

        return switch (c) {
            case ',' -> {
                int to = getNextInt();
                checkNextChar('}');
                yield new Token.Range(from, to);
            }

            case '}' -> new Token.Range(from);

            default -> throw new InvalidRouteException(route, position - 1);
        };
    }

    private void checkNextChar(char tc) throws InvalidRouteException {
        val nc = getNextCharSafe();

        if (nc != tc)
            throw new InvalidRouteException(route, position - 1);
    }

    private int getNextInt() throws InvalidRouteException {
        var c = getNextCharSafe();

        if (!Character.isDigit(c))
            throw new InvalidRouteException(route, position - 1);

        int i = 0;

        try {
            do {
                i += c - '0';
                c = getNextChar();
            } while (Character.isDigit(c));

            retractChar();
        } catch (IndexOutOfBoundsException exception) {}

        return i;
    }

    private @NotNull Token.Placeholder getNextPlaceholderToken() throws InvalidRouteException {
        try {
            val name        = getNextCharSafe();
            val placeholder = placeholderManager.getPlaceholder(name);
            val token       = new Token.Placeholder(name, placeholder);

            return token;
        } catch (IllegalArgumentException exception) {
            throw new InvalidRouteException(route, position - 1);
        }
    }

    private @NotNull Token.Exact getNextExactToken() throws InvalidRouteException {
        char c = getNextCharSafe();

        if (!Character.isJavaIdentifierStart(c))
            throw new InvalidRouteException(route, position - 1);

        val builder = new StringBuilder().append(c);

        while (true) {
            try {
                c = getNextChar();
            } catch (IndexOutOfBoundsException exception) {
                break;
            }

            if (!Character.isJavaIdentifierPart(c)) 
                break;

            builder.append(c);
        }

        retractChar();

        val string = builder.toString();
        val token  = new Token.Exact(string);

        return token;
    }

    private void retractToken(@NotNull Token token) {
        position -= token.getLength();
    }

    private char getNextCharSafe() throws InvalidRouteException {
        try {
            return getNextChar();
        } catch (IndexOutOfBoundsException exception) {
            throw new InvalidRouteException(route, position - 1);
        }
    }

    private char getNextChar() {
        return route.charAt(position++);
    }

    private void retractChar() {
        --position;
    }

    @Getter
    @AllArgsConstructor
    private static abstract sealed class Token permits Token.Open,
                                                       Token.Close,
                                                       Token.Optional,
                                                       Token.Or,
                                                       Token.Placeholder,
                                                       Token.Exact,
                                                       Token.Any,
                                                       Token.End,
                                                       Token.Range {
        private final @NotNull Type type;

        enum Type {
            OPEN,
            CLOSE,
            OPTIONAL,
            OR,
            PLACEHOLDER,
            EXACT,
            ANY,
            END,
            RANGE
        }

        int getLength() {
            return 1;
        }

        static final class Open extends Token {
            static final Open INSTANCE = new Open();

            private Open() {
                super(Type.OPEN);
            }
        }

        static final class Close extends Token {
            static final Close INSTANCE = new Close();

            private Close() {
                super(Type.CLOSE);
            }
        }

        static final class Optional extends Token {
            static final Optional INSTANCE = new Optional();

            private Optional() {
                super(Type.OPTIONAL);
            }
        }

        static final class Or extends Token {
            static final Or INSTANCE = new Or();

            private Or() {
                super(Type.OR);
            }
        }

        @Getter
        static final class Placeholder extends Token {
            private final          char                                                  name;
            private final @NotNull space.moontalk.mc.commands.placeholder.Placeholder<?> placeholder;

            Placeholder(char name, @NotNull space.moontalk.mc.commands.placeholder.Placeholder<?> placeholder) {
                super(Type.PLACEHOLDER);
                this.name        = name;
                this.placeholder = placeholder;
            }

            @Override
            int getLength() {
                return 2;
            }
        }

        @Getter
        static final class Exact extends Token {
            private final @NotNull String string;

            Exact(@NotNull String string) {
                super(Type.EXACT);
                this.string = string;
            }

            @Override
            int getLength() {
                return string.length();
            }
        }

        static final class Any extends Token {
            static final Any INSTANCE = new Any();

            private Any() {
                super(Type.ANY);
            }
        }

        static final class End extends Token {
            static final End INSTANCE = new End();

            private End() {
                super(Type.END);
            }

            @Override
            int getLength() {
                return 0;
            }
        }

        @Getter
        static final class Range extends Token {
            private final int from;
            private final int to;

            Range(int times) {
                this(times, times);
            }

            Range(int from, int to) {
                super(Type.RANGE);

                this.from = from;
                this.to   = to;
            }

            @Override
            int getLength() {
                return String.format("{%d,%d}", from, to).length();
            }
        }
    }
}
