package com.example.composeproject.presentation.compose

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.composeproject.data.model.DateSummary
import com.example.composeproject.data.model.TopicClass
import com.google.firebase.Timestamp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun dashCompose(
    navController: NavHostController,
    userId: String?
) {

    val totalStudyMinutesToday = 150
    val todayTopicsCount = 5 // Example, you can fetch this dynamically later

    val totalDayMinutes = 24 * 60f
    val targetFraction = totalStudyMinutesToday / totalDayMinutes

    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 1000),
        label = "AnimatedStudyFraction"
    )

    val pieData = listOf(
        animatedFraction to Color(0xFF4CAF50),
        (1f - animatedFraction) to Color(0xFFBDBDBD)
    )
    val sampleData = listOf(
        "Jun 1" to 80,
        "Jun 2" to 150,
        "Jun 3" to 324,
        "Jun 5" to 850,
        "Jun 6" to 1056,
        "Jun 7" to 1440,
        "Jun 11" to 326,
        "Jun 14" to 600,
        "Jun 15" to 862,
        "Jun 16" to 1007,
        "Jun 21" to 1440
    )
    val topic = TopicClass(
        topicId = "1",
        topicTitle = "Compose Basics",
        topicDescription = "Learning how Jetpack Compose works",
        startTime = Timestamp.now(),
        date = "June 4, 2025",
        subject = "Android Development"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                items(sampleData.reversed()) { (date, minutes) -> // Reverse data order
                    val hours = minutes / 60
                    val remainingMinutes = minutes % 60
                    val formattedTime = when {
                        hours > 0 && remainingMinutes > 0 -> "${hours} hr ${remainingMinutes} min"
                        hours > 0 -> "${hours} hr"
                        else -> "${remainingMinutes} min"
                    }

                    val barHeightRatio = minutes / maxMinutes
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
        var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
        var showTimePicker by remember { mutableStateOf(false) }
        val context=LocalContext.current
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

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

                Text("Title: ${topic.topicTitle}", style = MaterialTheme.typography.bodyMedium)
                Text("Subject: ${topic.subject}", style = MaterialTheme.typography.bodyMedium)
                Text("Description: ${topic.topicDescription}", style = MaterialTheme.typography.bodySmall)
                Text("Start Time: ${topic.startTime?.toDate()?.toString() ?: "Not Available"}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedTime?.format(timeFormatter) ?: "",
                    onValueChange = {},
                    label = { Text("Select End Time") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Pick Time")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedTime?.let { time ->
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                            calendar.set(Calendar.MINUTE, time.minute)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            val endTimestamp = Timestamp(calendar.time)

                            Toast.makeText(context,"$endTimestamp",Toast.LENGTH_LONG).show()
                        }
                    },
                    enabled = selectedTime != null,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit")
                }
            }
        }

        if (showTimePicker) {
            val context = LocalContext.current
            val currentTime = LocalTime.now()

            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    selectedTime = LocalTime.of(hour, minute)
                    showTimePicker = false
                },
                currentTime.hour,
                currentTime.minute,
                false
            ).show()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun dashCompose(

) {
    val totalStudyMinutesToday = 150
    val todayTopicsCount = 5 // Example, you can fetch this dynamically later

    val totalDayMinutes = 24 * 60f
    val targetFraction = totalStudyMinutesToday / totalDayMinutes

    val animatedFraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 1000),
        label = "AnimatedStudyFraction"
    )

    val pieData = listOf(
        animatedFraction to Color(0xFF4CAF50),
        (1f - animatedFraction) to Color(0xFFBDBDBD)
    )
    val sampleData = listOf(
        "Jun 1" to 80,
        "Jun 2" to 150,
        "Jun 3" to 324,
         "Jun 5" to 850,
        "Jun 6" to 1056,
        "Jun 7" to 1440,
        "Jun 11" to 326,
        "Jun 14" to 600,
        "Jun 15" to 862,
        "Jun 16" to 1007,
        "Jun 21" to 1440
    )

    val topic = TopicClass(
        topicId = "1",
        topicTitle = "Compose Basics",
        topicDescription = "Learning how Jetpack Compose works",
        startTime = Timestamp.now(),
        date = "June 4, 2025",
        subject = "Android Development"
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        )  {
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
                items(sampleData.reversed()) { (date, minutes) -> // Reverse data order
                    val hours = minutes / 60
                    val remainingMinutes = minutes % 60
                    val formattedTime = when {
                        hours > 0 && remainingMinutes > 0 -> "${hours} hr ${remainingMinutes} min"
                        hours > 0 -> "${hours} hr"
                        else -> "${remainingMinutes} min"
                    }

                    val barHeightRatio = minutes / maxMinutes
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
        var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
        var showTimePicker by remember { mutableStateOf(false) }
        val context=LocalContext.current
        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

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

                Text("Title: ${topic.topicTitle}", style = MaterialTheme.typography.bodyMedium)
                Text("Subject: ${topic.subject}", style = MaterialTheme.typography.bodyMedium)
                Text("Description: ${topic.topicDescription}", style = MaterialTheme.typography.bodySmall)
                Text("Start Time: ${topic.startTime?.toDate()?.toString() ?: "Not Available"}", style = MaterialTheme.typography.bodySmall)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedTime?.format(timeFormatter) ?: "",
                    onValueChange = {},
                    label = { Text("Select End Time") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true },
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Pick Time")
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        selectedTime?.let { time ->
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, time.hour)
                            calendar.set(Calendar.MINUTE, time.minute)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            val endTimestamp = Timestamp(calendar.time)

                            Toast.makeText(context,"$endTimestamp",Toast.LENGTH_LONG).show()
                         }
                    },
                    enabled = selectedTime != null,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Submit")
                }
            }
        }

        if (showTimePicker) {
            val context = LocalContext.current
            val currentTime = LocalTime.now()

            TimePickerDialog(
                context,
                { _, hour: Int, minute: Int ->
                    selectedTime = LocalTime.of(hour, minute)
                    showTimePicker = false
                },
                currentTime.hour,
                currentTime.minute,
                false
            ).show()
        }
    }
}


