package leoli.eventer.sample.rest;

import leoli.event.spi.EventManager;
import leoli.eventer.sample.bean.SayEvent;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Publish demo.
 *
 * @author leoli
 * @date 2020/2/26
 */
@RestController
@RequestMapping("publish")
public class PublishRest {

    @RequestMapping("{msg}")
    public String hello(@PathVariable String msg) {
        // Submit a Event to EventManager
        SayEvent event;
        if (RandomUtils.nextInt() % 3 == 0) {
            event = new SayEvent("leoli", new Date(), msg);
        } else {
            event = new SayEvent("xxx", new Date(), msg);
        }
        EventManager.get().publish(event);
        return msg;
    }

}
