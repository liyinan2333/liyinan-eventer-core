package liyinan.event.spi;

public interface Router<T> {

    boolean route(T event);

}
