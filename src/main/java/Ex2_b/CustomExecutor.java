package Ex2_b;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomExecutor {
    //The minimum number of threads in the collection of threads in CustomExecutor
    // will be half the number of processors
    private static final int MIN_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    //The maximum number of threads in the collection of threads in CustomExecutor
    // will be the number of available processors 1 less Java Virtual Machine
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    private static final long IDLE_TIMEOUT = 300L; // 300 milliseconds in milliseconds

    private final BlockingQueue<Task<?>> queue;
    private final Executor executor;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean shutdown;
    private int currentMaxPriority;

    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>();
        executor = Executors.newFixedThreadPool(MAX_THREADS);
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ExcessThreadKiller(), IDLE_TIMEOUT, IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
        shutdown = new AtomicBoolean(false);
        currentMaxPriority = 0;
    }

    public <V> Future<V> submit(Task<V> task) throws InterruptedException {
        if (!shutdown.get()) {
            queue.put(task);
            executor.execute(task);
        } else throw new RejectedExecutionException();
        return; // Error  Missing return value
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws InterruptedException {
        return submit(Task.createTask(callable, type));
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type, int priority) throws InterruptedException {
        return submit(Task.createTask(callable, type, priority));
    }

    public void gracefullyTerminate() {
        shutdown.set(false);
        scheduler.shutdown();
    }

    public int getCurrentMax() {
        return currentMaxPriority;
    }

    private class ExcessThreadKiller implements Runnable {
        @Override
        public void run() {
            if (queue.size() < MIN_THREADS) {
                ((ThreadPoolExecutor) executor).setCorePoolSize(MIN_THREADS);
            } else {
                ((ThreadPoolExecutor) executor).setCorePoolSize(MAX_THREADS);
            }
        }


    }
}
