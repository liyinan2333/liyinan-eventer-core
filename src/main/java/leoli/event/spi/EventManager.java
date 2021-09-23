package leoli.event.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import leoli.event.exception.HandlerUnregisteredException;
import leoli.event.exception.MultipleRequestHandlersException;
import leoli.event.util.JsonUtil;
import leoli.event.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Event Manager
 *
 * @author leoli
 * @date 2020/2/26
 */
public class EventManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);
    private static EventManager instance = new EventManager();

    private ExecutorService executor = ThreadUtil.newFixedThreadPool(2 << 2, 2 << 3,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));

    /**
     * One event,multiple handler.
     */
    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    private EventManager() {
    }

    public static EventManager get() {
        return instance;
    }

    /**
     * Publish event
     *
     * @param event
     */
    public void publish(Object event) {
        try {
            LOGGER.info("Received publish event: [{}]", JsonUtil.toJson(event));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Received publish event: [" + event.toString() + "], but an exception occurred during serialization.", e);
        }
        this.dispatch(event);
    }

    /**
     * Request event.
     *
     * @param event
     * @return
     */
    public Object request(Object event) {
        try {
            LOGGER.info("Received request event: [{}]", JsonUtil.toJson(event));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Received request event: [" + event.toString() + "], but an exception occurred during serialization.", e);
        }
        Object response = this.dispatchRequest(event);
        try {
            LOGGER.info("Received response event: [{}]", JsonUtil.toJson(response));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Received response event: [" + event.toString() + "], but an exception occurred during serialization.", e);
        }
        return response;
    }

    /**
     * Asynchronous request event.
     *
     * @param event
     */
    public void asyncRequest(Object event) {
        try {
            LOGGER.info("Received asynchronous request event: [{}]", JsonUtil.toJson(event));
        } catch (JsonProcessingException e) {
            LOGGER.warn("Received asynchronous request event: [" + event.toString() + "], but an exception occurred during serialization.", e);
        }
        this.dispatchAsyncRequest(event);
    }

    /**
     * Regist event handler.
     *
     * @param cls     Event class type.
     * @param handler Event handler.
     */
    protected void regist(Class<?> cls, EventHandler handler) {
        String key = cls.getName();
        if (handlers.containsKey(key)) {
            (handlers.get(key)).add(handler);
        } else {
            handlers.put(key, new ArrayList<>(Arrays.asList(handler)));
        }
        LOGGER.info("Event handler was registered: [event={}, handler={}]", key, handler.getClass().getName());
    }

    /**
     * Event routing and handler
     *
     * @param event
     */
    private void dispatch(Object event) {
        List<EventHandler> handlers = this.route(event);
        handlers.forEach(handler -> {
            if (handler.isAsync()) {
                // Asynchronous processing
                executor.submit(() -> {
                    handler.handle(event);
                });
            } else {
                handler.handle(event);
            }
        });
    }

    /**
     * Request event routing and handler
     *
     * @param event
     * @return
     */
    private Object dispatchRequest(Object event) {
        List<EventHandler> handlers = this.route(event);
        if (handlers.size() > 1) {
            throw new MultipleRequestHandlersException();
        }
        return handlers.get(0).handleRequest(event);
    }

    /**
     * Asynchronous request event routing and handler
     *
     * @param event
     * @return
     */
    private void dispatchAsyncRequest(Object event) {
        List<EventHandler> handlers = this.route(event);
        if (handlers.size() > 1) {
            throw new MultipleRequestHandlersException();
        }
        executor.submit(() -> {
            Object response = handlers.get(0).handleRequest(event);
            try {
                LOGGER.info("Received asynchronous response event: [{}]", JsonUtil.toJson(response));
            } catch (JsonProcessingException e) {
                LOGGER.warn("Received asynchronous response event: [" + response.toString() + "], but an exception occurred during serialization.", e);
            }
            this.dispatch(response);
        });
    }

    /**
     * Find handlers according to class type and router
     *
     * @param event
     * @return
     */
    private List<EventHandler> route(Object event) {
        List<EventHandler> classTypeHandlers = this.handlers.get(event.getClass().getName());
        if (classTypeHandlers == null || classTypeHandlers.size() == 0) {
            throw new HandlerUnregisteredException();
        }
        List<EventHandler> handlers = classTypeHandlers.stream().filter((handler) -> {
            // The router judges the match before processing.
            if (handler.getRouter().route(event)) {
                LOGGER.info("Event route: [event={}, handler={}]", event.getClass().getName(), handler.getClass().getName());
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (handlers.size() == 0) {
            throw new HandlerUnregisteredException();
        }
        return handlers;
    }
}
