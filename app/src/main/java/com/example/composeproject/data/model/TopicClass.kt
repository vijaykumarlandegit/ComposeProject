package com.example.composeproject.data.model

import com.google.firebase.Timestamp

data class TopicClass (
    val topicId: String = "",
    val topicTitle: String = "",
    val topicDescription: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val timestamp: Timestamp? = null,
    val date: String = "",
    val subject: String = ""
)