package leoli.eventer.sample.handler;

import leoli.event.anno.Async;
import leoli.event.spi.EventHandler;
import leoli.eventer.sample.bean.PingEvent;
import leoli.eventer.sample.bean.PongEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handle request event {@link PingEvent}.
 *
 * @author LiYinan
 * @date 2020/2/26
 */
@Async
@Component
public class AsyncPingResponseHandler extends EventHandler<PongEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncPingResponseHandler.class);

    @Override
    protected void handle(PongEvent event) {
        LOGGER.info("Handle async response......");
    }
}
