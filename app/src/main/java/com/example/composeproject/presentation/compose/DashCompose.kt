package com.example.composeproject.presentation.compose

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.composeproject.data.model.DateSummary
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.presentation.viewmodel.AddEndTimeViewModel
import com.example.composeproject.presentation.viewmodel.FetchTopicsViewModel
import com.example.composeproject.presentation.viewmodel.FetchUserViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dashCompose(
    navController: NavHostController,
    userViewModel:FetchUserViewModel= hiltViewModel(),
    addEndTimeViewModel:AddEndTimeViewModel= hiltViewModel(),
    viewModel: FetchTopicsViewModel = hiltViewModel(),
 ) {
    val userId=FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        userViewModel.fetchUser()
        if (userId != null) {
            viewModel.getTopicsListFromFireStore()
        }
        viewModel.getTopicsByDate(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()))
    }


    val groupedByDate = viewModel.groupedByDate
    val topic=viewModel.topicList

    var user= userViewModel.user
    var currentTopic=user?.currentTopic
      val totalStudyMinutesToday = groupedByDate.sumOf { it.totalTopics }
    val todayTopicsCount = groupedByDate.sumOf { it.totalMinutes }

    val totalDayMinutes = 24 * 60f
    val targetFraction = totalStudyMinutesToday / totalDayMinutes
    var selectedUTCTime by remember { mutableStateOf<Timestamp?>(null) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }
    val context=LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 1000),
        label = "AnimatedStudyFraction"
    )

    val pieData = listOf(
        animatedFraction to Color(0xFF4CAF50),
        (1f - animatedFraction) to Color(0xFFBDBDBD)
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Pie Chart on the left
                Box(
                    modifier = Modifier
                        .size(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        var startAngle = -90f
                        pieData.forEach { (fraction, color) ->
                            val sweepAngle = 360f * fraction
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = true
                            )
                            startAngle += sweepAngle
                        }
                    }

                    Text(
                        text = "${(animatedFraction * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }


                // Info on the right
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "Today's Topics: $todayTopicsCount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Studied: ${totalStudyMinutesToday / 60}h ${totalStudyMinutesToday % 60}m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            val maxMinutes = 1440f // total minutes in 24 hours

            val colorForMinutes: (Int) -> Color = { minutes ->
                when {
                    minutes >= 960 -> Color(0xFF2E7D32) // Dark Green
                    minutes >= 601 -> Color(0xFF4CAF50) // Green
                    minutes >= 361 -> Color(0xFFFFC107) // Yellow
                    minutes >= 181 -> Color(0xFFFF9800) // Orange
                    else -> Color(0xFFF44336) // Red
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                reverseLayout = true // Reverse scroll direction
            ) {
                items(topic.reversed()) { topicItem ->
                    val date = topicItem.date
                    val minutes = if (topicItem.startTime != null && topicItem.endTime != null) {
                        ((topicItem.endTime.seconds - topicItem.startTime.seconds) / 60).toInt()
                    } else 0

                    val hours = minutes / 60
                    val remainingMinutes = minutes % 60
                    val formattedTime = when {
                        hours > 0 && remainingMinutes > 0 -> "${hours} hr ${remainingMinutes} min"
                        hours > 0 -> "${hours} hr"
                        else -> "${remainingMinutes} min"
                    }

                    val barHeightRatio = minutes / maxMinutes.toFloat()
                    val color = colorForMinutes(minutes)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.height(200.dp)
                    ) {
                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .width(40.dp) // wider bar for longer text
                                .background(
                                    Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(6.dp)
                                ),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(fraction = barHeightRatio)
                                    .background(color = color, shape = RoundedCornerShape(6.dp))
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = date,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        if(!currentTopic?.topicId.isNullOrEmpty()){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Current Running Topic", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Title: ${currentTopic?.topicTitle}", style = MaterialTheme.typography.bodyMedium)
                    Text("Subject: ${currentTopic?.subject}", style = MaterialTheme.typography.bodyMedium)
                    Text("Description: ${currentTopic?.topicDescription}", style = MaterialTheme.typography.bodySmall)
                    Text("Start Time: ${currentTopic?.startTime?.toDate()?.toString() ?: "Not Available"}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(16.dp))


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                            .clickable { showTimePicker = true }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Select time",
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Select time"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val updatedTopic = currentTopic?.copy(
                                endTime = selectedUTCTime
                            )
                            if (updatedTopic != null) {
                                addEndTimeViewModel.uploadEndTime(updatedTopic) { isSuccess ->
                                    Toast.makeText(context, if (isSuccess) "Updated Successfully!!" else "Something is wrong", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        enabled = selectedTime != null,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Submit")
                    }
                }
            }
        }else if(currentTopic?.topicId.isNullOrEmpty()){

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Add New Topic", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(" You can add new topic by clicking on button", style = MaterialTheme.typography.bodyMedium)

                }
            }
        }




        LaunchedEffect(showTimePicker) {
            if (showTimePicker) {
                val currentTime = LocalTime.now()
                TimePickerDialog(
                    context,
                    { _, hour: Int, minute: Int ->
                        selectedTime = LocalTime.of(hour, minute)
                        val selectedDateTime = LocalDateTime.of(LocalDate.now(), selectedTime)
                        selectedUTCTime = Timestamp(Date.from(selectedDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                        showTimePicker = false
                    },
                    currentTime.hour,
                    currentTime.minute,
                    false
                ).show()
            }
        }
    }
}



