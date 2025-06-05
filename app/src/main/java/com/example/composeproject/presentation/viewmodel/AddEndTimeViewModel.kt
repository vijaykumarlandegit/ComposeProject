package com.example.composeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.data.repository.AddEndTimeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AddEndTimeViewModel @Inject constructor(private val repo:AddEndTimeRepo):ViewModel() {

    fun uploadEndTime(topic:TopicClass, isSuccess: (Boolean)-> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result=repo.addEndTime(topic)
            withContext(Dispatchers.Main){
                isSuccess(result)
            }

        }
    }
}