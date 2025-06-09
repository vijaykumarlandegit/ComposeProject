package com.example.composeproject.presentation.viewmodel

import android.content.Context
import android.icu.util.TimeUnit
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.composeproject.task.ReminderWorker
import java.util.Calendar
import androidx.work.PeriodicWorkRequestBuilder
import java.time.Duration
import androidx.work.Worker
import androidx.work.WorkerParameters



class DailyReminderViewModel {

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleDailyReminder(context: Context) {
        val delayMillis = calculateDelayTill8PM()

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatInterval = Duration.ofDays(1),
            flexTimeInterval = Duration.ofMinutes(15)
        )
            .setInitialDelay(Duration.ofMinutes(1))
            .setInitialDelay(Duration.ofMillis(delayMillis))
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "StudyReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun calculateDelayTill8PM(): Long {
        val now = Calendar.getInstance()
        val due = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 45)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (due.before(now)) {
            due.add(Calendar.DAY_OF_MONTH, 1)
        }
        return due.timeInMillis - now.timeInMillis
    }

}