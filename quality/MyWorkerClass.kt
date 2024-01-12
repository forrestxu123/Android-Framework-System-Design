package com.codelabs.composetutorial

import android.content.Context
import android.os.DropBoxManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets


class MyWorkerClass(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Fetch crash logs from DropBoxManager
        fetchCrashLogs()

        // Indicate success or failure
        return Result.success()
    }

    private fun fetchCrashLogs() {
        val dropBoxManager =
            getApplicationContext().getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager
        if (dropBoxManager != null) {
            // Specify the tag and time to retrieve crash logs
            val tag = "your_dropbox_tag"
            val time = System.currentTimeMillis() // Set the desired time
            try {
                var entry = dropBoxManager.getNextEntry(tag, time - 1)
                while (entry != null) {
                    // Process the crash log entry
                    processCrashLogEntry(entry)

                    // Move to the next entry
                    entry = dropBoxManager.getNextEntry(tag, entry.timeMillis - 1)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun processCrashLogEntry(entry: DropBoxManager.Entry) {
        // Process the crash log entry, e.g., read from InputStream, log, or save to a file
        // Note: This is a simplified example; actual processing may vary based on log format
        try {
            entry.inputStream.use { inputStream ->
                // Read from inputStream and handle the crash log data
                // Example: Convert inputStream to String and log
                val logData = convertInputStreamToString(inputStream!!)
                Log.d("CrashLog", logData)

                // Close the entry to release resources
                entry.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun convertInputStreamToString(inputStream: InputStream): String {
        // Convert InputStream to String
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString(StandardCharsets.UTF_8.name())
    }
}
