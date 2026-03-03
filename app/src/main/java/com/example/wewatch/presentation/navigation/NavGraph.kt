package com.example.wewatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.presentation.screens.AddScreen
import com.example.wewatch.presentation.screens.DetailScreen
import com.example.wewatch.presentation.screens.MainScreen
import com.example.wewatch.presentation.screens.SearchScreen
import com.example.wewatch.presentation.viewmodel.AddViewModel
import com.example.wewatch.presentation.viewmodel.DetailViewModel
import com.example.wewatch.presentation.viewmodel.MainViewModel
import com.example.wewatch.presentation.viewmodel.SearchViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun SetupNavGraph(
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel,
    searchViewModel: SearchViewModel,
    detailViewModel: DetailViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        // Главный экран
        composable("main") {
            MainScreen(
                viewModel = mainViewModel,
                onAddClick = {
                    navController.navigate("add")
                },
                onFilmClick = { film ->
                    if (film.id > 0) {
                        navController.navigate("detail/${film.id}")
                    } else {

                        println("Ошибка: film.id = 0")
                    }
                }
            )
        }

        // Экран добавления
        composable("add") {
            AddScreen(
                viewModel = addViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSearchClick = { query, year ->
                    navController.navigate("search/$query/$year")
                }
            )
        }

        // Экран поиска
        composable(
            "search/{query}/{year}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType },
                navArgument("year") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val year = backStackEntry.arguments?.getString("year") ?: ""

            SearchScreen(
                searchViewModel = searchViewModel,
                addViewModel = addViewModel,
                query = query,
                year = year,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onFilmSelected = {
                    navController.popBackStack()
                }
            )
        }

        // НОВЫЙ ЭКРАН: Детали фильма
        composable(
            "detail/{filmId}",
            arguments = listOf(
                navArgument("filmId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getInt("filmId") ?: 0

            // Получаем фильм из mainViewModel
            // В реальном проекте лучше передавать через аргументы
            val films by mainViewModel.films.collectAsState()
            val film = films.find { it.id == filmId }

            if (film != null) {
                DetailScreen(
                    film = film,
                    viewModel = detailViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}