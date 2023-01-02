import java.util.concurrent.Callable;

public class Task<T> implements Callable<T> {

    private final TaskType taskType;
    private final int priority;
    private final Callable<T> operation;

    private Task(TaskType taskType, Callable<T> operation) {
        this.taskType = taskType;
        this.priority = taskType.getPriorityValue();
        this.operation = operation;
    }

    public static <T> Task<T> of(TaskType taskType, Callable<T> operation) {
        return new Task<>(taskType, operation);
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public T call() throws Exception {
        return operation.call();
    }
}
