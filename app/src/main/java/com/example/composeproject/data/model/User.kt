package com.example.composeproject.data.model

data class User(
    val name:String="",
    val uid: String = "",
    val email: String? = null,
    val isGuest: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val currentTopic:TopicClass= TopicClass()
)
