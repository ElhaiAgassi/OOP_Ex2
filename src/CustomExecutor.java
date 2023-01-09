import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomExecutor {
    //the number of threads to keep in the pool, even if they are idle to be half the number of
    //processors available for the Java Virtual Machine (JVM)
    private static final int MIN_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    //  the maximum number of threads to allow in the pool to be on the number of processors
    //available for the Java Virtual Machine (JVM) minus 1
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    //when the number of threads is greater than the core, this is the maximum time that excess
    //idle threads will wait 300 milliseconds for new tasks before terminating
    private static final long IDLE_TIMEOUT = 3000L; // 3 seconds in milliseconds

    private final BlockingQueue<Future<?>> queue; //queue of mission
    public Executor executor;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean shutdown;

    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>();
        executor = Executors.newFixedThreadPool(MAX_THREADS); //factory method
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ExcessThreadKiller(), IDLE_TIMEOUT, IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
        shutdown = new AtomicBoolean(false);
    }



    public <V> Future<V> submit(Callable<V> callable) throws ExecutionException, InterruptedException {
        if (!shutdown.get()) {
            Task<V> task = Task.createTask(callable);
            queue.put((Future<?>) task);
            executor.execute((Runnable) task);
            return (Future<V>) task;
        } else {
            throw new RejectedExecutionException("CustomExecutor has been shut down");
        }
    }

    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws ExecutionException, InterruptedException {
        Task<V> task = Task.createTask(callable, type);
        queue.put((Future<?>) task);
        executor.execute((Runnable) task);
        return (Future<V>) task;
    }

    public int getCurrentMax() {
        int maxPriority = 0;
        for (Future<?> future : queue) {
            Task<?> task = (Task<?>) future;
            maxPriority = Math.max(maxPriority, task.getPriority());
        }
return maxPriority;
    }
        public void gracefullyTerminate() {
        scheduler.shutdownNow();
        ((ThreadPoolExecutor) executor).shutdown();
        shutdown.set(true);
    }
    public BlockingQueue<Future<?>> getQueueP() {
        return queue;
    }
    private class ExcessThreadKiller implements Runnable {
        @Override
        public void run() {
            // the number of active threads in the thread pool using the getActiveCount()
            int threadCount = ((ThreadPoolExecutor) executor).getActiveCount();
            if (threadCount > MIN_THREADS) {
                ((ThreadPoolExecutor) executor).setCorePoolSize(Math.max(MIN_THREADS, threadCount - 1));
            }
        }}}