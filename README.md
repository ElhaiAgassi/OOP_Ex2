# OOP_Ex2
Welcome to the Ex2 project!
This project provides a set of utility functions for working with text files.
In general: we create several text files and calculate the total number of lines in these files.

  We will use three methods:
• Normal method without the use of threads
• Using of threads
• Using ThreadPool

## createTextFiles

The createTextFiles function creates a specified number of text files, each containing a random number of lines with the text "I Love Programming". The function takes in three parameters:

- n: the number of text files to create
- seed: a seed value for the random number generator
- bound: the upper bound for the random number generator

The seed and bound parameters are used to generate a random number of lines for each file. The random number generator is initialized with the seed value and generates a number between 0 and bound (exclusive). This number is used as the number of lines in the file.

The function returns a String array containing the names of the created files. The file names are in the format "my_file1.txt", "my_file2.txt", and so on.
____________

## getNumOfLines

The getNumOfLines function counts the number of lines in a given array of text files. It takes in a String array containing the names of the text files and returns the total number of lines in all the files.

This function reads each file in the input array line by line using a BufferedReader, and increments a counter for each line. After all the files have been processed, the final value of the counter is returned as the result.
_____________

## getNumOfLinesThreads

The getNumOfLinesThreads function counts the number of lines in a given array of text files using a separate thread for each file. It extends the Thread class and overrides the run method to count the lines in a specific file. The function takes in a String array containing the names of the text files and returns the total number of lines in all the files.

For each file in the input array, a new LineCounterThread instance is created and started. The LineCounterThread class extends Thread and has a run method that reads the file line by line and increments a counter for each line. After all the threads have completed, the final value of the counter is returned as the result.
______________

## getNumOfLinesThreadPool

The getNumOfLinesThreadPool function counts the number of lines in a given array of text files using a fixed thread pool. It creates a class that implements the Callable interface and overrides the call method to count the lines in a specific file. The function takes in a String array containing the names of the text files and returns the total number of lines in all the files.

A fixed thread pool with a size equal to the length of the input fileNames array is created using the Executors class. For each file in the array, a new LineCounterCallable instance is created and submitted to the thread pool. The LineCounterCallable class implements the Callable interface and has a call method that reads the file line by line and increments a counter for each line. The call method returns the final value of the counter as the result.

----

After all the tasks have completed, the results are summed up to get the total number of lines in all the files. The thread pool is then shut down.

To use the Ex2 project, simply import the class into your project and call the desired



