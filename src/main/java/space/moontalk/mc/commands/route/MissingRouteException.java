package space.moontalk.mc.commands.route;

public class MissingRouteException extends RouteException {
    public MissingRouteException() {
        setReturnCode(false);
    }
}
