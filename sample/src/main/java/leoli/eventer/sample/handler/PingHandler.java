package leoli.eventer.sample.handler;

import leoli.event.spi.EventHandler;
import leoli.eventer.sample.bean.PingEvent;
import leoli.eventer.sample.bean.PongEvent;
import org.springframework.stereotype.Component;

/**
 * Handle request event {@link PingEvent}.
 *
 * @author LiYinan
 * @date 2020/2/26
 */
@Component
public class PingHandler extends EventHandler<PingEvent> {

    @Override
    protected Object handleRequest(PingEvent event) {
        String respContext = event.getContext().startsWith("Ping") ? "Pong." : "What?";
        return new PongEvent(respContext);
    }
}
