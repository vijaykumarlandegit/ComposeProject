package com.example.composeproject.data.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import com.example.composeproject.MainActivity
import com.example.composeproject.data.model.ReminderClass
import com.example.composeproject.presentation.viewmodel.ReminderViewModel
import java.util.Date

fun scheduleReminders(context: Context, reminderList: List<ReminderClass>) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    for (item in reminderList) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TITLE", item.topic)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.topic.hashCode(), // unique for each topic
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val timeInMillis = item.time?.toDate()?.time ?: return
        Log.d("ALARM", "Reminder for: ${item.topic}")
        Log.d("ALARM", "Time (UTC to millis): ${item.time?.toDate()?.time}")
        Log.d("ALARM", "Time (Date): ${item.time?.toDate()}")
        Log.d("ALARM", "Now: ${Date(System.currentTimeMillis())}")

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }
}
