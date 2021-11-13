package leoli.eventer.sample.handler;

import leoli.event.spi.EventHandler;
import leoli.eventer.sample.bean.SayEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handle {@link SayEvent}.
 *
 * @author LiYinan
 * @date 2020/2/26
 */
@Component
public class SayEventHandler extends EventHandler<SayEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SayEventHandler.class);

    @Override
    protected void handle(SayEvent event) {
        LOGGER.info("Someone said: {}", event.getMessage());
    }
}
