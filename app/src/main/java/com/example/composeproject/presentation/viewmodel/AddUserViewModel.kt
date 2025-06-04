package com.example.composeproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.User
import com.example.composeproject.data.repository.AddUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddUserViewModel @Inject constructor(private val repo: AddUserRepo): ViewModel(){

     fun addUser(userId: String, user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addUser(userId,user)
        }
    }
}