package com.example.composeproject.data.repository

import com.example.composeproject.data.model.TopicClass
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddEndTimeRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

     private val userId = auth.currentUser?.uid
     suspend fun addEndTime(topicClass: TopicClass): Boolean {

         return try {
             if (!userId.isNullOrEmpty()) {
                 val ref=  firestore.collection("users")
                     .document(userId?:"")
                     .collection("all_topics")
                     .document()
                 val topicWithId = topicClass.copy(topicId = ref.id)
                 ref.set(topicWithId).await()
                 updateUser()
             }
             true
         } catch (e: Exception) {
             false
         }

    }
    private fun updateUser(){
        if (!userId.isNullOrEmpty()) {
            firestore.collection("users")
                .document(userId)
                .update("currentTopic", TopicClass())
        }
    }


}