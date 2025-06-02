package com.example.composeproject.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.data.repository.FetchTopicsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FetchTopicsViewModel @Inject constructor(private val repo:FetchTopicsRepo):ViewModel() {

    var topicList by mutableStateOf<List<TopicClass>>(emptyList())
    private set

    fun getTopicsListFromFireStore(userId:String){
        viewModelScope.launch(Dispatchers.IO) {
           val newList= repo.getTopicsListFromFireStore(userId)
             topicList=newList
        }
    }
}