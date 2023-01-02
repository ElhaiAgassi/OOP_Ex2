import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


/**
 * Class for creating text files and counting the number of lines in a set of text files.
 */

public class Ex2 {

    /**
     * Creates n text files with random number of lines, from 0 to bound-1, with a seed value for the random generator.
     * @param n the number of text files to create
     * @param seed the seed value for the random generator
     * @param bound the maximum number of lines for each text file
     * @return an array of strings representing the names of the text files created
     */
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] fileNames = new String[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            fileNames[i] = "my_file" + (i + 1) + ".txt";
            int numLines = rand.nextInt(bound);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileNames[i]))) {
                for (int j = 0; j < numLines; j++) {
                    bw.write("I Love Programming");
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileNames;
    }

    /**
     * Counts the number of lines in a set of text files using a single thread.
     * This code executes in a single thread, meaning that the line counting for each file is done
     * sequentially rather than in parallel.
     * @param fileNames an array of strings representing the names of the text files
     * @return the total number of lines in the text files
     */

    public static int getNumOfLines(String[] fileNames) {
        int numLines = 0;
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while (br.readLine() != null) {
                    numLines++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return numLines;
    }

    /**
     * Counts the number of lines in a set of text files using multiple threads.
     * @param fileNames an array of strings representing the names of the text files
     * @return the total number of lines in the text files
     */
    public static int getNumOfLinesThreads(String[] fileNames) {
        // a new thread is created for each file name in the fileNames array
        //LineCounterThread class is a custom class that extends the Thread class
        LineCounterThread[] threads = new LineCounterThread[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new LineCounterThread(fileNames[i]);
            // start() is called, means that the line counting for each file will be done in parallel by different threads.
            threads[i].start();
        }

        int numLines = 0;
        for (LineCounterThread thread : threads) {
            try {
                //join() method,causes the main thread to block until the line counting thread has completed
                thread.join();
                numLines += thread.getNumLines();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return numLines;
    }

    /**

     Counts the number of lines in a set of text files using a thread pool.
     @param fileNames an array of strings representing the names of the text files
     @return the total number of lines in the text files
     */

    public static int getNumOfLinesThreadPool(String[] fileNames) {
        //The size of the thread pool is set to the length of the fileNames array
        ExecutorService threadPool = Executors.newFixedThreadPool(fileNames.length);
        List<Future<Integer>> missions = new ArrayList<>();
        for (String fileName : fileNames) {
            //The Callable interface is similar to the Runnable interface, but it allows a thread to return a result
            // new LineCounterCallable instance is created and submitted to the thread pool using the submit() method of the ExecutorService.
            LineCounterCallable task = new LineCounterCallable(fileName);
            //submit() method returns a Future object that stored in a list
            missions.add(threadPool.submit(task));
        }

        int numLines = 0;
        for (Future<Integer> mission : missions) {
            try {
                numLines += mission.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        return numLines;
    }
}

/**
 A class that implements a thread for counting the number of lines in a text file.
 */

class LineCounterThread extends Thread {
    private final String fileName;
    private int numLines;
    /**
     Constructs a new LineCounterThread instance.
     @param fileName the name of the file to count the lines from
     */

    public LineCounterThread(String fileName) {
        this.fileName = fileName;
    }
    /**
     Reads through the file and counts the number of lines.
     */

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                numLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     Returns the number of lines counted by this thread.
     @return the number of lines counted
     */

    public int getNumLines() {
        return numLines;
    }
}

/**
 A class that implements a Callable for counting the number of lines in a text file.
 */

class LineCounterCallable implements Callable<Integer> {
    private final String fileName;

    /**
     Constructs a new LineCounterCallable instance.
     @param fileName the name of the file to count the lines from
     */

    public LineCounterCallable(String fileName) {
        this.fileName = fileName;
    }

    /**
     Reads through the file and counts the number of lines.
     @return the number of lines counted
     */

    @Override
    public Integer call() {
        int numLines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                numLines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numLines;

    }
}







/* TODO THIS NOTE
  Note that this implementation is just a sample and may not handle all possible edge cases.
  For example, it does not check if the input `fileNames` array is `null` or if the files already exist.
  You may want to add additional error handling and validation to the code as needed.
 */

