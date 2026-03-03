package com.example.wewatch.presentation.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.wewatch.presentation.viewmodel.AddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    viewModel: AddViewModel,
    onNavigateBack: () -> Unit,
    onSearchClick: (String, String) -> Unit
) {
    val selectedFilm by viewModel.selectedFilm.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchYear by viewModel.searchYear.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить фильм") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Поле поиска
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Название фильма *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = searchQuery.isBlank(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                onSearchClick(searchQuery, searchYear)
                            }
                        },
                        enabled = searchQuery.isNotBlank()
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
                value = searchYear,
                onValueChange = { viewModel.updateSearchYear(it) },
                label = { Text("Год (необязательно)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Превью выбранного фильма
            if (selectedFilm != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Постер
                        AsyncImage(
                            model = selectedFilm!!.Poster.ifEmpty { "https://via.placeholder.com/200x300?text=No+Poster" },
                            contentDescription = selectedFilm!!.Title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Название
                        Text(
                            text = selectedFilm!!.Title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        // Год
                        Text(
                            text = selectedFilm!!.Year,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Жанр (если есть)
                        selectedFilm!!.Genre?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Кнопка добавления
                        Button(
                            onClick = {
                                viewModel.addFilmToDatabase()
                                onNavigateBack()
                            },
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
        }
    }
}