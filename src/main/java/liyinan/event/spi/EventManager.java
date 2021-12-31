package liyinan.event.spi;

import com.fasterxml.jackson.core.JsonProcessingException;
import liyinan.event.exception.HandlerUnregisteredException;
import liyinan.event.exception.MultipleRequestHandlersException;
import liyinan.event.util.JsonUtil;
import liyinan.event.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Event Manager.
 *
 * @author LiYinan
 * @date 2020/2/26
 */
public class EventManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventManager.class);
    private static EventManager INSTANCE = new EventManager();

    private ExecutorService executor = ThreadUtil.newFixedThreadPool(2 << 2, 2 << 3,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));

    /**
     * One event,multiple handler.
     */
    private Map<String, List<EventHandler>> handlers = new HashMap<>();

    private EventManager() {
    }

    public static EventManager get() {
        return INSTANCE;
    }

    /**
     * Publish event.
     *
     * @param event
     */
    public void publish(Object event) {
        this.dispatch(event);
    }

    /**
     * Request event.
     *
     * @param event
     * @return
     */
    public Object request(Object event) {
        Object response = this.dispatchRequest(event);
        return response;
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
     * Event routing and handler.
     *
     * @param event
     */
    private void dispatch(Object event) {
        this.log("Received publish event: [{}]", event);
        List<EventHandler> handlers = this.route(event);
        handlers.forEach(handler -> {
            if (handler.isAsync()) {
                // Asynchronous processing.
                executor.submit(() -> {
                    handler.handle(event);
                });
            } else {
                // Synchronous processing.
                handler.handle(event);
            }
        });
    }

    /**
     * Asynchronous response routing and handler.
     *
     * @param response
     */
    private void dispatchAsyncResponse(Object response) {
        this.log("Received Asynchronous response event: [{}]", response);
        List<EventHandler> handlers = this.route(response);
        handlers.forEach(handler -> {
            if (handler.isAsync()) {
                // Asynchronous processing.
                executor.submit(() -> {
                    handler.handle(response);
                });
            } else {
                // Synchronous processing.
                handler.handle(response);
            }
        });
    }

    /**
     * Request event routing and handler.
     *
     * @param event
     * @return
     */
    private Object dispatchRequest(Object event) {
        this.log("Received request event: [{}]", event);
        List<EventHandler> handlers = this.route(event);
        if (handlers.size() > 1) {
            throw new MultipleRequestHandlersException();
        }
        // Asynchronous handler.
        EventHandler handler = handlers.get(0);
        if (handler.isAsync()) {
            executor.submit(() -> {
                Object response = handler.handleRequest(event);
                this.dispatchAsyncResponse(response);
            });
            return null;
        } else {
            // Synchronous handler.
            Object response = handler.handleRequest(event);
            this.log("Received response event: [{}]", response);
            return response;
        }
    }

    /**
     * Find handlers according to class type and router.
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
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Event route: [event={}, handler={}]", event.getClass().getName(), handler.getClass().getName());
                }
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        if (handlers.size() == 0) {
            throw new HandlerUnregisteredException();
        }
        return handlers;
    }

    private void log(String msg, Object event) {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(msg, JsonUtil.toJson(event));
            }
        } catch (JsonProcessingException e) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(msg + ", but an exception occurred during serialization.", event.toString());
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }
}
