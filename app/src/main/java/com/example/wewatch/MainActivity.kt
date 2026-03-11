package com.example.wewatch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.ui.screens.MainScreen
import com.example.wewatch.ui.screens.AddScreen
import com.example.wewatch.ui.screens.SearchScreen
import com.example.wewatch.ui.screens.*
import com.example.wewatch.ui.theme.WewatchTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Прямое хранение состояния в Activity
    private lateinit var repository: FilmRepository
    private var films = mutableStateListOf<FilmEntity>()
    private var isLoading = mutableStateOf(false)

    // Состояние для AddScreen
    private var searchQuery = mutableStateOf("")
    private var searchYear = mutableStateOf("")
    private var selectedFilm = mutableStateOf<OmdbFilm?>(null)

    // Состояние для SearchScreen
    private var searchResults = mutableStateListOf<OmdbFilm>()
    private var isSearching = mutableStateOf(false)
    private var searchError = mutableStateOf<String?>(null)

    // Состояние для DetailScreen
    private var filmDetails = mutableStateOf<OmdbFilmDetails?>(null)
    private var isLoadingDetails = mutableStateOf(false)
    private var detailsError = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация
        val database = AppDatabase.getInstance(this)
        val apiKey = "bbf81799" // Вставьте ваш ключ
        repository = FilmRepository(database, apiKey)

        // Загрузка фильмов из БД
        loadFilmsFromDatabase()

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

    private fun loadFilmsFromDatabase() {
        lifecycleScope.launch {
            isLoading.value = true
            repository.getAllFilms().collect { filmList ->
                films.clear()
                films.addAll(filmList)
                isLoading.value = false
            }
        }
    }

    @Composable
    fun WeWatchApp() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainScreen(
                    films = films,
                    isLoading = isLoading.value,
                    onDeleteSelected = { deleteSelectedFilms() },
                    onToggleSelection = { film -> toggleFilmSelection(film) },
                    onAddClick = {
                        clearAddState()
                        navController.navigate("add")
                    },
                    onFilmClick = { film ->
                        film.imdbId?.let { loadFilmDetails(it) }
                        navController.navigate("detail/${film.id}")
                    }
                )
            }

            composable("add") {
                AddScreen(
                    searchQuery = searchQuery.value,
                    searchYear = searchYear.value,
                    selectedFilm = selectedFilm.value,  // Сюда придет выбранный фильм
                    onSearchQueryChange = { searchQuery.value = it },
                    onSearchYearChange = { searchYear.value = it },
                    onSearchClick = { query, year ->
                        searchFilms(query, year)
                        navController.navigate("search/$query/$year")
                    },
                    onAddFilm = {
                        // Здесь вызывается добавление в БД
                        selectedFilm.value?.let { film ->
                            addFilmToDatabase(film)
                            navController.popBackStack()  // Возврат на главный
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("search/{query}/{year}") { backStackEntry ->
                val query = backStackEntry.arguments?.getString("query") ?: ""
                val year = backStackEntry.arguments?.getString("year") ?: ""

                // Запускаем поиск при входе на экран
                LaunchedEffect(query, year) {
                    searchFilms(query, year)
                }

                SearchScreen(
                    searchResults = searchResults,
                    isSearching = isSearching.value,
                    errorMessage = searchError.value,
                    onFilmSelected = { film ->
                        // СОХРАНЯЕМ ВЫБРАННЫЙ ФИЛЬМ
                        selectedFilm.value = film
                        Log.d("WeWatch", "Выбран фильм: ${film.Title}")

                        // ВОЗВРАЩАЕМСЯ НА AddScreen (только один раз!)
                        navController.popBackStack()
                    },
                    onNavigateBack = {
                        // Просто возвращаемся назад
                        navController.popBackStack()
                    }
                )

            }

            composable("detail/{filmId}") { backStackEntry ->
                val filmId = backStackEntry.arguments?.getString("filmId")?.toIntOrNull() ?: 0
                val film = films.find { it.id == filmId }

                if (film != null) {
                    // Фильм найден - показываем детали
                    DetailScreen(
                        film = film,  // <-- Теперь film точно не null
                        details = filmDetails.value,
                        isLoading = isLoadingDetails.value,
                        errorMessage = detailsError.value,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    // Фильм не найден - возвращаемся назад
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }



    private fun deleteSelectedFilms() {
        lifecycleScope.launch {
            repository.deleteSelectedFilms()
        }
    }

    private fun toggleFilmSelection(film: FilmEntity) {
        lifecycleScope.launch {
            repository.updateFilm(film.copy(isSelected = !film.isSelected))
        }
    }

    private fun addFilmToDatabase(film: OmdbFilm) {
        lifecycleScope.launch {
            val filmEntity = FilmEntity(
                title = film.Title,
                year = film.Year,
                posterUrl = film.Poster,
                genre = film.Genre,
                imdbId = film.imdbID
            )
            repository.insertFilm(filmEntity)
        }
    }

    private fun searchFilms(query: String, year: String?) {
        lifecycleScope.launch {
            isSearching.value = true
            searchError.value = null
            searchResults.clear()

            try {
                val results = repository.searchFilmWithDetails(query, year)
                searchResults.addAll(results)
                if (results.isEmpty()) {
                    searchError.value = "Ничего не найдено"
                }
            } catch (e: Exception) {
                searchError.value = "Ошибка: ${e.message}"
                Log.e("WeWatch", "Search error", e)
            } finally {
                isSearching.value = false
            }
        }
    }

    private fun loadFilmDetails(imdbId: String) {
        lifecycleScope.launch {
            isLoadingDetails.value = true
            detailsError.value = null
            filmDetails.value = null

            try {
                val details = repository.getFilmDetails(imdbId)
                filmDetails.value = details
                if (details == null) {
                    detailsError.value = "Не удалось загрузить детали"
                }
            } catch (e: Exception) {
                detailsError.value = "Ошибка: ${e.message}"
                Log.e("WeWatch", "Details error", e)
            } finally {
                isLoadingDetails.value = false
            }
        }
    }

    private fun clearAddState() {
        searchQuery.value = ""
        searchYear.value = ""
        selectedFilm.value = null
    }
}