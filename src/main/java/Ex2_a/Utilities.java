package Ex2_a;
import Ex2_a.Ex2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

public class Utilities {

    public static void timeCounter(String func, String[] parameter) {
        Instant start = Instant.now();
        try {
            Method method = Ex2.class.getMethod(func, String[].class);
            int result = (int) method.invoke(func, (Object) parameter);
            System.out.println(result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis() + " milliseconds");
    }
}
