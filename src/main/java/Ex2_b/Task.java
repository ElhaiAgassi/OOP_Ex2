package Ex2_b;

import java.util.concurrent.*;

public class Task<V> implements Callable<V>, Comparable<Task<V>>  {
    private final Callable<V> callable;
    private final TaskType type;

    Task(Callable<V> callable, TaskType type) {
        this.callable = callable;
        this.type = type;
    }

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type);
    }

    public static <V> Task<V> createTask(Callable<V> callable) {
        return new Task<>(callable, TaskType.OTHER);
    }

    @Override
    public V call() throws Exception {
        return callable.call();
    }


    public int getPriority() {
        return type.getPriorityValue();
    }

    @Override
    public int compareTo(Task<V> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }


}