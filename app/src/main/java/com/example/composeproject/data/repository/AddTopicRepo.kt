package com.example.composeproject.data.repository

import com.example.composeproject.data.model.TopicClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AddTopicRepo @Inject constructor(private val firestore: FirebaseFirestore) {
   suspend fun uploadTopicToFirebase(topic: TopicClass):Boolean{
        return try {
          val ref=  firestore.collection("users")
                .document("123")
                .collection("all_topics")
                .document()
            val topicWithId = topic.copy(topicId = ref.id)

            ref.set(topicWithId).await()

            true
        } catch (e: Exception) {
            false
        }
    }
}