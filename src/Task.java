import java.util.concurrent.*;

public class Task<T> implements Callable<T>, Comparable<Task<T>> {
    private Callable<T> operation;
    private TaskType type; // the type of the mission
    private T result;
    private Exception exception;
    private Future<T> future;

    private Task(Callable<T> operation, TaskType type) {
        this.operation = operation;
        this.type = type;
    }


    public static <T> Task<T> createTask(Callable<T> operation, TaskType type) {
        return new Task<>(operation, type);
    }

    public static <T> Task<T> createTask(Callable<T> operation) {
        return new Task<>(operation, TaskType.OTHER);
    }


    public void setFuture(Future<T> future) {
        this.future = future;
    }

    public T get() throws ExecutionException, InterruptedException {
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return future.get();
    }


    void setResult(T result) {
        this.result = result;
    }

    void setException(Exception exception) {
        this.exception = exception;
    }

    public int getPriority() {
        return type.getPriorityValue();
    }

    @Override
    public int compareTo(Task<T> other) {
        return Integer.compare(getPriority(), other.getPriority());
    }

    public T run() throws Exception {
        try {
            result = operation.call();
        } catch (Exception e) {
            exception = e;
            throw e;
        }
        return result;
    }

    @Override
    public T call() throws Exception {
        return null;
    }
}
