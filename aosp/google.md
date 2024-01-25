PhoneInterfaceManager.java:
PhoneInterfaceManager is a system service. The changes made is for  Telephony2gUpdater available to receive a broadcast receiver message and act as a listener for subscription changes.
Telephony2gUpdater.java:
  - My comments about the code:
    -  goAsync()
      
  Calling goAsync() in onReceive()  keeps the broadcast active after returning from onReceive(). However, even with this approach the system expects you to finish with the broadcast very quickly (under 10 seconds). 
  It does allow you to move work to another thread to avoid glitching the main thread. suggestion use Scheduling a job with the JobScheduler or workmanager if the time can no be controlled.
  
   -  the return of getCompleteActiveSubscriptionIdList() or getCompleteActiveSubscriptionIdList() all flaged as @NonNull, therfore:
   if (subscriptionInfoList == null) {
              return;
   } is not necessary. we can remove it.
 - Code Logic: 
   - Two constructors:  The constructors initialize the following fields:
      - mExecutor: An Executor used to execute logic on a separate thread.
      - mContext: An instance of Context used for accessing system services.
      - mBaseAllowedNetworks: Represents the base allowed network types.
  - The init() method registers the instance as a listener for subscription changes and sets up a dynamic broadcast receiver with the action UserManager.ACTION_USER_RESTRICTIONS_CHANGED for user restriction changes.
  - The onReceive() method is invoked when a broadcast with the action UserManager.ACTION_USER_RESTRICTIONS_CHANGED is received. It calls goAsync() to hold a wake lock. The actual logic in handleUserRestrictionsChanged() is executed on a separate thread. And finally release the wake lock.
  - the handleUserRestrictionsChanged() method processes changes in user restrictions related to cellular network access:
  - SubscriptionListener inner class implements onSubscriptionsChanged to trigger the handleUserRestrictionsChanged method when subscriptions change.

We can follow 
https://source.android.com/docs/setup/contribute/life-of-a-patch
https://source.android.com/docs/setup/contribute/submit-patches
to submit the patch and review the patch.
