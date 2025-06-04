package com.example.composeproject.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
 import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.composeproject.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onResult: (Boolean) -> Unit) {
    LaunchedEffect(true) {
        delay(2000)
        val isUserSignedIn = Firebase.auth.currentUser != null
        onResult(isUserSignedIn)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
           // .background(Color(0xFF1976D2))
        , // Blue background
        contentAlignment = Alignment.Center
    ) {
        Image(
           painter = painterResource(R.drawable.study_tracker_logo),
            contentDescription = "StudyTracker Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}
