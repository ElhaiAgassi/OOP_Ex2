import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class Task<T> implements Comparable<Task<T>> {
    private Callable<T> operation;
    private TaskType type;
    private T result;
    private Exception exception;

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

    public T get() throws ExecutionException {
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
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
}
