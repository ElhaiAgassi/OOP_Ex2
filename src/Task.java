import java.util.concurrent.*;

public class Task<V> implements Callable<V>, Future<V>,Runnable, Comparable<Task<V>> {
    private final Callable<V> callable;
    private final TaskType type;
    private V result;
    private Exception exception;
    private volatile boolean isDone;
    private volatile boolean isCancelled;

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
        try {
            result = callable.call();
        } catch (Exception e) {
            exception = e;
            throw e;
        }
        isDone = true;
        return result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone) {
            return false;
        }
        isCancelled = true;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public V get() {
        if (exception != null) {
            try {
                throw exception;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    public int getPriority() {
        return type.getPriorityValue();
    }

    @Override
    public int compareTo(Task<V> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            exception = e;
        }}
}