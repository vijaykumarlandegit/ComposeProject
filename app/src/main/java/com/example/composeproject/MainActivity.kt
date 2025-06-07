package com.example.composeproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import androidx.navigation.NavHostController
import androidx.navigation.compose.*

import com.example.composeproject.data.alarm.scheduleReminders
import com.example.composeproject.presentation.compose.ShowAllDayTopics
import com.example.composeproject.presentation.compose.dashCompose
import com.example.composeproject.presentation.screen.MainAuthScreen
import com.example.composeproject.presentation.screen.ShowAllTopicScreen
import com.example.composeproject.presentation.screen.SplashScreen
import com.example.composeproject.presentation.viewmodel.ReminderViewModel
import com.example.composeproject.ui.theme.ComposeProjectTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        FirebaseApp.initializeApp(this)




        setContent {
            var showSplash by remember { mutableStateOf(true) }
            var isUserSignedIn by remember { mutableStateOf(false) }
            val navController = rememberNavController()


            ComposeProjectTheme {
                when {
                    showSplash -> {
                        SplashScreen { isSignedIn ->
                            isUserSignedIn = isSignedIn
                            showSplash = false
                        }
                    }
                    isUserSignedIn -> {
                        SimpleScaffold(navController)
                    }
                    else -> {
                        MainAuthScreen(
                            navController = navController,
                            onLoginSuccess = {
                                isUserSignedIn = true // Show Scaffold after login
                            }
                        )
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleScaffold(navController: NavHostController, reminderViewModel: ReminderViewModel= hiltViewModel()) {
         val context = LocalContext.current
        val reminders = reminderViewModel.reminderList

        LaunchedEffect(reminders) {
            if (reminders.isNotEmpty()) {
                scheduleReminders( this@MainActivity,reminders)
            }
        }
        val hideScaffold = remember { mutableStateOf(true) }
        Scaffold(
            topBar = {
                if (hideScaffold.value){
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = "Study Tracker",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "More")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )

                    )
                }

            },

            bottomBar = {
                if (hideScaffold.value){
                    BottomBar(navController)
                }

            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NavigationGraph(navController=navController,hideScaffold = hideScaffold)
            }
        }

    }

    @Composable
    fun BottomBar(navController: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Work,
            BottomNavItem.Profile,
            BottomNavItem.Settings
        )
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        NavigationBar {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentDestination?.route == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
        object Home : BottomNavItem("Home", Icons.Default.Home, "home")
        object Work : BottomNavItem("Work", Icons.Default.Book, "work")
        object Profile : BottomNavItem("Profile", Icons.Default.Person, "profile")
        object Settings : BottomNavItem("Settings", Icons.Default.Settings, "settings")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavigationGraph(navController: NavHostController,hideScaffold:MutableState<Boolean>) {
        val userId=FirebaseAuth.getInstance().currentUser?.uid
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    hideScaffold.value=true
                    dashCompose(navController = navController)

                }
            }
            composable("work") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    hideScaffold.value=true
                    ShowAllDayTopics(navController = navController, userId = "123")

                }
            }
            composable("profile") { Text("Profile Screen") }
            composable("settings") { Text("Settings Screen") }

            composable("showTopics/{date}") { backStackEntry ->
                hideScaffold.value=false

                val date = backStackEntry.arguments?.getString("date")
                if (date != null) {
                    ShowAllTopicScreen(navController=navController,date = date)
                }
            }


        }
    }

    @Composable
    fun SetSystemUi() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }
}


