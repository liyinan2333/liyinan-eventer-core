package leoli.eventer.sample.router;

import leoli.event.spi.Router;
import leoli.eventer.sample.bean.SayEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Route {@link SayEvent} from LeoLi.
 */
@Component
public class SayEventNameRouter implements Router<SayEvent> {

    private static Logger LOGGER =  LoggerFactory.getLogger(SayEventNameRouter.class);

    @Override
    public boolean route(SayEvent event) {
        if ("leoli".equals(event.getName())) {
            LOGGER.info("Messages matching to LeoLi");
            return true;
        }
        return false;
    }
}
