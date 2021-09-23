package leoli.event.spi;

import leoli.event.anno.Async;
import leoli.event.exception.MethodNotImplException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Abstract event processor, subscribe to and process a specified event.
 *
 * @author leoli
 * @date 2020/2/26
 */
public abstract class EventHandler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

    /**
     * If @Async annotation exists, it's processed asynchronously.
     */
    private boolean async = false;
    /**
     * Default match all.
     */
    private Router router = event -> {
        LOGGER.info("Default router matched.");
        return true;
    };

    public EventHandler() {
        // When initialized by the container, register the event type to be received with the eventmanager.
        EventManager.get().regist(this.getGenericClass(), this);
        // Determine whether to process asynchronously during container initialization.
        this.async = getClass().getAnnotation(Async.class) != null;
    }

    protected void regist(Router router) {
        this.router = router;
    }

    /**
     * Reflection get generic class.
     */
    private Class<?> getGenericClass() {
        Type type = getClass().getGenericSuperclass();
        return (Class<?>) (type instanceof Class ? type : ((ParameterizedType) type).getActualTypeArguments()[0]);
    }

    /**
     * Event handling method.
     *
     * @param event
     */
    protected void handle(T event) {
        throw new MethodNotImplException();
    }

    /**
     * Request event handling method.
     *
     * @param event
     * @return
     */
    protected Object handleRequest(T event) {
        throw new MethodNotImplException();
    }

    public Router getRouter() {
        return this.router;
    }

    public boolean isAsync() {
        return async;
    }
}
