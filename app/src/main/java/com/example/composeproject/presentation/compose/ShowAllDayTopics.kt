package com.example.composeproject.presentation.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.presentation.viewmodel.FetchTopicsViewModel

@Composable
fun ShowAllDayTopics(viewModel: FetchTopicsViewModel = hiltViewModel(), userId: String) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getTopicsListFromFireStore(userId)
    }
    val topicsList = viewModel.topicList
    val totalTopics=topicsList.size
    LazyColumn {
        items(items = topicsList) { topic -> // ✅ correct

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(10.dp)


            ) {
                Row(
                    modifier = Modifier
                         .padding(8.dp),
                ) {
                    Text(
                        text = topic.date,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                     Text(
                              text="$totalTopics",
                             fontSize = 20.sp,
                             fontWeight = FontWeight.Bold
                      )
                }
            }

        }

    }


}