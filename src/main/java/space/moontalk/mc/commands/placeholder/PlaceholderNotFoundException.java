package space.moontalk.mc.commands.placeholder;

public class PlaceholderNotFoundException extends PlaceholderException {
    public PlaceholderNotFoundException(char shortName) {
        super(String.format("Placeholder \"%%%c\" not found", shortName));
    }
}
