package leoli.eventer.sample.handler;

import leoli.event.anno.Async;
import leoli.event.spi.EventHandler;
import leoli.event.spi.Router;
import leoli.eventer.sample.bean.SayEvent;
import leoli.eventer.sample.router.SayEventNameRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Handle {@link SayEvent} from LeoLi.
 *
 * @author LiYinan
 * @date 2020/2/26
 */
@Async
@Component
public class SayEventLeoliHandler extends EventHandler<SayEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SayEventLeoliHandler.class);

    @Autowired
    SayEventNameRouter router;

    @Override
    protected Router registRouter() {
        return router;
    }

    @Override
    protected void handle(SayEvent event) {
        LOGGER.info("Amazing！LeoLi spoke!!");
    }

}
