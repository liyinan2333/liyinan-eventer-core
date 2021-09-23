package leoli.event.util;

import java.util.concurrent.*;

/**
 * @author leoli
 * @date 2021/09/21
 */
public class ThreadUtil {

    /**
     * Solve the memory overflow problem of {@link Executors}
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @return
     */
    public static ExecutorService newFixedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

}
