package leoli.eventer.sample.rest;

import leoli.event.spi.EventManager;
import leoli.eventer.sample.bean.PingEvent;
import leoli.eventer.sample.bean.PongEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Request demo.
 *
 * @author LiYinan
 * @date 2020/9/23
 */
@RestController
@RequestMapping("request")
public class RequestRest {

    @RequestMapping("{msg}")
    public String request(@PathVariable String msg) {
        // Submit a request event to EventManager
        Object response = EventManager.get().request(new PingEvent(msg));
        String context = ((PongEvent) response).getContext();
        return context;
    }

    @RequestMapping("async/{msg}")
    public String asyncRequest(@PathVariable String msg) {
        // Submit a request event to EventManager
        EventManager.get().asyncRequest(new PingEvent(msg));
        return "Request sent.";
    }

}
