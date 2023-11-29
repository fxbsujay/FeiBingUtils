package com.susu.utils;

import java.util.concurrent.*;

/**
 * <p>Description: Threading tool</p>
 * <p>线程工具</p>
 * @author sujay
 * @version 10:17 2023/11/29
 * @since JDK1.8 <br/>
 */
public class ThreadUtils {

    private static ExecutorService executor;

    /**
     * 执行一个任务
     */
    public static void execute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    /**
     * 提交一个任务
     */
    public static <T> Future<T> execAsync(Callable<T> task) {
        return getExecutor().submit(task);
    }

    public static Future<?> execAsync(Runnable runnable) {
        return getExecutor().submit(runnable);
    }

    /**
     * 创建线程池
     *
     * @param corePoolSize  核心线程数
     * @param maxPoolSize   最大线程数
     * @param queue         堵塞队列
     */
    public static ThreadPoolExecutor createExecutor(int corePoolSize, int maxPoolSize, BlockingQueue<Runnable> queue) {
        if (null == queue) {
            queue = corePoolSize <= 0 ? new SynchronousQueue<>() : new LinkedBlockingQueue<>(1024);
        }

        return new ThreadPoolExecutor(
                corePoolSize,                           // 核心线程数
                maxPoolSize,                            // 最大线程数
                TimeUnit.SECONDS.toNanos(60L),  // 任务超时释放
                TimeUnit.NANOSECONDS,                   // 超时时间单位
                queue,                                  // 堵塞队列
                Executors.defaultThreadFactory(),       // 线程工厂
                new ThreadPoolExecutor.AbortPolicy()    // 拒绝策略
        );
    }

    public static ExecutorService getExecutor() {
        if (executor == null) {
            int processors = Runtime.getRuntime().availableProcessors();
            executor = createExecutor(processors, processors * 4, null);
        }
        return executor;
    }

    public static Runnable execAsync(Runnable runnable, boolean isDaemon) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(isDaemon);
        thread.start();
        return runnable;
    }

    public static Thread newThread(Runnable runnable, String name) {
        Thread t = newThread(runnable, name, false);
        if (t.getPriority() != 5) {
            t.setPriority(5);
        }
        return t;
    }

    public static Thread newThread(Runnable runnable, String name, boolean isDaemon) {
        Thread t = new Thread(null, runnable, name);
        t.setDaemon(isDaemon);
        return t;
    }

    /**
     * 结束线程，调用此方法后，线程将抛出 {@link InterruptedException} 异常
     *
     * @param thread    需要结束的线程
     * @param isJoin    是否等待这个线程结束
     */
    public static void interrupt(Thread thread, boolean isJoin) {
        if (null != thread && !thread.isInterrupted()) {
            thread.interrupt();
            if (isJoin) {
                waitForDie(thread);
            }
        }
    }

    /**
     *  当前线程休眠
     */
    public static boolean sleep(long millis) {
        if (millis > 0L) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException var3) {
                return false;
            }
        }

        return true;
    }

    /**
     * 等待当前线程结束
     */
    public static void waitForDie() {
        waitForDie(Thread.currentThread());
    }

    /**
     * 等待线程结束. 调用 Thread.join() 并忽略 InterruptedException
     */
    public static void waitForDie(Thread thread) {
        if (null != thread) {
            boolean dead = false;
            do {
                try {
                    thread.join();
                    dead = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(!dead);

        }
    }

    /**
     * 获取JVM中与当前线程同组的所有线程
     */
    public static Thread[] getThreads() {
        return getThreads(Thread.currentThread().getThreadGroup().getParent());
    }

    /**
     * 获取同组的所有线程
     *
     * @param group 线程组
     */
    public static Thread[] getThreads(ThreadGroup group) {
        Thread[] slackList = new Thread[group.activeCount() * 2];
        int actualSize = group.enumerate(slackList);
        Thread[] result = new Thread[actualSize];
        System.arraycopy(slackList, 0, result, 0, actualSize);
        return result;
    }

    /**
     * 关闭线程池
     */
    public static synchronized void executorShutdown() {
        if (null != executor) {
            executor.shutdown();
        }
    }

    /**
     *  获取当前主线程
     */
    public static Thread getMainThread() {
        Thread[] threads = getThreads();
        for (Thread thread : threads) {
            if (thread.getId() == 1L) {
                return thread;
            }
        }
        return null;
    }
}
