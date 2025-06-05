package com.example.composeproject.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.User
import com.example.composeproject.data.repository.FetchUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchUserViewModel @Inject constructor(private val repo:FetchUserRepo): ViewModel(){

     var user by mutableStateOf<User?>(null)
    private  set
    fun fetchUser() {
        viewModelScope.launch(Dispatchers.IO) {
           user=repo.fetchUser()

        }


    }
}