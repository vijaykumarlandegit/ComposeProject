package com.example.composeproject.data.model

import com.google.firebase.Timestamp

data class TopicClass (
    val topicId: String = "",                  // Unique ID for the topic
    val topicTitle: String = "",               // Title of the topic
    val topicDescription: String = "",         // Description/details
    val startTime: Timestamp? = null,          // Study start time
    val endTime: Timestamp? = null,            // Study end time
    val timestamp: Timestamp? = null,          // When topic was added (for queries)
    val date: String = "",                     // Date as string (e.g. "2025-06-02")
    val subject: String = ""                   // Optional, for filtering by subject
)