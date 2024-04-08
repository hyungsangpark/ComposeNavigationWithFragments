package com.letterboxd.composenavigationwithfragments

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.letterboxd.composenavigationwithfragments.ui.theme.ComposeNavigationWithFragmentsTheme
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val icon: ImageVector) {
    data object Popular : Screen("popular", Icons.Rounded.Star)
    data object Search : Screen("Search", Icons.Rounded.Search)
    data object New : Screen("new", Icons.Filled.AddCircle)
    data object Activity : Screen("activity", Icons.Filled.FavoriteBorder)
    data object Profile : Screen("profile", Icons.Rounded.Person)
    data object Film : Screen("film/{id}", Icons.Rounded.Person)
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

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { screen ->
                when (screen) {
                    Screen.New -> {
                        NavigationBarItem(icon = { Icon(screen.icon, contentDescription = null) },
                            selected = false,
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("New message clicked")
                                }
                            })
                    }

                    else -> {
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // re-selecting the same item
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected item
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
        NavHost(navController = navController,
            startDestination = Screen.Popular.route,
            modifier = Modifier.padding(innerPadding).fillMaxSize().border(1.dp, Color.Red),
//            enterTransition = {
//                EnterTransition.None
//            },
//            exitTransition = {
//                ExitTransition.None
//            }
        ) {
            composable(Screen.Popular.route) { PopularScreen(navController, snackbarHostState) }
            composable(Screen.Search.route) { SearchScreen(navController) }
            composable(Screen.Activity.route) { ActivityScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(
                Screen.Film.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                    ?: error("Film ID not passed on film screen.")
                FilmScreen(navController, id)
            }
        }
    }
}

@Composable
fun NavBar() {

}