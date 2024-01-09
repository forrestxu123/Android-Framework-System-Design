#  A Comprehensive Dive into Android Technical Quality Excellence
This document serves as an exhaustive guide for achieving technical quality excellence in Android development. It covers critical aspects, including stability, performance, app size, and release quality, aiming to equip developers with practical knowledge to guarantee excellent outcomes. Introducing fundamental principles related to quality issues, the guide offers insights into issue identification, monitoring strategies, and problem resolution. With a focus on improving the overall development process, this resource is valuable for ensuring high-quality results in both Android applications and the Android platform. The key features we are going to introduce include:

[- Stability:](#a)

  - [Crash](#a1)

  - [Application not Response (ANR)](#a2)

  - [Memory Challenges](#a3)
      
[- Performance:](#b)

  - [Start and loading time ](#b1)

  - [Rendering](#b2)

[- Battery and network usage: ](#c) 
[- App Size Optimation:](#d)
[- Security Optimation:](#e)

 <a name="a"></a>
 
## 1 Stability
As a key element of technical quality excellence, stability is fundamental to the overall functionality and success of Android development. Stable products play a critical role in preventing disruptions such as crashes, ANR, and memory challenges, contributing to customer satisfaction. This section will explore the principles of issue identification, effective monitoring strategies, and problem resolution, empowering readers to enhance the robustness of their Android projects.

### 1.1 Crash
Handling and resolving crashes are essential in software development and for maintaining system reliability. When Android products encounter crashes, they disrupt user experiences and pose a risk to data integrity and system stability. Effectively addressing crashes involves navigating through various stages, including unraveling crashes, in-depth Analysis,  monitoring, and approaches to preventing crashes. This section focuses on these aspects to provide readers with valuable insights into managing crashes, ensuring a seamless user experience, and enhancing overall system stability.

####1.1.1  Decoding the Anatomy of Crashes

Understanding the details of crash events is crucial for developers to effectively address and prevent them. See below sample code snippets in Kotlin, and C/C++ for common scenarios where crashes occur:
- Kotlin:
  - FileNotFoundException: 
    ```c
    val file = File(Environment.getExternalStorageDirectory(), "example.txt")
    val inputStream = FileInputStream(file)  // FileNotFoundException
    ```

  - SecurityException: 
    ```c
    val uri = Uri.parse("content://com.android.contacts/data/1")
    val inputStream = contentResolver.openInputStream(uri) 
    ```

  - ArrayIndexOutOfBoundsException:
    ```c
    val androidVersions = arrayOf("Jelly Bean", "KitKat", "Lollipop", "Marshmallow")
    val version = androidVersions[10] 
    ```

- C/C++
  - Null Pointer Dereference
    ```c
    char* ptr = nullptr;
    int value = *ptr; // This line will crash and result in a segmentation fault
    ```

  - Out-of-Bounds Memory Access
    ```c
    int arr[5];
    int value = arr[10]; // This line goes out of bounds and may cause a crash
    ```

  - Division by Zero
    ```c
    int numerator = 88;
    int denominator = 0;
    int result = numerator / denominator; // This line will crash and result in a segmentation fault
    ```

  - Dangling Pointer Access
    ```c
    int* dynamicInt = new int(88);
  
    // Assign the pointer to another variable
    int* anotherPointer = dynamicInt;
    delete dynamicInt;
  
    // Access the memory through the other pointer (dangling pointer),This may cause a crash or undefined behavior
    std::cout << *anotherPointer << std::endl;
  
    // Free the memory again (double deletion),This may cause a crash or undefined behavior
    delete anotherPointer;
    return 0;
    ```

- Java/Kotlin based Components ( App and System Server):
  
  As we can see, a common scenario for Java/Kotlin app crashes is caused by an uncaught Throwable.  Therefore, to aid in identifying, locating, monitoring, and solving Java crash issues, it is crucial to understand how the Android system handles crashes in both Java environments. Here is the main workflow related to this topic:
  - App sets default uncaught exception handle:
     When an app is forked, it calls Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler()) to set the default uncaught exception handler for all throwable or exceptions in the process using an instance of KillApplicationHandler. Now, when an uncaught exception occurs in any thread within the process, KillApplicationHandler.uncaughtException() will be called to handle that exception.
  - App sets default uncaught exception handle:
    uncaughtException() calls the ActivityManager method handleApplicationCrash() when a throwable is not caught in the current app to request ActivityManagerService(AMS) for crash handling.
  - AMS Crash Handling:
    AMS collects all crash information needs and sends it to DropManagerService by calling the method DropManager#addData().
  - DropManagerService creates crash log information.
    DropManagerService receives the crash information from AMS and store crash information log file into /data/drop folder.
  - App Self-Termination Handling:
    the App takes appropriate actions to terminate itself.
See the java side of the following diagram for more detail.

<img src="crash.png" alt="Crash"/>

- Native components (JNI and Daemon):

  Any native component crash will cause the kernel to issue a signal from the list below in Android:
  - SIGABRT (Abort)
  - SIGBUS (Bus Error)
  - SIGFPE (Floating Point Exception)
  - SIGILL (Illegal Instruction)
  - SIGSEGV (Segmentation Fault)
  - SIGSTKFLT (Stack Fault) to the native components.
  From the example code provided above, we can also see that most crashes on the native side (C/C++) are related to improper memory handling. To support users in analyzing crashes and memory issues, Android loads liblinker, debugged library, and libAsan (Android 8.1+) when the app is started. This loading occurs as part of the Android runtime environment and aims to enhance debugging and analysis capabilities during runtime.

  - liblinker: A part of the Android runtime environment responsible for dynamic linking, loading, and unloading of shared libraries.
  - Debugged Library: When loaded, it provides additional debugging information, aiding developers in identifying and resolving issues during runtime.
  - libAsan (Android 8.1+): libAsan (AddressSanitizer) is a memory error detector tool that helps identify memory-related issues such as buffer overflows, use-after-free, and other memory corruptions at runtime, providing enhanced runtime debugging capabilities.

  When an ASan issue or crash occurs, the kernel will provide detailed information about the problem, including the location in the code where the issue happened, the type of issue (e.g., buffer overflow), and other relevant details. This information is valuable for developers to identify and fix bugs that could lead to crashes or other unexpected behavior. We will discuss this information in the next section. This section focuses on how the information of ASan issues or crashes is collected.
We can also utilize Debugged and libAsan for our native Daemon development if necessary. 

- Native based Components (JNI and Daemon):
  - 1. App sets default uncaught exception handle:
     When an app is forked, it calls Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler()) to set the default uncaught exception handler for all throwable or exceptions in the process using an instance of KillApplicationHandler. Now, when an uncaught exception occurs in any thread within the process, KillApplicationHandler.uncaughtException() will be called to handle that exception.
  - 2. App sets default uncaught exception handle:
    uncaughtException() calls the ActivityManager method handleApplicationCrash() when a throwable is not caught in the current app to request ActivityManagerService(AMS) for crash handling.
  - 3. AMS Crash Handling:
    AMS collects all crash information needs and sends it to DropManagerService by calling the method DropManager#addData().
  - 4. DropManagerService creates crash log information.
    DropManagerService receives the crash information from AMS and store crash information log file into /data/drop folder.
  - 5. App Self-Termination Handling:
    the App takes appropriate actions to terminate itself.


1、如何捕获崩溃（比如c++常见的野指针错误或是内存读写越界，当发生这些情况时程序不是异常退出了吗，我们如何捕获它呢）

2、如何获取堆栈信息（告诉我们崩溃是哪个函数，甚至是第几行发生的，这样我们才可能重现并修改问题）

3、将错误日志上传到指定服务器（这个最好办）

 

####1.1.2  Crash Analysis and Monitoring

####1.1.3  Strategies to Prevent Crashes
