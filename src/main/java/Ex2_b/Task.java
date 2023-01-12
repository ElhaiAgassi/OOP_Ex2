package main.java.Ex2_b;

import java.util.concurrent.*;

/**
 * The {@code Task} class represents a unit of work that can be executed in a separate thread.
 * It implements the {@link FutureTask} and {@link Comparable} interfaces, allowing it to be
 * used with thread pools and sorted based on priority.
 *
 * @param <V> the type of the result returned by this task
 * @author Elhai Agassi & Danielle Musai
 */

public class Task<V> extends FutureTask<V> implements Comparable<Task<V>>  {
    /**
     * Holds the callable object that contains the unit of work to be executed
     */
    private final Callable<V> callable;

    /**
     * Holds the type of task, which determines the priority
     */
    private final TaskType type;

    /**
     * Creates a new task with the provided callable object and task type.
     *
     * @param callable the callable object that contains the unit of work to be executed
     * @param type     the type of task, which determines the priority
     */

    Task(Callable<V> callable, TaskType type) {
        super(callable);
        this.callable = callable;
        this.type = type;

    }

    /**
     * Creates an instance of the Task class with the given callable and task type.
     *
     * @param callable the callable object that contains the unit of work to be executed
     * @param type the type of task, which determines the priority
     * @return an instance of the Task class
     */

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    /**
     * Creates an instance of the Task class with the given callable and default task type.
     *
     * @param callable the callable object that contains the unit of work to be executed
     * @return an instance of the Task class
     */

    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }
    /**
     * @return the callable object that contains the unit of work to be executed
     */

    public Callable<V> getCallable() throws Exception {
        return callable;
    }

    /**
     * @return the priority of the task
     */

    public int getPriority() {
        return type.getPriorityTypeValue();
    }

    /**
     * Compares the priority of the current task to another task passed as a parameter.
     *
     * @param o the other task to compare to
     * @return a negative integer, zero, or a positive integer as the current task's priority is less than,
     * equal to, or greater than the other task's priority
     */

    public int compareTo(Task<V> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

}