package liyinan.event.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide the default Router, directly return true.
 *
 * @author LiYinan
 * @date 2021/11/14
 */
public class DefaultRouter implements Router {

    private static final DefaultRouter INSTANCE = new DefaultRouter();
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRouter.class);

    private DefaultRouter() {
    }

    public static Router get() {
        return INSTANCE;
    }

    /**
     * Default match all.
     */
    @Override
    public boolean route(Object event) {
        LOGGER.info("Default router matched.");
        return true;
    }
}
