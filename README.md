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

The Task class is a generic class that represents a unit of work that can be executed in a separate thread.
It extends the FutureTask class and implements the Comparable interface. This allows it to be used with thread pools and sorted based on priority. 
The priority is determined by the TaskType enum, which is passed in to the constructor and stored as a field.

The class has two instance variables, a callable object that contains the unit of work to be executed and a task type that determines the priority of the task.
The class has a constructor that takes in a callable object and a task type, and assigns them to the respective instance variables. 
The class also has two static factory methods, createTask(Callable, TaskType) and createTask(Callable), 
that return new instances of the Task class.
 
The class also has getter methods for the callable and priority instance variables and a compareTo() method that compares the priority of the current task to another task passed as a parameter. 
This class is useful for creating tasks that need to be executed in a separate thread and for managing the order in which these tasks are executed based on their priority.


 ____
 ### CustomExecutor 
  
  
The CustomExecutor class is a subclass of ThreadPoolExecutor, which is a built-in Java class for managing a pool of threads for executing tasks. 
The CustomExecutor class adds some additional functionality on top of the basic functionality provided by ThreadPoolExecutor.

The class has several constant variables like:
  
 * MIN_THREADS - the number of threads to keep in the pool, even if they are idle to be half the number of
  processors available for the Java Virtual Machine (JVM)
 
 * MAX_THREADS - the maximum number of threads to allow in the pool, equal to the number of processors
  available for the Java Virtual Machine (JVM) minus 1
 
 * IDLE_TIMEOUT = 3000L - the maximum time that excess idle threads will wait for new tasks before terminating

When we want to execute a task, we can use the CustomExecutor class's submit() method to add it to the queue.
The CustomExecutor will then take care of getting a thread from the pool and running the task.
  
The submit method:
- A Task instance
- An operation that may return a value. It will then be used for creating a Task instance
- An operation that may return a value and a TaskType. It will then be used for creating a
  Task instance

The getMax() method retrieves the maximum priority of tasks in the queue. It does this by calling the peek() method on the queue of tasks, 
which returns the first element in the queue without removing it.
It then retrieves the priority of this task by calling the getPriority() method on it. 
If the queue is empty, the method returns 0.

The getCurrentMax() method retrieves the current maximum priority of tasks in the queue that have been submitted to the executor.

The setCurrentMax() method is used to set the current maximum priority of tasks in the queue. 
It takes an integer representing the priority as a parameter and sets the maxPriority variable to that value. 
This method is typically called when a new task is submitted to the executor, in order to update the current maximum priority.

gracefullyTerminate() - method which is used to shutdown the scheduler and executor gracefully and sets the shutdown flag to true. 
  
Overall, the Task and CustomExecutor classes demonstrate how to use threads in Java by providing a way to execute units of work concurrently, prioritize tasks, and manage the lifecycle of threads.


### Design Patterns
##### Factory:
The Factory Method pattern is a creational design pattern that provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.

In this case, the CustomExecutor class uses the factory method pattern in the method submit(Callable<V> callable, TaskType type) and submit(Callable<V> callable) where the callable passed to the method is used to create a new task object by calling Task.createTask(callable, type) or Task.createTask(callable) respectively.

In the Task class, where the class provides static factory methods createTask which creates new objects of the class. 
In this case, the Factory Method pattern allows the CustomExecutor class to create Task objects without having to know the specific implementation of the Task class, and it also allows subclasses of the Task class to be used without modifying the CustomExecutor class.

##### Template:
This pattern is a behavioral design pattern that defines the skeleton of an algorithm in a method, called template method, deferring some steps to subclasses. In this case, the ThreadPoolExecutor class defines the skeleton of the algorithm for managing a pool of threads, and the CustomExecutor class is a subclass that overrides some of the methods to add additional functionality.
  
ThreadPoolExecutor class defines the skeleton of the algorithm for managing a pool of threads, providing the basic functionality such as maintaining a queue of tasks, creating and managing threads, and controlling the number of threads in the pool. CustomExecutor class, which is a subclass of ThreadPoolExecutor, overrides some of the methods to add additional functionality such as the ability to submit tasks with a priority and track the maximum priority of tasks in the queue.

The template method pattern allows the ThreadPoolExecutor to define the basic structure and behavior of a thread pool, while also allowing subclasses like CustomExecutor to customize and extend that behavior.
  
### S.O.L.I.D
  
##### These principles are:

- Single Responsibility Principle
- Open-Closed Principle
- Liskov Substitution Principle 
- Interface Segregation Principle 
- Dependency Inversion Principle

The CustomExecutor class and its parent class ThreadPoolExecutor maintains to some of these principles.

- Single Responsibility Principle :
  
This principle states that a class should have one, and only one, reason to change.
The CustomExecutor class has a single responsibility, which is to manage a thread pool and a priority queue of tasks, 
allowing you to submit new tasks and get the max priority of the queued tasks.

- Open-Closed Principle: 
  
This principle states that the behavior of a class can be extended without modifying its source code.
The CustomExecutor class is open for extension and closed for modification. 
It allows for additional functionality to be added without modifying the existing code.

- Liskov Substitution Principle:
  
This principle states that objects of a superclass should be able to be replaced with objects of a subclass without affecting the correctness of the program. 
The CustomExecutor class is a subtype of ThreadPoolExecutor, it can be used as a substitute for ThreadPoolExecutor without any modification.

- Interface Segregation Principle :
  
This principle states that a class should not be forced to implement interfaces it does not use.
The CustomExecutor class has a few methods that are specific to its own functionality and not related to the basic functionality of ThreadPoolExecutor.

- Dependency Inversion Principle:
  
This principle states that the code should be organized in a way that high-level modules depend on interfaces rather than concrete implementations. 
The CustomExecutor class depends on the ThreadPoolExecutor class and the Task class, and it's not dependent on the specific implementation of those classes,
 it only depends on their interfaces.

In general, the CustomExecutor class and its parent class ThreadPoolExecutor follow some of the SOLID principles and make the code more maintainable & flexible.


# <p align="center"> UML </p>  
  
![image](https://user-images.githubusercontent.com/92378800/212168628-bcc445dd-f9e0-4891-b518-4b530ff3424b.png)
