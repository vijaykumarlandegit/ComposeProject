package com.example.composeproject.data.repository

import android.util.Log
import com.example.composeproject.data.model.TopicClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddTopicRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

) {
    private val userId = auth.currentUser?.uid
   suspend fun uploadTopicToFirebase(topic: TopicClass):Boolean{
     return  try {
           firestore.collection("users")
               .document(userId?:"")
               .update("currentTopic",topic)
               .await()
           true

       } catch (e: Exception) {
           Log.e("AddUserRepo", "Firestore error: ${e.message}")
         false
       }


    }
}