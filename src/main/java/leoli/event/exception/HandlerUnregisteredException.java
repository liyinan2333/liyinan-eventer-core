package leoli.event.exception;

/**
 * @author leoli
 * @date 2021/09/23
 */
public class HandlerUnregisteredException extends RuntimeException {

    public HandlerUnregisteredException() {
        super("Event can not route, because the handler is not registered!");
    }

}
