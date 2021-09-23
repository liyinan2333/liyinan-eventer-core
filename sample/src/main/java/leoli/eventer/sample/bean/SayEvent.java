package leoli.eventer.sample.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author leoli
 * @date 2020/2/26
 */
@Data
@AllArgsConstructor
public class SayEvent {

    private String name;
    private Date time;
    private String message;

}
