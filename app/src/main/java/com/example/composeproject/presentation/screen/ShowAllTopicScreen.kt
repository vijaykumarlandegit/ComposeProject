package com.example.composeproject.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.presentation.viewmodel.FetchTopicsViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ShowAllTopicScreen(
    navController: NavHostController,
    viewModel: FetchTopicsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getTopicsListFromFireStore("123")
    }
    val topicList = viewModel.topicList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(topicList) { item ->
                TopicItemCard(topic = item)
            }
        }
    }
}

@Composable
fun TopicItemCard(topic: TopicClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Title
            Text(
                text = topic.topicTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subject tag
            Text(
                text = topic.subject,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF3F51B5), shape = RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Description
            Text(
                text = topic.topicDescription,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date & Time row
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                topic.date.takeIf { it.isNotBlank() }?.let {
                    Text(text = "Date: $it", style = MaterialTheme.typography.labelSmall)
                }
                topic.startTime?.let {
                    val timeText = "From: ${formatTime(it)}"
                    Text(text = timeText, style = MaterialTheme.typography.labelSmall)
                }
                topic.endTime?.let {
                    val timeText = "To: ${formatTime(it)}"
                    Text(text = timeText, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun formatTime(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}
