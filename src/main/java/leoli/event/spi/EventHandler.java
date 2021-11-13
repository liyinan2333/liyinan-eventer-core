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
 * @author LiYinan
 * @date 2020/2/26
 */
public abstract class EventHandler<T> {

    /**
     * If @Async annotation exists, it's processed asynchronously.
     */
    private boolean async;
    private Router router;

    public EventHandler() {
        // When the object is initialized, register the event type to be received with the event manager.
        EventManager.get().regist(this.getGenericClass(), this);
        // Determine whether to process asynchronously during object initialization.
        this.async = getClass().getAnnotation(Async.class) != null;
        // Regist default router during object initialization.
        this.router = registRouter();
    }

    /**
     * Default match all.
     */
    protected Router registRouter() {
        return DefaultRouter.get();
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
