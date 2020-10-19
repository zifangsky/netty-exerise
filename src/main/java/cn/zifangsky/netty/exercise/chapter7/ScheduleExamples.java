package cn.zifangsky.netty.exercise.chapter7;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务调度示例
 *
 * @author zifangsky
 * @date 2020/10/19
 * @since 1.0.0
 */
public class ScheduleExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Scheduling a task with a ScheduledExecutorService
     */
    public static void schedule() {
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("thread-test-" + count.getAndIncrement());

                return thread;
            }
        });

        executor.schedule(()-> System.out.println("Now it is 60 seconds later"),
                60, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * Scheduling a task with EventLoop
     */
    public static void scheduleViaEventLoop() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        channel.eventLoop().schedule(()-> System.out.println("Now it is 60 seconds later"),
                60, TimeUnit.SECONDS);
    }

    /**
     * Scheduling a recurring task with EventLoop
     */
    public static void scheduleFixedViaEventLoop() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        channel.eventLoop().scheduleAtFixedRate(()-> System.out.println("Run every 60 seconds"),
                60, 60, TimeUnit.SECONDS);
    }

    /**
     * Canceling a task using ScheduledFuture
     */
    public static void cancelingTaskUsingScheduledFuture() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;

        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(() -> System.out.println("Run every 60 seconds"),
                60, 60, TimeUnit.SECONDS);

        // Some other code that runs...
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning);
    }

}
