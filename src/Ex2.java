import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Ex2 {
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

    public static class LineCounterThread extends Thread {
        private String fileName;
        private int numLines;

        public LineCounterThread(String fileName) {
            this.fileName = fileName;
        }

        public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while (br.readLine() != null) {
                    numLines++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int getNumLines() {
            return numLines;
        }
    }

    public static int getNumOfLinesThreads(String[] fileNames) {
        LineCounterThread[] threads = new LineCounterThread[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new LineCounterThread(fileNames[i]);
            threads[i].start();
        }

        int numLines = 0;
        for (LineCounterThread thread : threads) {
            try {
                thread.join();
                numLines += thread.getNumLines();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return numLines;
    }


    public static int getNumOfLinesThreadPool(String[] fileNames) {
        ExecutorService threadPool = Executors.newFixedThreadPool(fileNames.length);
        List<Future<Integer>> futures = new ArrayList<>();
        for (String fileName : fileNames) {
            LineCounterCallable task = new LineCounterCallable(fileName);
            futures.add(threadPool.submit(task));
        }

        int numLines = 0;
        for (Future<Integer> future : futures) {
            try {
                numLines += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        threadPool.shutdown();
        return numLines;
    }
}

class LineCounterCallable implements Callable<Integer> {
    private String fileName;

    public LineCounterCallable(String fileName) {
        this.fileName = fileName;
    }

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