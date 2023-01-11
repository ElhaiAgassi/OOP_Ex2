package main.java.Ex2_b;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

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

public class CustomExecutor extends ThreadPoolExecutor {
    /**
     * the number of threads to keep in the pool, even if they are idle to be half the number of
     * processors available for the Java Virtual Machine (JVM)
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
    private static final long IDLE_TIMEOUT = 300L; // 300 milliseconds

    private int index = 0;

    /**
     * priority queue to hold the queue of tasks
     */
//    private final PriorityQueueThreadFactory queue;
    /**
     * Scheduler service to handle background task that periodically kills excess idle threads
     */

//   private final ScheduledExecutorService scheduler;

//    private final ThreadPoolExecutor executor;
    /**
     * Used to ensure that only once the executor is shut down
     */
    private final AtomicBoolean shutdown;
    private int maxPriority = 0;

    /**
     * Creates a new CustomExecutor, with a thread pool, scheduler and a priority queue.
     * It also sets the core pool size and schedule a background task that periodically
     * kills excess idle threads.
     */
    public CustomExecutor() {
        super(MIN_THREADS, MAX_THREADS, IDLE_TIMEOUT, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
        shutdown = new AtomicBoolean(false);
    }


    @Override
    public ThreadFactory getThreadFactory() {
        return callable -> {
            Thread t = new Thread(callable);
            t.setDaemon(true);
            t.setName("Thread #" + index++);
            return t;
        };
    }
    // submit = 1. get callable return future 2. execute 3. return future

    public <V> Future <V> submit(Task<V> task) throws Exception {
        if (task.getCallable() == null) throw new NullPointerException();
        else {
            this.setCurrentMax(Math.max(task.getPriority(), maxPriority));
            try {
                execute(task);
                return (task);
            } catch (Exception e) {
                throw new InterruptedException(e.getLocalizedMessage());
            }
        }
    }

    /**
     * Submits a callable to the priority queue and thread pool
     *
     * @param callable the callable to submit
     * @param type     the type of task
     * @param <V>      the type of the result returned by the task
     * @return a Future representing pending completion of the task
     * @throws ExecutionException   if the computation threw an exception
     * @throws InterruptedException if the current thread was interrupted while waiting
     */
    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws Exception {
        return submit(Task.createTask(callable, type));
    }

    /**
     * Submits a callable to the priority queue and thread pool, with the type of task set to 'OTHER'
     *
     * @param callable the callable to submit
     * @param <V>      the type of the result returned by the task
     * @return a Future representing pending completion of the task
     * @throws ExecutionException   if the computation threw an exception
     * @throws NullPointerException if callable is null
     */

    public <V> Future<V> submit(Callable<V> callable) {
        if (callable == null) throw new NullPointerException();
        try {
            return submit(Task.createTask(callable));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * get the max priority of queued tasks
     *
     * @return the max priority of queued tasks
     */

    public int getCurrentMax() {
        return this.maxPriority;
    }

    public void setCurrentMax(int Priority) {
        this.maxPriority = Priority;
    }

    /**
     * Terminates the scheduler and executor gracefully and sets the shutdown flag to true
     */
    public void gracefullyTerminate() {
        shutdown.set(true);
        this.shutdownNow();
    }
}