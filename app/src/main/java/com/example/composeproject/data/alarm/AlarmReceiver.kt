package com.example.composeproject.data.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.composeproject.presentation.activity.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TITLE") ?: "Reminder"
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("TITLE", title)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(alarmIntent)
    }
}
