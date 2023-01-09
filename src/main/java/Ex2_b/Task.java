package Ex2_b;

import java.util.concurrent.Callable;

public class Task<V> implements Callable<V>, Comparable<Task<V>>, Runnable {
    private final Callable<V> callable;
    private final TaskType type;
    private final int priority;

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type) {
        return new Task<>(callable, type, 0);
    }

    public static <V> Task<V> createTask(Callable<V> callable, TaskType type, int priority) {
        return new Task<>(callable, type, priority);
    }

    private Task(Callable<V> callable, TaskType type, int priority) {
        this.callable = callable;
        this.type = type;
        this.priority = priority;
    }

    @Override
    public V call() throws Exception {
        return callable.call();
    }

    public TaskType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Task<V> other) {
        return Integer.compare(this.getPriority(), other.getPriority());
    }

    @Override
    public void run() {
        try {
            call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
