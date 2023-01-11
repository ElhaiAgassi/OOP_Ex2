package main.java.Ex2_b;


import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.*;

public class TaskTest {
    public static final Logger logger = LoggerFactory.getLogger(TaskTest.class);

    /**
     * check if the queue add by priority,
     * set core and max to be 1, because we need
     * that task get in the work queue.
     * Print - print the priorities og the queue's tasks
     * by the order in the queue
     */
    @Test
    public void partialTest() throws Exception {
        CustomExecutor customExecutor = new CustomExecutor();
        //TODO implement the classes
        for (int i = 0; i < 5; i++) {

            var task = Task.createTask(() -> {
                int sum = 0;
                for (int j = 1; j <= 10; j++) {
                    sum += j;
                }
                return sum;
            }, TaskType.COMPUTATIONAL);
            var sumTask = customExecutor.submit(task);
            final int sum;
            try {
                sum = sumTask.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            logger.info(() -> "Sum of 1 through 10 = " + sum);

            var math_result = customExecutor.submit(() -> {
                return 1000 * Math.pow(1.02, 5);
            }, TaskType.COMPUTATIONAL);

            Callable<String> testIO = () -> {
                StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                return sb.reverse().toString();
            };
            System.out.println(customExecutor.getCurrentMax());
            var revers_result = customExecutor.submit(testIO, TaskType.OTHER);
            System.out.println(customExecutor);

            final String get1;
            final double get2;
            try {
                get1 = revers_result.get();
                get2 = math_result.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            logger.info(() -> "Reversed String = " + get1);
            logger.info(() -> ("Total Price = " + get2));
            logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        }
        customExecutor.gracefullyTerminate();
    }
}