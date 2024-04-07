package com.letterboxd.composenavigationwithfragments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.letterboxd.composenavigationwithfragments.ui.theme.ComposeNavigationWithFragmentsTheme
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val icon: ImageVector) {
    data object Popular : Screen("popular", Icons.Rounded.Star)
    data object Search : Screen("Search", Icons.Rounded.Search)
    data object New : Screen("new", Icons.Filled.AddCircle)
    data object Activity : Screen("activity", Icons.Filled.FavoriteBorder)
    data object Profile : Screen("profile", Icons.Rounded.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigationWithFragmentsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val items = listOf(Screen.Popular, Screen.Search, Screen.New, Screen.Activity, Screen.Profile)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    when (screen) {
                        Screen.New -> {
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("New message clicked")
                                    }
                                }
                            )
                        }

                        else -> {
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                //                    label = { Text(text = screen.route) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }) { innerPadding ->
        // A surface container using the 'background' color from the theme
        NavHost(
            navController = navController,
            startDestination = Screen.Popular.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            }
        ) {
            composable(Screen.Popular.route) {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Popular Screen")
                }
            }

            composable(Screen.Search.route) {
                Box(
                    modifier = Modifier
                        .background(Color.Green)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Search Screen")
                }
            }

//            composable(Screen.New.route) {
//                Box(
//                    modifier = Modifier.background(Color.Red).fillMaxSize(),
//                    contentAlignment = Alignment.Center,
//                ) {
//                    Text(text = "New Screen")
//                }
//            }

            composable(Screen.Activity.route) {
                Box(
                    modifier = Modifier
                        .background(Color.Cyan)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Activity Screen")
                }
            }

            composable(Screen.Profile.route) {
                Box(
                    modifier = Modifier
                        .background(Color.Blue)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Profile Screen")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeNavigationWithFragmentsTheme {
        Navigation()
    }
}