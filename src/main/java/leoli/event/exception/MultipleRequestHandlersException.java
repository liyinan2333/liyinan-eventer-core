package leoli.event.exception;

/**
 * @author leoli
 * @date 2021/09/23
 */
public class MultipleRequestHandlersException extends RuntimeException {

    public MultipleRequestHandlersException() {
        super("There are multiple request handlers.");
    }

}
