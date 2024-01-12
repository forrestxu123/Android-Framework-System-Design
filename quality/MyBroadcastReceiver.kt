package com.codelabs.composetutorial

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager


class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Handle the broadcast and start WorkManager
        startWorkManager(context)
    }

    private fun startWorkManager(context: Context) {
        // Create a Constraints object if needed
        val constraints: Constraints =
            Constraints.Builder() // Add constraints if necessary (e.g., network connectivity)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // Create a OneTimeWorkRequest for your worker
        val workRequest: OneTimeWorkRequest =
            OneTimeWorkRequest.Builder(MyWorkerClass::class.java) // Set constraints if needed
                .setConstraints(constraints)
                .build()

        // Enqueue the work request with WorkManager
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
