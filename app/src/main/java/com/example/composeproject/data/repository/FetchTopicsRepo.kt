package com.example.composeproject.data.repository

import com.example.composeproject.data.model.TopicClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FetchTopicsRepo @Inject constructor(
    private val firebase:FirebaseFirestore,
    private val auth:FirebaseAuth
) {

    val userId=auth.currentUser?.uid
    suspend fun getTopicsListFromFireStore(): List<TopicClass> {
        val uid = userId ?: return emptyList() // Return early if null
        return try {
            val snapshot = firebase.collection("users")
                .document(uid)
                .collection("all_topics")
                .get()
                .await()
            snapshot.toObjects(TopicClass::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

}