package com.example.wewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wewatch.presentation.add.AddIntent
import com.example.wewatch.presentation.add.AddScreen
import com.example.wewatch.presentation.add.AddViewModel
import com.example.wewatch.presentation.detail.DetailScreen
import com.example.wewatch.presentation.detail.DetailViewModel
import com.example.wewatch.presentation.main.MainScreen
import com.example.wewatch.presentation.main.MainViewModel
import com.example.wewatch.presentation.search.SearchScreen
import com.example.wewatch.presentation.search.SearchViewModel
import com.example.wewatch.theme.WeWatchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeWatchTheme {
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
    val searchViewModel: SearchViewModel = hiltViewModel()
    val detailViewModel: DetailViewModel = hiltViewModel()

    val mainState by mainViewModel.state.collectAsState()

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

            SearchScreen(
                viewModel = searchViewModel,
                query = query,
                year = year,
                onNavigateBack = { navController.popBackStack() },
                onFilmSelected = { film ->
                    addViewModel.handleIntent(AddIntent.SelectFilm(film))
                    navController.popBackStack()
                }
            )
        }

        composable("detail/{filmId}") { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId")?.toIntOrNull() ?: 0

            val film = mainState.films.find { it.id == filmId }

            if (film != null) {
                DetailScreen(
                    viewModel = detailViewModel,
                    film = film,
                    onNavigateBack = { navController.popBackStack() }
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}