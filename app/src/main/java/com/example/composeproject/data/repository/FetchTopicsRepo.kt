package com.example.composeproject.data.repository

import com.example.composeproject.data.model.TopicClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FetchTopicsRepo @Inject constructor( private val firebase:FirebaseFirestore) {

    suspend fun getTopicsListFromFireStore(userId:String): List<TopicClass>{
        return try {
          val snapshot= firebase.collection("users")
                .document(userId)
                .collection("all_topics")
                .get().await()
            snapshot.toObjects(TopicClass::class.java)
        }catch (e:Exception){
            emptyList<TopicClass>()
        }
    }
}