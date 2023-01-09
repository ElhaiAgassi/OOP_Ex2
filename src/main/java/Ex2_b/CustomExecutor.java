package Ex2_b;


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

    private final PriorityBlockingQueue<Task<?>> queue; //queue of mission
    public ExecutorService executor; //TODO Make it normal Executor, implement shutdown
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean shutdown;

    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>();
        executor = Executors.newFixedThreadPool(MAX_THREADS); //factory method
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ExcessThreadKiller(), IDLE_TIMEOUT, IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
        shutdown = new AtomicBoolean(false);
    }


    public <V> Future<V> submit(Task<V> task) throws ExecutionException, InterruptedException {
        queue.put(task);
        FutureTask<V> futureTask = new FutureTask<>(task); /// is it legal??
        executor.execute(futureTask);
//        executor.execute(() -> {
//            try {
//                task.call();
//            } catch (Exception e) {
//                throw new RuntimeException(e); // what exception throw?
//            }
//        });
        return futureTask;

    }



    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws ExecutionException, InterruptedException {
        return submit(Task.createTask(callable, type));
    }
    public <V> Future<V> submit(Callable<V> callable) throws ExecutionException, InterruptedException {
        if (callable == null) throw new NullPointerException();
        return submit(Task.createTask(callable));
    }
    public int getCurrentMax() {
        int maxPriority = 0;
        for (Task<?> future : queue) {
            maxPriority = Math.max(maxPriority, future.getPriority());
        }
        return maxPriority;
    }

    public void gracefullyTerminate() {
        scheduler.shutdownNow();
        executor.shutdown();
        shutdown.set(true);
    }

    public BlockingQueue<Task<?>> getQueueP() {
        return queue;
    }

    private class ExcessThreadKiller implements Runnable {
        @Override
        public void run() {
            // the number of active threads in the thread pool using the getActiveCount()
            int threadCount = ((ThreadPoolExecutor) executor).getActiveCount();
            if (threadCount > MIN_THREADS) {
                ((ThreadPoolExecutor) executor).setCorePoolSize(Math.max(MIN_THREADS, MAX_THREADS));
            }
        }
    }
}