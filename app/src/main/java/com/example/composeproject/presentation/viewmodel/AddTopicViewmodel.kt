package com.example.composeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.AddTopicDataClass
import com.example.composeproject.data.repository.AddTopicRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AddTopicViewmodel @Inject constructor(private val repo:AddTopicRepo):ViewModel() {

    fun uploadTopic(topic: AddTopicDataClass, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.uploadTopicToFirebase(topic) // suspend fun
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

}