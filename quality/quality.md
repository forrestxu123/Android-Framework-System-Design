#  A Comprehensive Dive into Android Technical Quality Excellence
This document serves as a guide for achieving technical quality excellence in Android development. It offers insights into issue identification, monitoring strategies, and problem resolution,providing developers with practical knowledge to ensure excellent outcomes. The key features we will introduce include:

[- Stability:](#a)

  - [Memory Challenges](#a1)
  
  - [Crash Challenges](#a2)

  - Application not Response (ANR)

      
[- Performance:](#b)

   - [Rendering](#b1)
    
   - Start and loading time

- Battery and network usage Optimization: 

- App Size Optimization:

- Security Optimization:

 <a name="a"></a>
 
## 1 Stability
As a key element of technical quality excellence, stability is fundamental to the overall functionality and success of Android development. Stable products play a critical role in preventing disruptions such as crashes, ANR, and memory challenges, contributing to customer satisfaction. This section will explore the principles of issue identification, effective monitoring strategies, and problem resolution, empowering readers to enhance the robustness of their Android projects.
 <a name="a1"></a>
### 1.1 Memory Challenges
Effective memory management is crucial for achieving optimal performance, preventing crashes, and delivering a seamless user experience in Android products. This section delves into memory management principles in both Java and C/C++, and introduces essential memory monitoring tools along with effective resolution approaches.

#### 1.1.1 Memory Management Principles
**Java Memory Management Principles**

In the Java environment, memory management relies on a garbage collector. It is crucial for developers to understand object lifecycle management and avoid unnecessary object retention. By allowing the garbage collector to reclaim memory efficiently, developers can prevent memory leaks and maintain optimal application performance.
Android employs a memory management strategy leveraging Young Generation garbage collection to enhance memory efficiency. Here's a high-level overview of the Young Generation garbage collection process:

- Young Generation Space:
  The Young Generation Space is divided into three spaces: Eden space and two survivor spaces (S0 and S1). Eden is where new objects are initially allocated and referenced by local variables in the stack or static variables in the method area. After each garbage collection cycle, surviving objects are moved to one of the survivor spaces.
- Minor Garbage Collection:
  When Eden space becomes full, a minor garbage collection is triggered. The garbage collector collects unreferenced objects in the Young Generation Space. The surviving objects are moved to one of the survivor spaces. Objects that survive several garbage collection cycles are eventually promoted to the Old Generation.
- Old Generation:
  The Old Generation is a memory space intended for longer-lived objects.
- Major Garbage Collection:
  Major garbage collection involves cleaning up the entire heap. Major GC is triggered when the Old Generation is close to full capacity or when explicit calls are made.

The following diagram shows the main work flow for Android  Jave garbage collection:

<img src="jvm.png" alt="JVM"/>

We can use VisualVM tool to monitoe above work flow, please see the daigram below:

<img src="VisualVM.png" alt="VisualVM"/>

In Minor/Major Garbage Collection, strong references and weak references play the role below in GC process.

Strong References: Objects referenced by strong references are considered reachable and are not eligible for garbage collection. As long as there is at least one strong reference pointing to an object, it will persist in memory.

Weak References: Objects referenced by weak references are considered weakly reachable. This means that they are eligible for garbage collection even if weak references point to them. When the GC runs, it will collect objects that only have weak references or have no other references, freeing up memory.

The purpose of weak references is often to avoid memory leaks. For example, in scenarios where an object is only needed as long as there are strong references to it, using weak references allows the object to be collected when no strong references exist, preventing unnecessary memory consumption. This principle has been used by LeakCanary for detecting memory leaks.
Based on our analysis, common memory issues in Java include:
- Insufficient Stack or Heap Space:
This can occur when there is insufficient space or a lack of continuous space in the stack or heap to allocate a Java object. Examples include a stack overflow due to excessive function calls and heap space limitations, leading to failed object allocation.

- Memory Leaks:
A long-lived object referencing a short-lived object can cause memory leaks because the short-lived object may not be appropriately released.

- Excessive Memory Allocation:
Allocating too much memory in the stack or heap can burden system resources and result in performance degradation.

- Garbage Collection Overhead:
Frequent GC cycles due to an abundance of memory allocation can impact performance negatively.

- Memory Corruption:
Unintentional memory overwrites or corruption can occur, such as buffer overflow, attempting to access memory through a null reference, leading to unpredictable behavior and potential crashes.

- Resource Leaks:
Failure to release resources like file handles or network connections can lead to resource leaks, affecting overall system performance.

Each item listed above can potentially lead to different issues, including Application Not Respone(ANR), OutOfMemoryError (OOM), degraded system performance, and unpredictable application behavior in some cases.

In conclusion, a comprehensive grasp of these memory management principles empowers developers to create Java applications that not only prevent memory-related challenges but also maintain optimal performance. By following best practices and utilizing modern tools, developers can effectively address memory management challenges and deliver top-quality software. We will focus on these topics in later sections.

 <a name="a112"></a>

**C/C++ Memory Management Principles**

In C/C++, memory management relies on manual allocation and deallocation. Developers must explicitly allocate and free memory to prevent memory leaks and maintain optimal performance. Alternatively, smart pointers, such as `std::unique_ptr` and `std::shared_ptr`, can automate memory management and enhance code safety.

**Stack and Heap:**
The stack is used for local variables and function call management. In contrast, the heap is a dynamic memory region where developers manually allocate and free memory.

**Common Memory Issues:**
- *Insufficient Stack or Heap Space:* Insufficient stack space can lead to a stack overflow, often caused by excessive function calls or large local variables. Insufficient heap space can occur when there's not enough continuous memory for dynamic allocation.
  
- *Excessive Memory Allocation:* Allocating too much memory in the stack or heap can strain system resources, leading to performance degradation or memory fragmentation.
  
- *Performance Impact:* Frequent manual memory allocation and deallocation, especially if done inefficiently, can negatively impact performance.

**Specific Memory Issues with Examples:**
- Memory Leaks:
  
  Cause gradually increase in memory usage.
```c
int main() {
    int* dynamicMemory = new int;
    // Missing 'delete' or 'free' statement, leading to a memory leak
    return 0;
}
```

- Buffer Overflow:
  
  Potentially overwriting adjacent memory and causing undefined behavior.
```c
int main() {
    char buffer[5];
    strcpy(buffer, "Overflowing the buffer");
    return 0;
}
```

- Use-After-Free:
  
  Results in undefined behavior, potentially a crash.
```c
int main() {
    int* dynamicMemory = new int;
    delete dynamicMemory;
    *dynamicMemory = 88; // Accessing memory after it has been freed
    return 0;
}
```

- Double Free:
  
  Results in undefined behavior, potentially a crash
```c
int main() {
    int* dynamicMemory = new int;
    delete dynamicMemory;
    delete dynamicMemory; 
    return 0;
}
```


- Out-of-Bounds Array Access:
  
  Results in undefined behavior, potentially a cras
```c
int main() {
    int array[3] = {1, 2, 3};
    int value = array[4]; // Accessing an element beyond the array bounds
    return 0;
}
```

 - Null Pointer Dereference
   
   Causes a segmentation fault as dereferencing a null pointer is an illega
    ```c
    char* ptr = nullptr;
    int value = *ptr; // This line will crash and result in a segmentation fault
    ```

  - Out-of-Bounds Memory Access

    Results in undefined behavior, potentially a crash
    ```c
    int arr[5];
    int value = arr[10]; // This line goes out of bounds and may cause a crash
    ```

  - Dangling Pointer Access
    
    Results in undefined behavior, potentially a crash. 
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
In conclusion, a solid understanding of memory management principles in C++ is essential for developers dedicated to building robust and high-performance software. By following best practices and utilizing modern tools, developers can effectively address memory management challenges and deliver top-quality software. We will focus on these topics in later sections.

 #### 1.1.2 Unlocking Memory Optimization

 This section introduces widely used tools to help readers optimize memory in Java and C/C++.

#### 1.1.2.1 Mastering Java Memory Optimization

**Android Memory Profiler**

Android Memory Profiler is an essential tool for Android developers. It helps them ensure optimal memory management, overcome performance challenges, and deliver a seamless user experience on the Android platform. Here is Key Highlights below:

- Memory Profiling: 

   track memory usage, heap dumps,  memory events and  native code allocations and deallocations

- Interface Overview:
  
  Provides a comprehensive timeline, controls for garbage collection and heap dump capture, and memory usage information.

- Memory Counts:

  provide memory counts of  Java, Native, Graphics, Stack, Code, and more. Distinguishes between allocated and deallocated objects.

- Visualizing Allocations:

  Presents a visual representation of memory allocations, includes object types, sizes, and allocation stack traces.

- Memory Leak Detection:
  
  Includes a feature for filtering data indicating potential memory leaks in Activity and Fragment instances.

For example, you can use the picture below to know:
- Java: Memory from objects allocated from Java or Kotlin code.
- Native: Memory from objects allocated from C or C++ code.
- Graphics: Memory used for graphics buffer queues 
- Stack: Memory used by both native and Java stacks in your app.
- Code: Memory that your app uses for code and resources, such as dex bytecode, optimized or compiled dex code, .so libraries, and fonts.
- Others: Memory used by your app that the system isn't sure how to categorize.
- Allocated: The number of Java/Kotlin objects allocated by your app.

  <img src="profiler1.png" alt="Profiler"/>

Please see [the link](https://developer.android.google.cn/studio/profile/memory-profiler) for more information about Android Memory Profiler.

**LeakCanary**

LeakCanary is a powerful memory leak detection library for Android, offering two main features:
- API Check: Developers can manually check any objects that are no longer needed using the provided API. The AppWatcher.objectwatch.watch() function creates a weak reference for the specified object. If this weak reference isn't cleared after a 5-second wait and garbage collection, the watched object is considered potentially leaking, and LeakCanary logs this information.

- Automatic Check: LeakCanary goes beyond manual checks by automatically detecting memory leaks in specific scenarios without requiring additional code. It achieves this by leveraging Android's lifecycle hooks. This automation is based on the understanding that the referenced objects are no longer needed after these lifecycle events, simplifying the process of identifying memory leaks during the development and debugging phases.

Here is the princiapl of LeakCanary:

- ObjectWatcher and Weak References:
  
When an attachedObject is watched using  AppWatcher.objectwatch.watch(attachedObject, description), LeakCanary creates a weak reference to that attachedObject.
- Garbage Collection and Weak References:

After a waiting period of 5 seconds, LeakCanary triggers garbage collection. Weak references allow the associated objects to be collected during garbage collection if there are no strong references pointing to them.

- Detection of Retained Objects:

If the weak reference held by the ObjectWatcher isn't cleared after garbage collection, it implies that the watched object has not been properly released from memory. This situation indicates a potential memory leak, as the object should have been collected if it was no longer needed.

- Logging and Identification:

LeakCanary logs information about the retained object, including its type and any provided description. Developers can inspect these logs to identify and address the source of the memory leak.

In summary, LeakCanary uses weak references and a systematic process of garbage collection and observation to identify objects that should have been released but are still being retained in memory, signaling a potential memory leak. This automated detection simplifies the debugging process for developers.

We take the following code as an example to locate memory leak using LeakCanary:

```c
	// Add below in gradle file
	debugImplementation ("com.squareup.leakcanary:leakcanary-android:3.0-alpha-1")
	
	
	class MainActivity : ComponentActivity() {
	
	    companion object {
	        lateinit var context : Context
	    }
	    override fun onCreate(savedInstanceState: Bundle?) {
	        super.onCreate(savedInstanceState){
		...
	   }
	}
	
	// in MainActivity, we have a botton defined as below:
	
	 Button({
	        val intent = Intent(context, MainActivity2::class.java)
	        context.startActivity(intent)
	}) {
	        ...
	}
	
	class MainActivity2 : AppCompatActivity() {
	    ...
	    override fun onCreate(savedInstanceState: Bundle?) {
	        MainActivity.context = this
	    ...
```
From the code, it's evident that a long-lived companion object context has a short-lived object MainActivity2. When we click the back button to finish MainActivity2, it causes a memory leak. The memory leak information obtained from the test phone is shown below:

<img src="leak.png" alt="Leak"/>

We can easily identify the cause of the memory leak from the UI on the device.

#### 1.1.2.2 Mastering C/C++ Memory Optimization

In this section, we are going to focus on AddressSanitizer (ASan)/ HWAddressSanitizer (HWASan) tools. Google has intergated ASan into Android system and suggests 'Whenever possible, prefer HWASan.' However, for simplicity in this document, we will use ASan. ASan is a memory error detection tool that helps identify memory-related issues such as buffer overflows, use-after-free, and other memory corruptions at runtime, providing enhanced runtime debugging capabilities. When properly set up in the [building system](https://developer.android.com/ndk/guides/asan), the app can be run without the need to make any code changes
We take the following code as an example to locate memory leak using ASan:

  ```c
 jstring native_get_string(JNIEnv* env) {
  std::string s = "Hellooooooooooooooo ";
   std::string_view sv = s + "World\n";
   // BUG:   the expression s + "World\n" creates a temporary string that has a limited lifetime.
   //  `sv` holds reference to the temporary string. After returm, `sv` holds a dangling reference to 
   // the string . Accessing the data here is a  use-after-free.
   return env->NewStringUTF(sv.data());
}

extern "C" JNIEXPORT jstring JNICALL
Java_android11_test_gwpasan_MainActivity_nativeGetString(
    JNIEnv* env, jobject /* this */) {
  jstring return_string;
  for (unsigned i = 0; i < 0x10000; ++i) {
    return_string = native_get_string(env);   // will have memory leak.
  }
  return reinterpret_cast<jstring>(env->NewGlobalRef(return_string));
}
 ```

This example native code has a heap use-after-free bug. It will cause the follwong log from logcate or /data/tombstone:
```c
*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
Build fingerprint: 'google/sargo/sargo:10/RPP3.200320.009/6360804:userdebug/dev-keys'
Revision: 'PVT1.0'
ABI: 'arm64'
Timestamp: 2020-04-06 18:27:08-0700
pid: 16227, tid: 16227, name: 11.test.gwpasan  >>> android11.test.gwpasan <<<
uid: 10238
signal 11 (SIGSEGV), code 2 (SEGV_ACCERR), fault addr 0x736ad4afe0
Cause: [GWP-ASan]: Use After Free on a 32-byte allocation at 0x736ad4afe0

backtrace:
      #00 pc 000000000037a090  /apex/com.android.art/lib64/libart.so (art::(anonymous namespace)::ScopedCheck::CheckNonHeapValue(char, art::(anonymous namespace)::JniValueType)+448)
      #01 pc 0000000000378440  /apex/com.android.art/lib64/libart.so (art::(anonymous namespace)::ScopedCheck::CheckPossibleHeapValue(art::ScopedObjectAccess&, char, art::(anonymous namespace)::JniValueType)+204)
      #02 pc 0000000000377bec  /apex/com.android.art/lib64/libart.so (art::(anonymous namespace)::ScopedCheck::Check(art::ScopedObjectAccess&, bool, char const*, art::(anonymous namespace)::JniValueType*)+612)
      #03 pc 000000000036dcf4  /apex/com.android.art/lib64/libart.so (art::(anonymous namespace)::CheckJNI::NewStringUTF(_JNIEnv*, char const*)+708)
      #04 pc 000000000000eda4  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (_JNIEnv::NewStringUTF(char const*)+40)
      #05 pc 000000000000eab8  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (native_get_string(_JNIEnv*)+144)
      #06 pc 000000000000edf8  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (Java_android11_test_gwpasan_MainActivity_nativeGetString+44)
      ...

deallocated by thread 16227:
      #00 pc 0000000000048970  /apex/com.android.runtime/lib64/bionic/libc.so (gwp_asan::AllocationMetadata::CallSiteInfo::RecordBacktrace(unsigned long (*)(unsigned long*, unsigned long))+80)
      #01 pc 0000000000048f30  /apex/com.android.runtime/lib64/bionic/libc.so (gwp_asan::GuardedPoolAllocator::deallocate(void*)+184)
      #02 pc 000000000000f130  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (std::__ndk1::_DeallocateCaller::__do_call(void*)+20)
      ...
      #08 pc 000000000000ed6c  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (std::__ndk1::basic_string<char, std::__ndk1::char_traits<char>, std::__ndk1::allocator<char> >::~basic_string()+100)
      #09 pc 000000000000ea90  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (native_get_string(_JNIEnv*)+104)
      #10 pc 000000000000edf8  /data/app/android11.test.gwpasan/lib/arm64/libmy-test.so (Java_android11_test_gwpasan_MainActivity_nativeGetString+44)
      ...
```

For more detailed information about analyzing the log files generated by Asan, you can refer to the [link] (https://developer.android.com/ndk/guides/gwp-asan) . We will introduce how the log file is captured in the Crash Challenges section.


#### 1.1.3  Best Practise to Prevent Memory Issue

In this section, we'll explore essential strategies to prevent memory issues in Android apps, emphasizing coding best practices:

- **Null Checks:** Perform explicit null checks, especially before accessing object references. This helps avoid NullPointerExceptions and ensures the stability of your app.

- **Memory Management:** Efficiently manage object creation and destruction. Leverage tools like the Android Memory Profiler and LeakCanary to identify memory leaks and optimize resource usage, enhancing overall app performance.

- **Thread Safety:** Implement robust thread safety measures, particularly when dealing with shared resources. Use synchronization or concurrency mechanisms to prevent race conditions and crashes caused by improper threading.

- **Static Code Analysis Tool Scanning:** Employ static code analysis tools to identify potential issues, vulnerabilities, and maintain high code quality throughout the software development lifecycle.

- **Thorough Code Review:** Conduct regular code reviews to catch potential issues early in the development process. Collaborative reviews help ensure code quality and adherence to best practices.

- **Resource Management:** Efficiently manage resources by releasing unused ones promptly. This practice is crucial for preventing memory leaks and ensuring optimal app performance.

By following these best practices, we can create robust Android applications with improved memory handling and overall stability.

 <a name="a2"></a>
 
### 1.2 Crash Challenges
Handling and resolving crashes are essential in software development and for maintaining system reliability. When Android products encounter crashes, they disrupt user experiences and pose a risk to data integrity and system stability. Effectively addressing crashes involves navigating through various stages, including unraveling crashes, crashes analysis,  crashes monitoring, and approaches to preventing crashes. This section focuses on these aspects to provide readers with valuable insights into managing crashes, ensuring a seamless user experience, and enhancing overall system stability.

#### 1.2.1  Decoding the Anatomy of Crashes

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
  See Null Pointer Dereference, Out-of-Bounds Memory Access, Dangling Pointer Access listed in [section 1.1.2 Memory Management Principles in C++](#a112)

  - Division by Zero
    ```c
    int numerator = 88;
    int denominator = 0;
    int result = numerator / denominator; // This line will crash and result in a segmentation fault
    ```

As we can see, a common scenario for Java/Kotlin app crashes is caused by an uncaught throwable/exception and most crashes on the native side (C/C++) are related to improper memory handling. Therefore, to aid in identifying, locating, monitoring, and solving Java crash issues, it is crucial to understand how the Android system handles crashes in Java environments. The following diagram shows the main work flow related to this topic:

<img src="crash.png" alt="Crash"/>

Let's explain the daigram:
- Java/Kotlin based Components (App and System Server) Crash Handling:
  - App sets default uncaught exception handle:
    
     When an app is forked, it calls Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler()) to set the default uncaught exception handler for all throwable or exceptions in the process using an instance of KillApplicationHandler. Now, when an uncaught exception occurs in any thread within the process, KillApplicationHandler.uncaughtException() will be called to handle that exception.
  - App sets default uncaught exception handle:
    
    uncaughtException() calls the ActivityManager method handleApplicationCrash() when a throwable is not caught in the current app to request ActivityManagerService(AMS) for crash handling.
  - AMS Crash Handling:
    
    AMS collects all crash information needs through handleApplicationCrashInner() and sends it to DropManagerService by calling the method DropManager#addData().
  - DropManagerService creates crash log information:
    
    DropManagerService receives the crash information from AMS and store crash information log file into /data/system/drop folder.
  - App Self-Termination Handling:
    
    the App takes appropriate actions to terminate itself.

- Native components (JNI and Daemon) Memory Issue and Crash Handling:

  Any native component crash will cause the kernel to issue a signal from the list below in Android:
  - SIGABRT (Abort)
  - SIGBUS (Bus Error)
  - SIGFPE (Floating Point Exception)
  - SIGILL (Illegal Instruction)
  - SIGSEGV (Segmentation Fault)
  - SIGSTKFLT (Stack Fault)
    
  To support users in analyzing crashes and memory issues, Android loads liblinker, debugged library, and [libAsan] (https://developer.android.com/ndk/guides/gwp-asan) when the app is started. This loading occurs as part of the Android runtime environment and aims to enhance debugging and analysis capabilities during runtime.
   - liblinker: A part of the Android runtime environment responsible for dynamic linking, loading, and unloading of shared libraries.
   - Debugged Library: When loaded, it provides additional debugging information, aiding developers in identifying and resolving issues during runtime.
   - libAsan (Android 8.1+): libAsan (AddressSanitizer) is a memory error detector tool that helps identify memory-related issues such as buffer overflows, use-after-free, and other memory corruptions at runtime, providing enhanced runtime debugging capabilities.

  When an ASan issue or crash occurs, the kernel and ASan tool provides detailed information about the problem, including the location in the code where the issue happened, the type of issue (e.g., buffer overflow), and other relevant details. This information is valuable for developers to identify and fix bugs that could lead to crashes or other unexpected behavior. We will discuss this information in the next section. This section focuses on how the information of ASan issues or crashes is collected (To simplify, we call it a crash issue here). Here is the main workflow related to this topic:
  - Triggle crash issue handling:
    
    The kernel triggers a crash signal or ASan triggers a memory issue. It causes the current app to use the debuggerd_signal_handler() method in the debugged library to handle crash issue information.
  - Create debuggerd dispatch pseudo thread to transfer crash issue information to the crashdump process:
    
    The debuggerd_signal_handler() method creates the debuggerd_dispatch_pseudo_thread. The debuggerd_dispatch_pseudo_thread creates the crashdump process and passes crash issue information to crashdump using a Pipe.
  - Log handling:
    
    The crashdump uses UDS to send crash issue information to tombstoned daemon for logging and store the informatuin at /data/tombstone.  The crashdump also uses UDS to send crash issue information to AMS for logging.
  - AMS Crash Handling:
    
    AMS has a NativeCrashListener thread started at the System Server launch stage. It creates a UDS socket to observe the crash from the crashdump process. If it receives crash issue information from the crashdump process, it creates a NativeCrashReport thread and calls handleApplicationCrashInner() for further handling.
  - DropManagerService creates crash log information.
    
    Similar to the handling in Java code, the crash log is put into the /data/drop folder.

Please note that the above workflow is available only for Android apps. However, we can also utilize Debugged and libAsan for our native Daemon development if necessary.

#### 1.2.2  Crash Analysis
Crash log files play a crucial role in identifying and resolving issues in Android development. Analyzing these logs provides valuable information about the root cause of crashes, contributing to enhancements in stability and user experience. Let's proceed to analyze several crash log files using both example code and log files.
##### 1.2.2.1  Java crash logfile analysis 

  We provide the following source code in MainActivity:
  ```c
        52  val contentResolver = contentResolver
        53  val uri = Uri.parse("content://com.android.contacts/data/1")
        54  val inputStream = contentResolver.openInputStream(uri) // SecurityException
 ```

  Run the application, we obtain the followng [log infromation](data_app_crash@1704743557731.txt) in /data/system/dropbox or logcat:

```c
java.lang.RuntimeException: Unable to start activity ComponentInfo{com.codelabs.composetutorial/com.codelabs.composetutorial.MainActivity}: java.lang.SecurityException: Permission Denial: ... 
	at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3782)
	at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3922)...
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:971)
Caused by: java.lang.SecurityException: Permission Denial: opening provider ...
	at android.os.Parcel.createExceptionOrNull(Parcel.java:3057)
	at android.os.Parcel.createException(Parcel.java:3041)...
	at android.content.ContentResolver.openInputStream(ContentResolver.java:1528)
	at com.codelabs.composetutorial.MainActivity.onCreate(MainActivity.kt:54)...
Caused by: android.os.RemoteException: Remote stack trace:
	at com.android.server.am.ContentProviderHelper.checkAssociationAndPermissionLocked(ContentProviderHelper.java:691)
	at com.android.server.am.ContentProviderHelper.getContentProviderImpl(ContentProviderHelper.java:287)
	at com.android.server.am.ContentProviderHelper.getContentProvider(ContentProviderHelper.java:144)
	at com.android.server.am.ActivityManagerService.getContentProvider(ActivityManagerService.java:6697)
	at android.app.IActivityManager$Stub.onTransact(IActivityManager.java:2761)
 ```
As seen, one crash causes 3 exceptions, making it challenging for the reader to understand. Let's provide further clarification:

- Binder IPC Failure - RemoteException
  - Description: The initial failure occurs in Binder Inter-Process Communication (IPC).
  - Cause: The IPC failure is a result of a permission issue, leading to a SecurityException.
  - Details: The RemoteException is thrown, indicating a problem in the communication channel.

- Propagation to SecurityException - Binder Proxy
  - Description: The SecurityException is detected and re-thrown in the Binder Proxy layer.
  - Cause: The SecurityException is the underlying issue in the IPC failure.
  - Details: The Binder Proxy, upon handling the RemoteException, identifies the embedded SecurityException and re-throws it.
    
- Exception Propagation to RuntimeException - ActivityThread
  - Description: The SecurityException further propagates up the stack, resulting in a java.lang.RuntimeException.
  - Cause: The root cause of the RuntimeException is the original SecurityException from the IPC failure.
  - Details: ActivityThread, during the launch of the activity, re-throws the received RuntimeException.

In a stack trace, the order of exceptions is typically determined by the order in which they were thrown. The most recently thrown exception (RuntimeException) appears at the top of the log without having a "Caused by:" prefix. The last caught exception (RemoteException) is at the bottom of the log. In this example, the root cause can be easily identified in line 54 based on the information:
  at com.codelabs.composetutorial.MainActivity.onCreate(MainActivity.kt:54)
Normally, the presence of the current app package name (e.g., com.codelabs.composetutorial) may indicate the specific location in our code where the issue or crash occurred. Analyzing this part of the code may help identify the root cause of the problem.

##### 1.2.2.2  Native Crash Log file Analysis 
 We provide the following source code:
```c
void com::example::Crasher::crash() {
    int* nullPointer = nullptr;
    *nullPointer = 88; // Attempting to dereference a null pointer
}

extern "C" {
    JNIEXPORT void JNICALL
    Java_com_example_testapp_MainActivity_runCrashThread(JNIEnv *env, jobject instance) {
        com::example::Crasher::crash();
    }
}
```

Run the application including above code, we obtain the followng log infromation in /data/tombstone or logcat:

```c
*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***
Build fingerprint: 'google/foo/bar:10/123.456/78910:user/release-keys'
ABI: 'arm64'
Timestamp: 2020-02-16 11:16:31+0100
pid: 8288, tid: 8288, name: com.example.testapp  >>> com.example.testapp <<<
uid: 1010332
signal 11 (SIGSEGV), code 1 (SEGV_MAPERR), fault addr 0x0
Cause: null pointer dereference
    x0  0000007da81396c0  x1  0000007fc91522d4  x2  0000000000000001  x3  000000000000206e
    x4  0000007da8087000  x5  0000007fc9152310  x6  0000007d209c6c68  x7  0000007da8087000
    x8  0000000000000000  x9  0000007cba01b660  x10 0000000000430000  x11 0000007d80000000
    x12 0000000000000060  x13 0000000023fafc10  x14 0000000000000006  x15 ffffffffffffffff
    x16 0000007cba01b618  x17 0000007da44c88c0  x18 0000007da943c000  x19 0000007da8087000
    x20 0000000000000000  x21 0000007da8087000  x22 0000007fc9152540  x23 0000007d17982d6b
    x24 0000000000000004  x25 0000007da823c020  x26 0000007da80870b0  x27 0000000000000001
    x28 0000007fc91522d0  x29 0000007fc91522a0
    sp  0000007fc9152290  lr  0000007d22d4e354  pc  0000007cba01b640

backtrace:
  #00  pc 0000000000042f89  /data/app/com.example.testapp/lib/arm64/libexample.so (com::example::Crasher::crash() const)
  #01  pc 0000000000000640  /data/app/com.example.testapp/lib/arm64/libexample.so (com::example::runCrashThread())
  #02  pc 0000000000065a3b  /system/lib/libc.so (__pthread_start(void*))
  #03  pc 000000000001e4fd  /system/lib/libc.so (__start_thread)
```
We can get a lot of information such as pid, uid, app package name, crash signal , carsh coause , register information and  backtrace for us to locate crash issue.

#### 1.2.3  Crash Monitoring

Android Vitals and Firebase Crashlytics are two distinct services offered by Google that exclusively manage issues arising from Java code. They both provide a comprehensive set of crash data and analytics to help developers diagnose and understand issues within their Android applications. For example, metrics related to the crash include:

- the number of affected users, the frequency of occurrences, and any other relevant statistical data.
- The version of the application in which the crash occurred.
- A detailed stack trace highlighting the sequence of method calls and the location where the crash occurred.
- Details about the device on which the crash occurred, such as the device model, manufacturer, and operating system version.

By analyzing the information provided in the services, developers can prioritize and address the most critical issues impacting their application's stability.
Application developers can also develop their own custom crash monitoring system, for example:

**UncaughtExceptionHandler Approch**

See below sample code snippets for java crash log
```c
class MyApplication : Application(), Thread.UncaughtExceptionHandler {
    private var systemUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    override fun onCreate() {
        super.onCreate()
        systemUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        // Additional initialization code if needed
    }
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        // Save the crash details to a file, database, or send it to a server
        saveCrashDetailsToFile(ex)
        val background = mainLooper.thread != thread
        if (systemUncaughtExceptionHandler != null) {
            val handler = Handler(mainLooper)
            handler.post {
                // If the user hasn't handled it, let the system default exception handler handle it
                systemUncaughtExceptionHandler?.uncaughtException(thread, ex)
            }
        }
    }
```

**DropBoxManage Approach**

See the lined sample code snippets [MyBroadcastReceiver.kt](MyBroadcastReceiver.kt) and [MyWorkerClass](MyWorkerClass.kt) for more information.  dropBoxManager.getNextEntry() returns the log related with crash, ANR,  native issue and System Events ( e.g. low memory, low power). 

**Registering Crash Signal Handlers Approach**

By leveraging signal handlers, developers can inject their crash reporting logic to transmit the log to a monitoring service. This approach is valuable for addressing native crashes and adapting crash reporting to meet specific development needs.

#### 1.2.4  Strategies to Prevent Crashes
In this section, we'll In this section, we'll focus on coding best practices and a solutions to enhance stability.

#### 1.2.4.1 Best Practise to Prevent Crashes
- Best Practise to Prevent Memory Issue: See section 1.1.3.
- Input Validation: Ensure thorough validation of user inputs to prevent unexpected data from causing issues.
- Handle Configuration Changes: Account for configuration changes (e.g., screen rotations) by properly handling the lifecycle events.
- Use Libraries Carefully: When integrating third-party libraries, ensure they are well-maintained, up-to-date, and compatible with your app's requirements.
- Testing and QA: Conduct thorough testing, including unit tests, integration tests, and real-device testing.
- Crash Reporting: Integrate a crash reporting tool like Firebase Crashlytics or other similar services. 
- Regular Updates: Keep your app and its dependencies up-to-date. 
- Error Handling: Provide meaningful error messages to users, log errors for developers, and gracefully handle unexpected scenarios to prevent crashes.
- Input Validation: Ensure thorough validation of user inputs to prevent unexpected data and potential crashes.
- Proactive Logging: Implement comprehensive logging to record relevant information, aiding in identifying and resolving issues before they lead to crashes.
- Asynchronous Operations: Handle asynchronous operations carefully, considering callback execution on the main thread and preventing ANR (Application Not Responding) errors.
- Permission Checks: Verify and request permissions appropriately to avoid security-related crashes.
- Network Connectivity: Safeguard against network-related crashes by checking for network availability before initiating network operations.

#### 1.2.4.2 A solution to Prevent Crashes

Here is the sample code snippet for handling uncaught exceptions below:

```c
class BeerApp: Application(){
    val leakedViews = mutableListOf<View>()
    override fun onCreate() {
        super.onCreate()
	initLoop()
    }

    private fun initLoop() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Exception) {
                    e.printStackTrace()
                    when {
                        e is SecurityException -> {
                            // Handle SecurityException
                        }
                        e is IndexOutOfBoundsException -> {
                            // Handle IndexOutOfBoundsException
                        }
                        e is NullPointerException -> {
                            // Handle NullPointerException
                        }
                        else -> {
                            handleOtherExceptions(e)
                        }
                    }
                }
            }
        }
    }

    private fun handleOtherExceptions(e: Exception) {
        val stack = Log.getStackTraceString(e)
        when {
            stack.contains("YourCustomMessage1")
                    || stack.contains("YourCustomMessage2")
                    || stack.contains("YourCustomMessage3")
                // ... add more conditions as needed
            -> {
                e.printStackTrace()
            }
            else -> {
                throw e
            }
        }
    }
```
When mainHandler.post is called, the original loop within Looper.loop() source code in [Looper.java](https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/core/java/android/os/Looper.java;drc=2c73f5abdf31943e12c68e75f8148c10d2abbf6a;l=160?q=Looper.JAVA&ss=android) becomes the outer loop. All code outside this loop is never executed since it contains an infinite inner loop. The code within this inner loop becomes accessible for use. Therefore, we can conditionally handle it based on specific conditions. Using this way, We can resolve many uncaught exception crash. 

the provided solution may prevent immediate crashes and allow for custom exception handling, it comes with potential risks and should be tested and used carefully, considering the specific requirements and context of the application.

 <a name="b"></a>
## 2 Performance
Effective performance optimization plays a crucial role in enhancing user experiences and ensuring overall project success. This section will specifically focus on the optimization of graphic rendering, start-up, and loading times. By providing insights and strategies in these areas, readers can gain the knowledge needed to enhance the performance of their Android applications, ultimately delivering a seamless and responsive user experience.
 <a name="b1"></a>
### 2.1 Rendering
In this section, we will introduce the principles of the Android Graphic Framework, guide readers on monitoring and locating graphic issues, and provide solutions to resolve these issues. The goal is to empower readers to identify, locate, and effectively resolve rendering problems, ensuring a smoother and visually pleasing user experience.

#### 2.1.1  The principal of Android Graphic Framework
The Android Graphics Framework is vital for crafting engaging visual experiences on Android devices. Its key components play a crucial role in rendering and managing graphical elements, utilizing the Surface class in Image Stream Producers for seamless interaction with SurfaceFlinger to efficiently render images. This involves features such as buffer queue reuse, collaboration with the Hardware Composer and synchronization with VSYNC for optimal performance and memory efficiency.

Key Components:

- Image stream producers: Components that create graphic buffers, such as the image stream in the Camera, WindowManagerService, MediaPlayer, and OpenGL ES, are referred to as image stream producers. They are closely connected with a surface, enabling efficient buffer handling.
- Rendering Tools:The Android Graphics Framework leverages rendering tools such as OpenGL ES, Skia, Vulkan, and Decoders to enhance the rendering capabilities. These tools contribute to the creation and manipulation of graphical content, further enriching the visual experience.
- Surface: The Surface is the image stream producer component that points to the graphic buffer queue. Created and managed by SurfaceFlinger, this buffer queue is a fundamental element in the interaction between image stream producers and SurfaceFlinger. It plays a crucial role in ensuring seamless handling of graphic buffers on the client side, contributing to the overall performance of the Android Graphics Framework.
- SurfaceFlinger: SurfaceFlinger is a crucial system service tasked with consuming currently visible surfaces and composing them onto the display. SurfaceFlinger utilizes OpenGL and the Hardware Composer to compose a collection of surfaces. Key features include::

   - Vsync Integration: SurfaceFlinger seamlessly incorporates Vsync (Vertical Synchronization) features, guaranteeing smooth and synchronized rendering. This integration involves coordination with the caller in the graphics pipeline and receive Vsync event from HW Cpmposer.

   - Enqueue/Dequeue Mechanism: SurfaceFlinger manages the queueing and dequeuing of surfaces in collaboration with its caller, ensuring smooth transitions and updates in graphical content

   - Collaboration with Hardware Composer(HW Composer): As an HW Composer stream producer, SurfaceFlinger shares composed buffers for HW Composer to consume.

- HW Composer: The HW Composer acts as the hardware abstraction for the display subsystem. It collaborates with SurfaceFlinger to offload certain composition work from OpenGL and the GPU, contributing to lower power consumption. Therefore, the stream composer could be either SurfaceFlinger or HW Composer. The HW Composer consumes the composed stream using a 2-buffer approach as outlined below:
  - Acquire a composed buffer from the stream composer for display.
  - Release a displayed buffer for the composer to inject the stream.
- Gralloc: Gralloc is responsible for allocating and managing graphics memory. These buffers are seamlessly interacted with by components such as Surface, SurfaceFlinger, and Hardware Composer, ensuring a cohesive and optimized visual experience on Android devices.

<img src="../graphic.png" alt="Android Graphic"/>
 <a name="e"></a>

#### 2.1.2  Analysis of Graphic Rendering Performance and Issues:

An example of the frame rendering process work flow is illustrated in the diagram below:

<img src="graphicdropframe.png" alt="Rendering"/>

In the diagram, arrows represent refresh time, and the blocks represent frame time. The below is their rates definition:
 - **Refresh Rates:**
  Refresh rates refer to how often a display refreshes per second.
 - **Frame Rates:**
  Frame rate indicates how many frames are processed per second in the rendering process.

Different colors in the diagram represent the time spent by a rendering thread in each state in the diagram.  
- Green: Running state. The rendering thread is in the process to draw the frame.
- Blue: Runnable state. The thread is available to run but isn't currently scheduled.
- Gray: Lock block state. The thread is blocked on a mutex lock.
- Orange: I/O block state. The thread is blocked on I/O.
- Purple: STW block state. The thread is blocked by STW due to GC.
  
The numbers (1 and 2) in the diagram correspond to the actual frame numbers during processing. There could be multiple blocks shared with the same number. The sum of the time spent for the same number means the actual time spent processing a frame.

In the diagram, some frames have dropped because of different factors. This may cause screen tearing or stuttering, impacting the smoothness of animations or visual output:

- **Screen Tearing:**
  Screen tearing occurs when two different frames are displayed on the screen simultaneously, leading to a visible horizontal line or "tear" between them.

- **Stuttering:**
  Stuttering refers to motion being stuck during animations or video playback.

Fundamentally, stuttering and screen tearing occur due to the difference between frame rates and the refresh rate . The main causes are often related to the following factors:

- **Overdraw:**
  e.g., Excessive layering.

- **Memory Management:**
  e.g., Stop the world (STW) caused by GC and excessive memory allocation.

- **Thread Management:**
  e.g., Thread scheduling, I/O block, and lock block.

Therefore, we conclude that:
- Longer green color block indicates overdraw, possibly caused by many UI layers or time-consuming code in drawing.
- Longer purple color block suggests that your code is causing excessive memory allocation, potentially leading to Garbage Collection (GC).
- Longer gray color block implies that the rendering thread is blocked by a lock.
- Longer blue color block may indicate insufficient system resources, causing the rendering thread to wait for an extended period before execution.
- Longer orange color block suggests that the rendering thread has been blocked due to excessive Input/Output (IO) operations.

#### 2.1.3 Graphic Rendering Performance Monitoring:
In this secton,  we provide three approach to monitor Graphic Rendering Performance.

**Monitoring Tools**

To analyze frame rendering performance, we can leverage  [systrace](https://developer.android.com/topic/performance/tracing/) and [perfetto](https://perfetto.dev/). The diagram below, extracted from systrace, illustrates frame rendering:

<img src="renderthread.png" alt="Frame Rendering"/>

- A red circle with 'f' inside indicates a dropped frame.
- A green circle with 'f' inside signifies a correctly drawn frame.
- The line between 'deliverInputEvent' and 'UI Thread' is color-coded based on the states introduced in section 2.1.2, representing different states of the render thread .


**Looper with custom logging**

Inspect the AOSP code snippet for Looper:
```c
public final class Looper {
    private Printer mLogging;
    // Used for customized Printer for logging
    public void setMessageLogging(@Nullable Printer printer) {
        mLogging = printer;
    }

    public static Looper getMainLooper() {
        ...
    }

    public static void loop() {
        final Looper me = myLooper();
   	...

        for (;;) {
            if (!loopOnce(me, ident, thresholdOverride)) {
                return;
            }
        }
    }

   private static boolean loopOnce(final Looper me,
            final long ident, final int thresholdOverride) {
        Message msg = me.mQueue.next(); // Specifically, each frame rendering involves receiving a message here
        ...
        final Printer logging = me.mLogging;
        if (logging != null) {
            logging.println(">>>>> Dispatching to " + msg.target + " "
                    + msg.callback + ": " + msg.what);
        }
        ...
        token = observer.messageDispatchStarting();
        ...

        if (logging != null) {
            logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
        }
        ...
    }
}
```
In this custom logging mechanism, each frame rendering results in a message being received.

As we can see, each frame rendering will cause me.mQueue.next() to receive a message. The information will be logged before and after the message is processed. This approach is utilized by monitoring tools like BlockCanary. Developing a custom logging mechanism allows us to incorporate various features, such as detecting frame drops during graphic rendering, capturing stack information when a frame is dropped, and measuring navigation time when a button is clicked. However, this solution has some limitations. It is primarily focused on UI rendering and also does not take print time cost into consideration.

**Choreographer#postFrameCallback**

Analyzing the Choreographer class reveals the following:

- postFrameCallback (Choreographer.FrameCallback callback): Posts a frame callback to run on the next frame. The callback runs once and is automatically removed.
- Choreographer.FrameCallback#doFrame(long frameTimeNanos): Called when a new display frame is being rendered, providing the time (in nanoseconds) when the frame started rendering.
- The difference between two consecutive frameTimeNanos values represents the time taken to render the previous frame.
We can utilize the following code snippet to monitor frame rendering:

```c
Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
    private long lastFrameTimeNanos = 0;
    @Override
    public void doFrame(long frameTimeNanos) {
        if (lastFrameTimeNanos == 0) {
            lastFrameTimeNanos = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }
        double diff = (frameTimeNanos - lastFrameTimeNanos) / 1000000.0;
        if (diff > 16.67) {
            int dropCount = (int) (diff / 16.7);
            if (dropCount >= 2) {
                Log.w(TAG, dropCount + " frames have been dropped. Time difference: " + diff + " ms");

            }
        }
        lastFrameTimeNanos = frameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);
    }
});
```
This solution is also focused on UI rendering and does not take print time cost into consideration.

