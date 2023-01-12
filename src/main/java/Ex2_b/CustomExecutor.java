package main.java.Ex2_b;

import java.util.concurrent.*;

/**
 * The CustomExecutor  class is responsible for managing a threadPool
 * and a priority queue of tasks, allowing you to submit new tasks and get the max priority
 * of the queued tasks.
 *
 *
 * @author Elhai Agassi & Danielle Musai
 *
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

    private int maxPriority = Integer.MAX_VALUE;

    public CustomExecutor() {
        super(MIN_THREADS, MAX_THREADS, IDLE_TIMEOUT, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
    }

    /**
     * Overrides the ThreadFactory method from ThreadPoolExecutor to create daemon threads with names in the format "Thread #X",
     * where X is an incrementing index.
     *
     * @return ThreadFactory that creates daemon threads with names in the format "Thread #X"
     */

    @Override
    public ThreadFactory getThreadFactory() {
        return callable -> {
            Thread t = new Thread(callable);
            t.setDaemon(true);
            t.setName("Thread #" + index++);
            return t;
        };
    }

    /**
     * Submits a task with a priority to the executor.
     *
     * @param task the task to be submitted
     * @return the submitted task
     * @throws Exception if the task's callable is null
     */
    public <V> Task<V> submit(Task<V> task) throws Exception {
        if (task.getCallable() == null) throw new NullPointerException();
        else {
            try {
                execute(task);
                this.setCurrentMax(getMax());
                return (task);
            } catch (Exception e) {
                throw new InterruptedException(e.getLocalizedMessage());
            }
        }
    }

    /**
     * Retrieves the maximum priority of tasks in the queue.
     *
     * @return the maximum priority of tasks in the queue, or 0 if the queue is empty
     */

    private int getMax() {
        if (this.getQueue().isEmpty()) return 0;
        else return ((Task<?>) (this.getQueue().peek())).getPriority();
    }

    /**
     * Submits a task with a priority to the executor.
     *
     * @param callable the callable to be submitted as a task
     * @param type the priority of the task
     * @return the submitted task
     * @throws Exception if the callable is null
     */

    public <V> Future<V> submit(Callable<V> callable, TaskType type) throws Exception {
        return submit(Task.createTask(callable, type));
    }

    /**
     * Submits a task with default priority to the executor.
     *
     * @param callable the callable to be submitted as a task
     * @return the submitted task
     * @throws NullPointerException if the callable is null
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
     * @return the current maximum priority in the task queue
     */
    public int getCurrentMax() {
        return this.maxPriority;
    }

    /**
     * Sets the current maximum priority in the task queue.
     * @param Priority the new maximum priority
     */

    public void setCurrentMax(int Priority) {
        this.maxPriority = Priority;
    }

    /**
     * Gracefully shuts down the executor by allowing currently running tasks to complete
     * and not accepting new tasks.
     */
    public void gracefullyTerminate() {
        this.shutdown();
    }
}