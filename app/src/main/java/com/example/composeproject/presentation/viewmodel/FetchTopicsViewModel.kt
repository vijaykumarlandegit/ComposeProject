package com.example.composeproject.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeproject.data.model.DateSummary
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.data.repository.FetchTopicsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FetchTopicsViewModel @Inject constructor(
    private val repo: FetchTopicsRepo
) : ViewModel() {

    var topicList by mutableStateOf<List<TopicClass>>(emptyList())
        private set

    var groupedByDate by mutableStateOf<List<DateSummary>>(emptyList())
        private set

    fun getTopicsListFromFireStore(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = repo.getTopicsListFromFireStore(userId)
            topicList = newList

            // Grouping by date and preparing summary
            val grouped = newList.groupBy { it.date }
                .map { (date, topicsForDate) ->
                    val totalMinutes = topicsForDate.sumOf {
                        ((it.endTime?.seconds ?: 0) - (it.startTime?.seconds ?: 0)).toInt() / 60
                    }
                    DateSummary(
                        date = date,
                        totalTopics = topicsForDate.size,
                        totalMinutes = totalMinutes
                    )
                }

            groupedByDate = grouped
        }
    }
}
