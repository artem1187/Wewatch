package com.example.wewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wewatch.presentation.add.AddScreen
import com.example.wewatch.presentation.add.AddViewModel
import com.example.wewatch.presentation.main.MainScreen
import com.example.wewatch.presentation.main.MainViewModel
import com.example.wewatch.ui.theme.WewatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WewatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeWatchApp()
                }
            }
        }
    }
}

@Composable
fun WeWatchApp() {
    val navController = rememberNavController()

    val mainViewModel: MainViewModel = hiltViewModel()
    val addViewModel: AddViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                viewModel = mainViewModel,
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToDetail = { filmId ->
                    navController.navigate("detail/$filmId")
                }
            )
        }

        composable("add") {
            AddScreen(
                viewModel = addViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSearch = { query, year ->
                    navController.navigate("search/$query/$year")
                }
            )
        }

        composable("search/{query}/{year}") { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""

            // SearchScreen будет добавлен позже
            // Пока заглушка
            androidx.compose.material3.Text("Search Screen: $query")
        }

        composable("detail/{filmId}") { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")?.toIntOrNull() ?: 0

            // DetailScreen будет добавлен позже
            androidx.compose.material3.Text("Detail Screen: $filmId")
        }
    }
}