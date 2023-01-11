package main.java.Ex2_b;

import java.util.concurrent.*;
/**
        * The {@code Task} class represents a unit of work that can be executed in a separate thread.
        * It implements the {@link Callable} and {@link Comparable} interfaces, allowing it to be
        * used with thread pools and sorted based on priority. The task can be created by providing callable object
        * and task type using factory methods or default task type will be 'OTHER'
        *
        * @param <V> the type of the result returned by this task
        *
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
     * @param type the type of task, which determines the priority
     */

    Task(Callable<V> callable, TaskType type) {
        super(callable);
        this.callable = callable;
        this.type = type;

    }

    /**
     * Creates a task object with the provided callable object and task type.
     *
     * @param callable the callable object that contains the unit of work to be executed
     * @param type the type of task, which determines the priority
     * @param <V> the type of the result returned by the callable
     * @return a new task object
     */

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    /**
     * Creates a task object with the provided callable object and task type as 'OTHER'
     *
     * @param callable the callable object that contains the unit of work to be executed
     * @param <V> the type of the result returned by the callable
     * @return a new task object
     */

    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    /**
     * Executes the callable object and returns the result.
     *
     * @return result returned by callable
     * @throws Exception the callable may throw an exception
    */
    public Callable<V> getCallable() throws Exception {
        return callable;
    }

    /**
     * Get the priority value of the task.
     *
     * @return the priority value
     */

    public int getPriority() {
        return type.getPriorityTypeValue();
    }


    @Override
    /**
     * Compare this task with the provided task by their priorities
     *
     * @param o the task to compare to
     * @return a negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     */
    public int compareTo(Task<V> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

}