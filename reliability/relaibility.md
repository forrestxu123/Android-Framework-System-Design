
# Android System Reliability Architecture Recommendations
**by [Jinlin Xu]**

# 1 Overview
The primary goal of the project is to enhance the reliability of the TikTok Android app and eliminate issues such as crashes, ANRs (Application Not Responding), and system event-related issue (e.g., network changes, battery events, memory management). These recommendations aim to ensure the reliability, monitoring, and quality of the TikTok app throughout its development and production lifecycle. The architecture includes various components to support robust issue monitoring, alerting, and the efficient identification and location of issues, along with automated updates and gradual upgrades.

# 2 Rquuirement Analysis

Funtional requirement:
  - Tiktok reliability Client: Develop the Tiktok reliability Client to find, locate and resolve issues in earlier datage. the features include: 
    - Collect all issues from current suite of automated / maunal tests in globe QA and development teams
    - Develop interface for QA team to intergate Tiktok reliability Client into their automation test cases.
    - Transfer necessary information to tiktok relability server.
    - Make notification if the system find an error issue
    - All error message (crashes, ANRs and system event-related issue) captured by DropBoxManager should also be collected by the Tiktok reliability Client. 
    - All error message that are from crash signals will captured by Tiktok reliability Client.
    - All error message should includes error type, call stack and other information that can help developer to identify the cause of error.  
  - Android Playstore: 
     - Create on-Demand modules for modules to be upgraded independent with app.
  - Tiktok Reliability Server: includes components such as Persistence Service, Monitoring & Alerting Service, Distributed Queue Service, System-Level Issues Service, Crash Service, ARN Service, and Web Service Load Balancer.

Implement a mechanism for automated updates or patching after bugs are fixed.
Ensure that updates are delivered seamlessly to the user's installed app.
Gradual Upgrades:

Support gradual upgrades to reduce the impact and ensure stability during the update process.
Allow for the phased rollout of updates to a small percentage of users initially.
Monitoring and Alerting:

Set up robust monitoring systems to track app performance, detect issues, and gather relevant data.
Implement alerting mechanisms to notify stakeholders in real-time when anomalies or critical issues are identified.
Issue Identification and Location:

Develop tools or mechanisms for efficient identification and location of issues.
Include features for detailed logging, error reporting, and diagnostics to aid in issue resolution.
Android Vitals Integration:

Integrate with Android Vitals to monitor and collect data related to app quality, including crash rates, ANR rates, and other vital metrics.
Reliability Server:

Establish a dedicated Reliability Server that includes components such as Persistence Service, Monitoring & Alerting Service, Distributed Queue Service, System-Level Issues Service, Crash Service, ARN Service, and Web Service Load Balancer.
Play Store Reporting API:

Leverage the Google Play Developer Reporting API to collect data about app quality, including crash rates, ANR rates, and error stack traces.
On-Demand and Conditional Delivery:

Utilize on-demand and conditional delivery for app modules, allowing features to be downloaded at app install time or removed later based on user preferences.
