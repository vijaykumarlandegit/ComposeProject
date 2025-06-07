package com.example.composeproject.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.ReminderClass
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.data.repository.ReminderRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ReminderViewModel @Inject constructor(private val repo:ReminderRepo):ViewModel(){

    fun setReminder(data:ReminderClass, isSet: (Boolean)->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            repo.setReminder(data)
            withContext(Dispatchers.Main){
                isSet(true)
            }
        }
    }

    var reminderList by mutableStateOf<List<ReminderClass>>(emptyList())
        private set  // ✅ Prevent external modification

    init {
        getReminderList()  // ✅ Automatically called once when ViewModel is created
    }

    fun getReminderList() {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = repo.getReminderList()
            reminderList = newList
            Log.d("rem", "reminderrrrVVV: $reminderList")
        }
    }
}