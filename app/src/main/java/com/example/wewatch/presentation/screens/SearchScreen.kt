package com.example.wewatch.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wewatch.presentation.components.SearchResultItem
import com.example.wewatch.presentation.viewmodel.AddViewModel
import com.example.wewatch.presentation.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    addViewModel: AddViewModel,
    query: String,
    year: String,
    onNavigateBack: () -> Unit,
    onFilmSelected: () -> Unit
) {
    val searchResults by searchViewModel.searchResults.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val isLoadingDetails by searchViewModel.isLoadingDetails.collectAsState()
    val errorMessage by searchViewModel.errorMessage.collectAsState()

    // Состояние для выбранного фильма (для показа диалога загрузки)
    var selectedFilmId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(query, year) {
        searchViewModel.searchFilms(query, year.takeIf { it.isNotBlank() })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Результаты поиска") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = onNavigateBack) {
                            Text("Назад")
                        }
                    }
                }

                searchResults.isEmpty() -> {
                    Text(
                        text = "Ничего не найдено",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { film ->
                            SearchResultItem(
                                film = film,
                                onClick = {
                                    selectedFilmId = film.imdbID
                                    // Загружаем детали перед добавлением
                                    searchViewModel.loadFilmDetails(film.imdbID) { detailedFilm ->
                                        if (detailedFilm != null) {
                                            addViewModel.setSelectedFilm(detailedFilm)
                                            onFilmSelected()
                                        }
                                        selectedFilmId = null
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Индикатор загрузки деталей
            if (isLoadingDetails || selectedFilmId != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Загрузка деталей...")
                    }
                }
            }
        }
    }
}