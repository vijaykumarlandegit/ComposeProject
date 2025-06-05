package com.example.composeproject.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.composeproject.R
import com.example.composeproject.data.model.TopicClass
import com.example.composeproject.data.model.User
import com.example.composeproject.presentation.viewmodel.AddUserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun MainAuthScreen(
    navController: NavHostController,
    viewModel: AddUserViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit // Add callback
) {
    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.result
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Firebase.auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val user = User(
                        uid = userId,
                        email = userEmail,
                        isGuest = false
                    )
                    viewModel.addUser(userId, user)
                    onLoginSuccess() // Call callback
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.study_tracker_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Welcome to StudyTracker", style = MaterialTheme.typography.headlineMedium)
        Text("Track your daily study", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("33969016706-bpl0dv2m5m8670d7vojfvspqeeq2kgui.apps.googleusercontent.com") // from Firebase Console
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Person, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                Firebase.auth.signInAnonymously()
                    .addOnSuccessListener {
                        val userId=FirebaseAuth.getInstance().currentUser?.uid ?:""
                        val user = User(
                            uid = userId,
                            isGuest = true,
                            currentTopic = TopicClass(topicId="current")
                        )
                        viewModel.addUser(userId,user)
                        onLoginSuccess() // Call callback
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Guest sign-in failed", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Continue as Guest")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MainAuthScreen() {
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.study_tracker_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(140.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Welcome to StudyTracker", style = MaterialTheme.typography.headlineMedium)
        Text("Track your daily study", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Person, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Continue with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Continue as Guest")
        }
    }
}
