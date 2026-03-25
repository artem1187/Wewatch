package com.example.wewatch.presentation.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.wewatch.presentation.components.SearchResultItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: AddViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToSearch: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddEffect.NavigateBack -> onNavigateBack()
                is AddEffect.ShowError -> {
                    // Показать ошибку (можно через Snackbar)
                }
                is AddEffect.NavigateToSearch -> onNavigateToSearch(effect.query, effect.year)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить фильм") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.handleIntent(AddIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Поле поиска
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.handleIntent(AddIntent.UpdateSearchQuery(it)) },
                label = { Text("Название фильма *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = state.searchQuery.isBlank(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (state.searchQuery.isNotBlank()) {
                                viewModel.handleIntent(AddIntent.NavigateToSearch)
                            }
                        },
                        enabled = state.searchQuery.isNotBlank()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Поле для года (опционально)
            OutlinedTextField(
                value = state.searchYear,
                onValueChange = { viewModel.handleIntent(AddIntent.UpdateSearchYear(it)) },
                label = { Text("Год (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Превью выбранного фильма
            if (state.selectedFilm != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = state.selectedFilm!!.Poster.ifEmpty {
                                "https://via.placeholder.com/200x300?text=No+Poster"
                            },
                            contentDescription = state.selectedFilm!!.Title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = state.selectedFilm!!.Title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = state.selectedFilm!!.Year,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        state.selectedFilm!!.Genre?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.handleIntent(AddIntent.AddSelectedFilm) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Добавить в список")
                        }
                    }
                }
            } else {
                // Инструкция когда фильм не выбран
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Поиск фильма",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Введите название фильма и нажмите на иконку поиска",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Индикатор загрузки
            if (state.isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            // Сообщение об ошибке
            if (state.error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}