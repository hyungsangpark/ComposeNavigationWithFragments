package com.letterboxd.composenavigationwithfragments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavController
import com.letterboxd.composenavigationwithfragments.databinding.FragmentTestBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
        /** As far as I'm aware, this is the only way to insert fragments in composable. */
fun PopularScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    AndroidViewBinding(FragmentTestBinding::inflate) {
        this.label.text = "Changed text"
        this.button.setOnClickListener {
            scope.launch {
                navController.navigate("film/ae15ew")
//                snackbarHostState.showSnackbar("from fragment button!")
            }
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .background(Color.Green)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Search Screen in level", modifier = Modifier.clickable {
            navController.navigate("search")
        })
    }
}

@Composable
fun ActivityScreen() {
    Box(
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Activity Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .background(Color.Blue)
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Profile Screen")
    }
}

@Composable
fun FilmScreen(navController: NavController, id: String) {
    Surface {
        Column {
            Text(text = "Film with id: $id")
            Button(onClick = {
                navController.navigate("film/${id}z")
            }) {
                Text("Click me to advance in further.")
            }
        }
    }
}
