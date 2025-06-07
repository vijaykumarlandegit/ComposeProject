package com.example.composeproject.data.repository

import com.example.composeproject.data.model.ReminderClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReminderRepo @Inject constructor(private val fireStore:FirebaseFirestore,private val auth:FirebaseAuth) {

    val userId=auth.currentUser?.uid
    suspend fun setReminder(data:ReminderClass):Boolean{
        return try {
            if (userId.isNullOrEmpty()) return false
            val ref=fireStore.collection("users")
                .document(userId?:"")
                .collection("reminder")
                .document()
            val reminderId=ref.id
            val newData=data.copy(reminderId=reminderId)
            ref.set(newData).await()
            true
        }catch (e:Exception){
            false
        }
    }
}