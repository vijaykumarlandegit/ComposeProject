package com.example.composeproject.data.repository

import com.example.composeproject.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FetchUserRepo @Inject constructor(
    private val fireStore:FirebaseFirestore,
    private val auth:FirebaseAuth
) {
    val userId=auth.currentUser?.uid
    suspend fun fetchUser(): User ?{
        return try {
           val snapshot= fireStore.collection("users")
                .document(userId?:"")
                .get()
                .await()
            snapshot.toObject(User::class.java)

        }catch (e:Exception){
            null
        }

    }
}