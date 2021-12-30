package liyinan.event.exception;

/**
 * @author LiYinan
 * @date 2021/09/23
 */
public class MethodNotImplException extends RuntimeException {

    public MethodNotImplException() {
        super("Please override this method in a subclass.");
    }

}
