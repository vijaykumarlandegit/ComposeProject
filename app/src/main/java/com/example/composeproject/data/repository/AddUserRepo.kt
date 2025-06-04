package com.example.composeproject.data.repository

import android.util.Log
import com.example.composeproject.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddUserRepo @Inject constructor(private val fireStore:FirebaseFirestore) {

    suspend fun addUser(userId: String, user: User) {
        try {
            fireStore.collection("users")
                .document(userId)
                .set(user)
                .await()  // ✅ waits for completion
        } catch (e: Exception) {
            Log.e("AddUserRepo", "Firestore error: ${e.message}")
        }
    }
}