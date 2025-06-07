package com.example.composeproject.data.model

import com.google.firebase.Timestamp


data class ReminderClass(
    val topic:String="",
    val time: Timestamp?=null,
    val reminderId:String=""
)
