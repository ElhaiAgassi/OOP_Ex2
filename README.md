_OOP course Exercise #2_  
_Authors [@ElhaiAgassi]() & [@Danielle Musai](https://github.com/DanielleMusai)_



# <p align="center"> :black_medium_square: THREADS :black_medium_square: </p>
                                           
## <p align="center"> Welcome to the Ex2 project :v:  </p>                                                        

<p align="center">
<img width="317" alt="image" src="https://user-images.githubusercontent.com/92378800/211576757-c364f245-e4bf-46ef-9fc0-0fb2920d5cba.png">
</p>



# Part A :arrow_down:

This part of the project provides a set of utility functions for working with text files.
In general: we create several text files and calculate the total number of lines in these files.

  We will use three methods:
  
•  Normal method without the use of threads

•  Using of threads

•  Using ThreadPool

### createTextFiles

The createTextFiles function creates a specified number of text files, each containing a random number of lines with the text "I Love Programming". The function takes in three parameters:

- n: the number of text files to create
- seed: a seed value for the random number generator
- bound: the upper bound for the random number generator

The seed and bound parameters are used to generate a random number of lines for each file. The random number generator is initialized with the seed value and generates a number between 0 and bound (exclusive). This number is used as the number of lines in the file.

The function returns a String array containing the names of the created files. The file names are in the format "my_file1.txt", "my_file2.txt", and so on.
____________

### getNumOfLines

The getNumOfLines function counts the number of lines in a given array of text files. It takes in a String array containing the names of the text files and returns the total number of lines in all the files.

This function reads each file in the input array line by line using a BufferedReader, and increments a counter for each line. After all the files have been processed, the final value of the counter is returned as the result.
_____________

### getNumOfLinesThreads

The getNumOfLinesThreads function counts the number of lines in a given array of text files using a separate thread for each file. It extends the Thread class and overrides the run method to count the lines in a specific file. The function takes in a String array containing the names of the text files and returns the total number of lines in all the files.

For each file in the input array, a new LineCounterThread instance is created and started. The LineCounterThread class extends Thread and has a run method that reads the file line by line and increments a counter for each line. After all the threads have completed, the final value of the counter is returned as the result.
______________

### getNumOfLinesThreadPool

The getNumOfLinesThreadPool function counts the number of lines in a given array of text files using a fixed thread pool. It creates a class that implements the Callable interface and overrides the call method to count the lines in a specific file. The function takes in a String array containing the names of the text files and returns the total number of lines in all the files.

A fixed thread pool with a size equal to the length of the input fileNames array is created using the Executors class. For each file in the array, a new LineCounterCallable instance is created and submitted to the thread pool. The LineCounterCallable class implements the Callable interface and has a call method that reads the file line by line and increments a counter for each line. The call method returns the final value of the counter as the result.
After all the tasks have completed, the results are summed up to get the total number of lines in all the files. The thread pool is then shut down.


_______

## Running Time

It is important to understand the differences in running times because it can help determine which method is most efficient and suitable for a given task.

- Method 4, getNumOfLinesThreadPool: ThreadPool

This method creates a fixed number of threads that are reused to execute the tasks. When a task is submitted to the thread pool, a thread is taken from the pool and used to execute the task. When the task is finished, the thread is returned to the pool. This can lead to improved performance because it reduces the overhead of creating and destroying threads.

- Method 3, getNumOfLinesThreads: Multi-Thread

Each text file is assigned to a separate thread, and the threads run concurrently to count the lines in the text files. This method may not be as efficient as method 4 because it creates a new thread for each text file, which can lead to increased overhead.

- Method 2, getNumOfLines: Single-Thread

This method may be slower than the other two methods because it only uses one thread to count the lines in all of the text files.

In general, the running time of the methods will depend on the number and size of the text files, the number of available processors, and the workload of the system. To compare the running times of the methods accurately, a large number of 
After a number of tests it can be seen that:
As it is a large number of files, we saw that single-thread takes the most time compared to multi-thread and Threadpool which compete with each other for the best running time.
Most of the time, threadpool will "win".
As it is a small number of files, single-thread will perform the best running time, multi-thread after it and then with the worst running time will be the thread-pool.
<img width="737" alt="image" src="https://user-images.githubusercontent.com/92378800/210207320-eb4b65f8-cae4-4a84-a88a-64715ce01e9e.png">


_______


# <p align="center"> UML </p>  

![image](https://user-images.githubusercontent.com/92378800/211555684-786c0430-e7bb-479f-bb79-6714ca3f097f.png)


_________
# Part B :arrow_down:

Our goal at this part of the project is to  create a new two types that extend the functionality of Javas Concurrency Framework.
Create a new type that provides an asynchronous task with priority and a ThreadPool type that supports tasks priority.

We created a two classes:
Task & CustomExecutor
____
### Task
(based on the enum class we got 'TaskType')

 The Task class is a generic class that represents a unit of work that can be executed in a separate thread. It extends the FutureTask class and implements the Comparable interface. This allows it to be used with thread pools and sorted based on priority. 
The priority is determined by the TaskType enum, which is passed in to the constructor and stored as a field.

The class has two instance variables, a callable object that contains the unit of work to be executed and a task type that determines the priority of the task. The class has a constructor that takes in a callable object and a task type, and assigns them to the respective instance variables. The class also has two static factory methods, createTask(Callable, TaskType) and createTask(Callable), that return new instances of the Task class.
 
The class also has getter methods for the callable and priority instance variables and a compareTo() method that compares the priority of the current task to another task passed as a parameter. This class is useful for creating tasks that need to be executed in a separate thread and for managing the order in which these tasks are executed based on their priority.


 ____
 ### CustomExecutor 
  
The CustomExecutor class is a subclass of ThreadPoolExecutor, which is a built-in Java class for managing a pool of threads for executing tasks. The CustomExecutor class adds some additional functionality on top of the basic functionality provided by ThreadPoolExecutor.

The class has several constant variables like:
  
 * MIN_THREADS - the number of threads to keep in the pool, even if they are idle to be half the number of
  processors available for the Java Virtual Machine (JVM)
 
 * MAX_THREADS - the maximum number of threads to allow in the pool, equal to the number of processors
  available for the Java Virtual Machine (JVM) minus 1
 
 * IDLE_TIMEOUT = 3000L - the maximum time that excess idle threads will wait for new tasks before terminating
 
It overrides the getThreadFactory() method of ThreadPoolExecutor to create daemon threads with names in the format "Thread #X", where X is an incrementing index.

When we want to execute a task, we can use the CustomExecutor class's submit() method to add it to the queue.
The CustomExecutor will then take care of getting a thread from the pool and running the task.
  
The submit method:
- A Task instance
- An operation that may return a value. It will then be used for creating a Task instance
- An operation that may return a value and a TaskType. It will then be used for creating a
  Task instance

The getMax() method retrieves the maximum priority of tasks in the queue. It does this by calling the peek() method on the queue of tasks, which returns the first element in the queue without removing it. It then retrieves the priority of this task by calling the getPriority() method on it. If the queue is empty, the method returns 0.

The getCurrentMax() method retrieves the current maximum priority of tasks in the queue that have been submitted to the executor.

The setCurrentMax(int Priority) method is used to set the current maximum priority of tasks in the queue. It takes an integer representing the priority as a parameter and sets the maxPriority variable to that value. This method is typically called when a new task is submitted to the executor, in order to update the current maximum priority.

gracefullyTerminate() - method which is used to shutdown the scheduler and executor gracefully and sets the shutdown flag to true. 
  
Overall, the Task and CustomExecutor classes demonstrate how to use threads in Java by providing a way to execute units of work concurrently, prioritize tasks, and manage the lifecycle of threads in a controlled environment.

### Design Patterns
  - Factory:
  In the Task class, where the class provides static factory methods createTask which creates new objects of the class. 
  This allows the caller to create new objects of the class without having to use the constructor directly.
  
  - Template:
  The template method pattern is used in the CustomExecutor class, with the method submitTask(Callable<V> task) which act as a template method that defines an           algorithm as a skeleton of methods that subclasses can override to build their own implementations, 
  but also rely on the implemented methods in the super class for the common behavior.
  
### S.O.L.I.D
  
In terms of SOLID principles, it appears that the Single Responsibility Principle is being followed. 
The Task class is responsible for wrapping a callable object and providing a way to compare its priority with other tasks. 
The CustomExecutor class is responsible for managing the thread pool and task queue.
Also, there are some indication of open-closed principle since the class Task is open for extension and closed for modification, and they are using Factory Method to generate the object .
The Liskov Substitution Principle states that objects of a superclass should be able to be replaced with objects of a subclass without affecting the correctness of the program.

In this 2 classes, this is demonstrated by the use of interfaces and abstract classes.

The Task class is a generic class that implements the Callable and Comparable interfaces. This allows it to be used as a replacement for any other class that also implements these interfaces, as long as it meets the same contract. In other words, it guarantees that any class that implements Callable and Comparable can be used in place of Task.

The CustomExecutor is using ExecutorService and PriorityBlockingQueue classes and since those class are providing a clear interface and abstraction, it makes sure that it is using those classes in a way that any other class that implements the same interfaces and abstract classes can be used in place of the current classes.

# <p align="center"> UML </p>  
  
![image](https://user-images.githubusercontent.com/92378800/212049460-c0725938-2cfb-45ba-a5e1-dd32b8e96d82.png)
