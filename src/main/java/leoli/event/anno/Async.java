package leoli.event.anno;

import java.lang.annotation.*;

/**
 * @date 2021/09/17
 * @author leoli
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
}
