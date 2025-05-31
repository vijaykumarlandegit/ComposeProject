package com.example.composeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeproject.ui.theme.ComposeProjectTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.google.accompanist.systemuicontroller.rememberSystemUiController

import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            ComposeProjectTheme {
                SimpleScaffold()
            }
        }
    }

    @Preview
    @Composable
    fun SimpleScaffold() {

        val navController = rememberNavController()
        SetSystemUi()
        Scaffold(

            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Simple Scaffold",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /* TODO: Handle back or drawer */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Handle search */ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        IconButton(onClick = { /* TODO: Handle more options */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },

                    )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Action here */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            },
            bottomBar = {
                BottomBar(navController)
            }

        ) { padding ->
            // Main content area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                NavigationGraph(navController)
            }
        }
    }

     @Composable
    fun BottomBar(navController: NavController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Profile,
            BottomNavItem.Settings
        )

        BottomNavigation(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ) {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination

            items.forEach { item ->
                BottomNavigationItem(
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
        object Profile : BottomNavItem("Profile", Icons.Default.Person, "profile")
        object Settings : BottomNavItem("Settings", Icons.Default.Settings, "settings")
    }

    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = "home") {
            composable("home") { Text("Home Screen") }
            composable("profile") { Text("Profile Screen") }
            composable("settings") { Text("Settings Screen") }
        }
    }
    @Composable
    fun SetSystemUi() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme() // ✅ correct way

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
        }
    }


}


