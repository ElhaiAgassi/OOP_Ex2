import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomExecutor {
    //The minimum number of threads in the collection of threads in CustomExecutor
    // will be half the number of processors
    private static final int MIN_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    //The maximum number of threads in the collection of threads in CustomExecutor
    // will be the number of available processors 1 less Java Virtual Machine
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    private static final long IDLE_TIMEOUT = 3000L; // 3 seconds in milliseconds

    private final BlockingQueue<Task<?>> queue;
    private final Executor executor;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean shutdown;

    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>();
        executor = Executors.newFixedThreadPool(MAX_THREADS);
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ExcessThreadKiller(), IDLE_TIMEOUT, IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
        shutdown = new AtomicBoolean(false);
    }

    public <V> void submit(Task<V> task) throws InterruptedException {
        if (!shutdown.get()) {
            queue.put(task);
            executor.execute(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    // handle exception
                }
            });
        } else {
            throw new RejectedExecutionException("CustomExecutor has been shut down");
        }
    }

    public <V> void submit(Callable<V> callable, TaskType type) throws InterruptedException {
        submit(Task.createTask(callable,type));
    }
    public <V> void submit(Callable<V> callable) throws InterruptedException {
        submit(Task.createTask(callable));
    }

    public <V> V get(Task<V> task) throws Exception {
        return task.get();
    }
    public int getCurrentMax() {
        int maxPriority = 0;
        for (Task<?> task : queue) {
            maxPriority = Math.max(maxPriority, task.getPriority());
        }
        return maxPriority;
    }

    public void gracefullyTerminate() {
        scheduler.shutdownNow();
        ((ThreadPoolExecutor) executor).shutdown();
        shutdown.set(true);

    }
    private class ExcessThreadKiller implements Runnable {
        @Override
        public void run() {
            int threadCount = ((ThreadPoolExecutor) executor).getActiveCount();
            if (threadCount > MIN_THREADS) {
                ((ThreadPoolExecutor) executor).setCorePoolSize(Math.max(MIN_THREADS, threadCount - 1));
            }
        }
    }
}
