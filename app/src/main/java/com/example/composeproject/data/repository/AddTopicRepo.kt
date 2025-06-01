package com.example.composeproject.data.repository

import com.example.composeproject.data.model.AddTopicDataClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddTopicRepo @Inject constructor(private val firestore: FirebaseFirestore) {
   suspend fun uploadTopicToFirebase(topic:AddTopicDataClass):Boolean{
        return try {
            firestore.collection("Topic")
                .add(topic)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}