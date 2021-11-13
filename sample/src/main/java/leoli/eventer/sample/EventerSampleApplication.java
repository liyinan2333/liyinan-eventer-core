package leoli.eventer.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application Starter
 *
 * @author LiYinan
 * @date 2020/2/26
 */
@SpringBootApplication(scanBasePackages = {"leoli.eventer.sample"})
public class EventerSampleApplication {

    public static void main(String[] args) {

        SpringApplication.run(EventerSampleApplication.class, args);

    }

}