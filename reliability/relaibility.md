
# TikTok Android App Reliability Framework Recommendations
**by [Jinlin Xu]**


# TikTok Reliability Framework Recommendations

## 1 Overview
The primary goal of the reliability framework is to enhance the TikTok Android app's reliability by addressing issues such as crashes, ANRs, performance challenges, memory management issues, and system event-related problems (e.g., network changes, battery events). These recommendations aim to ensure the reliability, monitoring, alerting, identification, and location of issues while maintaining the overall quality of the TikTok app throughout its lifecycle, including the stages of development, testing, app publishing, release, and upgrade. As results of the framework, it can help us minimize issues during app release and facilitate quick updates through module-based delivery.
The flexibility of this reliability framework allows for potential extension to different operating systems, providing a versatile solution for enhancing app reliability across diverse platforms.


# 2 Requirement Analysis

## 2.1 Functional Requirements

### 2.1.1 Tiktok Reliability Client

- **Identify Issues:**
  - Develop functionality to identify issues within the TikTok app.
  
- **Issue Collection:**
  - Implement a mechanism to collect issues, including call stack information, from the suite of automated/manual tests in the global QA and development teams.
  
- **Data Transfer:**
  - Enable the transfer of necessary issue information to the Tiktok Reliability Server, with user consent.
  
- **Integration Interface:**
  - Develop an interface for the QA team to seamlessly integrate the Tiktok Reliability Client into their automation test cases.

### 2.1.2 Android Playstore

- **On-Demand Modules:**
  - Create on-demand modules to allow independent upgrading of specific modules without impacting the entire app.

### 2.1.3 Tiktok Reliability Server

- **Issue Collection:**
  - Develop the capability to collect issues from devices with the TikTok app installed.

- **Reliability Dashboard:**
  - Provide a reliability dashboard for the development/QA team to assess the performance of the TikTok app.

- **Monitoring & Alerting Service:**
  - Implement a monitoring and alerting service for the development/QA team to identify, locate, and resolve issues efficiently.

- **Reliability REST API:**
  - Provide a reliability REST API to enable the development/QA team to incorporate additional features for issue analysis.

## 2.2 Non-Functional Requirements

### 2.2.1 Scalability

- **Issue Collection:**
  - Ensure the system can scale to collect issues from a billion devices.

### 2.2.2 Performance

- **Impact on TikTok App:**
  - Minimize the performance impact on the TikTok app during issue collection. If no issues are detected, ensure zero impact.

### 2.2.3 Security

- **Access Control:**
  - Implement robust access control mechanisms to restrict system usage to authorized personnel only.

# 3 Existing Systems

## 3.1 Android Vitals

Android Vitals is an integral part of the Android operating system, providing essential tools to seamlessly monitor and analyze app performance. Key features include:

- Data Collection Mechanism: Android Vitals components collect vital issue (crashes, ANRs, and other essential data) directly from users' devices, ensuring minimal impact on app runtime.
- Comprehensive Dashboard: The Android Vitals console offers a user-friendly dashboard for developers to assess and enhance the overall app quality and user experience.
- Playstore Reporting Rest API: Developers can leverage the Playstore Reporting Rest API to collect detailed app quality data from Android Vitals.



## 3.2 Firebase Crashlytics

- **Overview:**
  - Firebase Crashlytics is a crash reporting tool provided by Firebase, a mobile and web application development platform.
  - It provides real-time crash reporting and analysis.

- **Performance Impact:**
  - Firebase Crashlytics has a lightweight impact on app performance.
  - It is designed to operate efficiently without significantly affecting the user experience.

## 3.3 Crashlytics API

- **Overview:**
  - Crashlytics is a crash reporting solution, initially developed independently and later acquired by Fabric, which was then acquired by Google.

- **Performance Impact:**
  - Similar to Firebase Crashlytics, the standalone Crashlytics API is designed for minimal performance impact during crash reporting.

## 3.4 Perfetto

- **Overview:**
  - Perfetto is a system-wide performance tracing tool for Linux.

- **Performance Impact:**
  - Perfetto itself has minimal impact on app performance as it operates at the system level.
  - Its use in profiling and tracing is crucial for identifying performance bottlenecks.

## 3.5 Systrace

- **Overview:**
  - Systrace is a tool for collecting and inspecting system traces on Android devices.

- **Performance Impact:**
  - Systrace has a minimal impact on the app's runtime performance.
  - It provides valuable insights into system-level events and performance metrics.

## 3.6 Android Studio Profiler

- **Overview:**
  - Android Studio Profiler is an integrated profiling tool for Android app development.

- **Performance Impact:**
  - Profiling with Android Studio Profiler has a negligible impact on the app's runtime performance.
  - It offers insights into CPU, memory, and network usage during app execution.
