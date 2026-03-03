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
import com.example.wewatch.presentation.screens.AddScreen
import com.example.wewatch.presentation.screens.MainScreen
import com.example.wewatch.presentation.screens.SearchScreen
import com.example.wewatch.presentation.viewmodel.AddViewModel
import com.example.wewatch.presentation.viewmodel.MainViewModel
import com.example.wewatch.presentation.viewmodel.SearchViewModel


@Composable
fun SetupNavGraph(
    mainViewModel: MainViewModel,
    addViewModel: AddViewModel,
    searchViewModel: SearchViewModel
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

        // Экран поиска с параметрами
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
                    // Возвращаемся на AddScreen
                    navController.popBackStack()
                }
            )
        }
    }
}