package com.example.composeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.ReminderClass
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
}