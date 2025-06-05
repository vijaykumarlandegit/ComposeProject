package com.example.composeproject.presentation.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.presentation.viewmodel.AddTopicViewmodel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BottomSheetContent(addTopicViewmodel: AddTopicViewmodel, context: Context) {

    var subjectName by remember { mutableStateOf("") }
    var topicName by remember { mutableStateOf("") }
    var topicDes by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add Study Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add Study Topic and Select Time When You Start And End.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                label = { Text("Subject Name") },
                placeholder = { Text("Enter subject name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            OutlinedTextField(
                value = topicName,
                onValueChange = { topicName = it },
                label = { Text("Topic Name") },
                placeholder = { Text("Enter topic title") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            OutlinedTextField(
                value = topicDes,
                onValueChange = { topicDes = it },
                label = { Text("Topic Description") },
                placeholder = { Text("Write something about the topic...") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                maxLines = 6
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp) // inner padding inside the border
            ) {
                Text(text = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(Date()))
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if(topicName.isNotEmpty() && topicDes.isNotEmpty()) {
                        val topic = TopicClass(
                            "current",
                            topicName,
                            topicDes,
                            Timestamp.now(),
                            endTime = null,
                            Timestamp.now(),
                             date= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
                            subject = subjectName
                        )
                        addTopicViewmodel.uploadTopic(topic) { isSuccess ->
                            if (isSuccess) {
                                topicName = ""
                                topicDes = ""
                                subjectName=""
                                Toast.makeText(context, "Topic uploaded Successfully!!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Something is wrong", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Fill all fields", Toast.LENGTH_LONG).show()
                    }
                }
                ,
                modifier = Modifier.fillMaxWidth()
            ) {
                 Text("Upload")
            }

        }


    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomSheetContent() {

    val currentDateTime = remember {
        SimpleDateFormat("yyyy-MM-dd | hh:mm a", Locale.getDefault()).format(Date())
    }
    var subjectName by remember { mutableStateOf("") }
    var topicName by remember { mutableStateOf("") }
    var topicDes by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add Study Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Add Study Topic and Select Time When You Start And End.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )


        Column(modifier = Modifier.padding(16.dp)) {

            // Topic Name Field
            OutlinedTextField(
                value = subjectName,
                onValueChange = { subjectName = it },
                label = { Text("Subject Name") },
                placeholder = { Text("Enter subject name") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
             OutlinedTextField(
                value = topicName,
                onValueChange = { topicName = it },
                label = { Text("Topic Name") },
                placeholder = { Text("Enter topic title") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            // Topic Description Field
            OutlinedTextField(
                value = topicDes,
                onValueChange = { topicDes = it },
                label = { Text("Topic Description") },
                placeholder = { Text("Write something about the topic...") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                maxLines = 6
            )
            Spacer(modifier = Modifier.height(20.dp))


            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(12.dp) // inner padding inside the border
            ) {
                Text(text = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(Date()))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                }
                ,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Upload")
            }

        }


    }
}
