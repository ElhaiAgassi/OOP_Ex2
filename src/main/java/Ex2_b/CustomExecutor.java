package main.java.Ex2_b;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * The CustomExecutor  class is responsible for managing a threadPool
 * and a priority queue of tasks, allowing you to submit new tasks and get the max priority
 * of the queued tasks.
 *
 * <p> It provides the ability to terminate gracefully and also schedule a background task that periodically
 * kills excess idle threads
 *
 * @author Elhai Agassi & Danielle Musai
 * @see PriorityBlockingQueue
 * @see ExecutorService
 * @see ScheduledExecutorService
 */

public class CustomExecutor {
    /** the number of threads to keep in the pool, even if they are idle to be half the number of
     *processors available for the Java Virtual Machine (JVM)
     */
    private static final int MIN_THREADS = Runtime.getRuntime().availableProcessors() / 2;
    /**
     * the maximum number of threads to allow in the pool, equal to the number of processors
     * available for the Java Virtual Machine (JVM) minus 1
     */
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() - 1;
    /**
     * the maximum time that excess idle threads will wait for new tasks before terminating
     */
    private static final long IDLE_TIMEOUT = 3000L; // 3 seconds in milliseconds
    /**
     * priority queue to hold the queue of tasks
     */
    private final PriorityBlockingQueue<Task<?>> queue;
    /**
     * Executor service to handle threads and assigns tasks to them.
     */
    public ExecutorService executor; //TODO Make it normal Executor, implement shutdown
    /**
     * Scheduler service to handle background task that periodically kills excess idle threads
     */
    private final ScheduledExecutorService scheduler;
    /**
     * Used to ensure that only once the executor is shut down
     */
    private final AtomicBoolean shutdown;
    /**
     * Creates a new CustomExecutor, with a thread pool, scheduler and a priority queue.
     * It also sets the core pool size and schedule a background task that periodically
     * kills excess idle threads.
     */
    public CustomExecutor() {
        queue = new PriorityBlockingQueue<>();
        executor = Executors.newFixedThreadPool(MAX_THREADS);
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ExcessThreadKiller(), IDLE_TIMEOUT, IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
        shutdown = new AtomicBoolean(false);
    }

    /**
     * Submits a task to the priority queue and thread pool
     *
     * @param task the task to submit
     * @param <V> the type of the result returned by the task
     * @return a Future representing pending completion of the task
     * @throws ExecutionException if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */

//    public <V> Future<V> submit(Task<V> task) throws ExecutionException, InterruptedException {
//        queue.put(task);
//        FutureTask<V> futureTask = new FutureTask<>(task); /// is it legal??
//        executor.execute(futureTask);
////        executor.execute(() -> {
////            try {
////                task.call();
////            } catch (Exception e) {
////                throw new RuntimeException(e); // what exception throw?
////            }
////        });
//        return futureTask;
//
//    }

    public <V> Future<V> submit(Task<V> task) throws InterruptedException {
        queue.put(task);
        return executor.submit(task);
    }

    /**

     Submits a callable to the priority queue and thread pool
     @param callable the callable to submit
     @param type the type of task
     @param <V> the type of the result returned by the task
     @return a Future representing pending completion of the task
     @throws ExecutionException if the computation threw an exception
     @throws InterruptedException if the current thread was interrupted while waiting
     */
    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws ExecutionException, InterruptedException {
        return submit(Task.createTask(callable, type));
    }

    /**

     Submits a callable to the priority queue and thread pool, with the type of task set to 'OTHER'
     @param callable the callable to submit
     @param <V> the type of the result returned by the task
     @return a Future representing pending completion of the task
     @throws ExecutionException if the computation threw an exception
     @throws InterruptedException if the current thread was interrupted while waiting
     @throws NullPointerException if callable is null
     */

    public <V> Future<V> submit(Callable<V> callable) throws ExecutionException, InterruptedException {
        if (callable == null) throw new NullPointerException();
        return submit(Task.createTask(callable));
    }

    /**
     * get the max priority of queued tasks
     * @return the max priority of queued tasks
     */

    public int getCurrentMax() {
        int maxPriority = 0;
        for (Task<?> future : queue) {
            maxPriority = Math.max(maxPriority, future.getPriority());
        }
        return maxPriority;
    }
    /**
     * Terminates the scheduler and executor gracefully and sets the shutdown flag to true
     */
    public void gracefullyTerminate() {
        scheduler.shutdownNow();
        executor.shutdown();
        shutdown.set(true);
    }

    /**
     * Getter for the priority queue
     * @return the priority queue
     */

    public BlockingQueue<Task<?>> getQueueP() {
        return queue;
    }
    /**
     * An inner class to periodically kill excess idle threads
     * private class ExcessThreadKiller implements Runnable
     * this method is called periodically by the scheduler
     * it gets the number of active threads in the thread pool and
     * if the number is greater than the core, it will set the core size to the minimum of MIN_THREADS and MAX_THREADS
     */

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