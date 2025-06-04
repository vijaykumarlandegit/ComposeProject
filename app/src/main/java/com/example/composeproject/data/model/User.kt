package com.example.composeproject.data.model

data class User(
    val uid: String = "",
    val email: String? = null,
    val isGuest: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
