Authors @ElhaiAgassi & @Danielle Musai

# OOP_Ex2-THREADS
## Welcome to the Ex2 project!

<img width="203" alt="image" src="https://user-images.githubusercontent.com/92378800/211576576-6e6fc9bd-c6f6-4aa9-8fad-b6b2ea913bba.png">

_________
# Part A

This part of the project provides a set of utility functions for working with text files.
In general: we create several text files and calculate the total number of lines in these files.

  We will use three methods:
  
•  Normal method without the use of threads

•  Using of threads

•  Using ThreadPool

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

_______

# Running Time

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


# UML 

![image](https://user-images.githubusercontent.com/92378800/211555684-786c0430-e7bb-479f-bb79-6714ca3f097f.png)


_________
# Part B

Our goal at this part of the project is to  create a new two types that extend the functionality of Javas Concurrency Framework.
Create a new type that provides an asynchronous task with priority and a ThreadPool type that supports tasks priority.

We created a two classes:
Task & CustomExecutor
____
## Task
(based on the enum class we got 'TaskType')

This is implements two interfaces: Callable and Comparable
*Callable is an interface that's similar to Runnable, but it can return a value or throw an exception.
*Comparable is an interface that allows an object to be compared to other objects of the same type.
The compareTo() method, which is also defined in the Comparable interface, is overridden here to compare the priority of the current task to that of another task passed in as an argument. 
This is important when multiple tasks are queued and we want to ensure that the most important tasks are executed first.

The priority is determined by the TaskType enum, which is passed in to the constructor and stored as a field.

The class also has two static methods to create a Task object. createTask(Callable<V> callable, Ex2_b.TaskType type) will create a task object by providing the callable object and task type,
and createTask(Callable<V> callable) will create a task object by providing the callable object and default task type as Ex2_b.TaskType.OTHER.
  
In summary this class implements the Callable interface so that it can be used in a thread pool, and also implements the Comparable interface so that it can be sorted based on priority.
  
 ____
 ## CustomExecutor 
  
The CustomExecutor class is a custom implementation of a thread-pool and priority queue that manages tasks submitted to it. It utilizes the PriorityBlockingQueue class to implement the priority queue, and ExecutorService to manage the thread pool. The CustomExecutor class also uses ScheduledExecutorService to schedule a background task that periodically kills excess idle threads.

The class has several constant variables like:
  
 * MIN_THREADS - the number of threads to keep in the pool, even if they are idle to be half the number of
  processors available for the Java Virtual Machine (JVM)
 
 * MAX_THREADS - the maximum number of threads to allow in the pool, equal to the number of processors
  available for the Java Virtual Machine (JVM) minus 1
 
 * IDLE_TIMEOUT = 3000L - the maximum time that excess idle threads will wait for new tasks before terminating
  
 These 3 values are used to configure the thread pool and the background task for killing excess idle threads.

The class also maintains a state variable shutdown which is an instance of AtomicBoolean which is used to ensure that only once the executor is shut down.

When creating a new instance of the CustomExecutor class, it initializes a new PriorityBlockingQueue, creates a fixed thread pool using Executors.newFixedThreadPool(), creates a scheduled thread pool using Executors.newScheduledThreadPool(), schedules a task that kills excess idle threads, and sets the atomic boolean shutdown to false.

When we want to execute a task, we can use the CustomExecutor class's submit() method to add it to the queue.
The CustomExecutor will then take care of getting a thread from the pool and running the task.
  
The submit method:
- A Task instance
- An operation that may return a value. It will then be used for creating a Task instance
- An operation that may return a value and a TaskType. It will then be used for creating a
  Task instance

gracefullyTerminate() - method which is used to shutdown the scheduler and executor gracefully and sets the shutdown flag to true. 
getCurrentMax() - method that returns the max priority of queued tasks.
  
Overall, the Task and CustomExecutor classes demonstrate how to use threads in Java by providing a way to execute units of work concurrently, prioritize tasks, and manage the lifecycle of threads in a controlled environment.

## Design Patterns
  - Factory:
  In the Task class, where the class provides static factory methods createTask which creates new objects of the class. 
  This allows the caller to create new objects of the class without having to use the constructor directly.
  
  - Template:
  The template method pattern is used in the CustomExecutor class, with the method submitTask(Callable<V> task) which act as a template method that defines an           algorithm as a skeleton of methods that subclasses can override to build their own implementations, 
  but also rely on the implemented methods in the super class for the common behavior.
  
## S.O.L.I.D
  
In terms of SOLID principles, it appears that the Single Responsibility Principle is being followed. 
The Task class is responsible for wrapping a callable object and providing a way to compare its priority with other tasks. 
The CustomExecutor class is responsible for managing the thread pool and task queue.
Also, there are some indication of open-closed principle since the class Task is open for extension and closed for modification, and they are using Factory Method to generate the object .
The Liskov Substitution Principle states that objects of a superclass should be able to be replaced with objects of a subclass without affecting the correctness of the program.

In this 2 classes, this is demonstrated by the use of interfaces and abstract classes.

The Task class is a generic class that implements the Callable and Comparable interfaces. This allows it to be used as a replacement for any other class that also implements these interfaces, as long as it meets the same contract. In other words, it guarantees that any class that implements Callable and Comparable can be used in place of Task.

The CustomExecutor is using ExecutorService and PriorityBlockingQueue classes and since those class are providing a clear interface and abstraction, it makes sure that it is using those classes in a way that any other class that implements the same interfaces and abstract classes can be used in place of the current classes.

## UML
  ![image](https://user-images.githubusercontent.com/92378800/211555593-739c7be3-dea3-4ab3-a6d1-4a71207a4ec3.png)

