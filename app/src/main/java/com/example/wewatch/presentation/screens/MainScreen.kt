package com.example.wewatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.presentation.components.DeleteConfirmationDialog
import com.example.wewatch.presentation.components.EmptyState
import com.example.wewatch.presentation.components.FilmListItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    films: List<FilmEntity>,
    isLoading: Boolean,
    onDeleteSelected: () -> Unit,
    onToggleSelection: (FilmEntity) -> Unit,
    onAddClick: () -> Unit,
    onFilmClick: (FilmEntity) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои фильмы") },
                actions = {
                    if (films.any { it.isSelected }) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить выбранные"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить фильм"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (films.isEmpty()) {
                EmptyState(
                    message = "Нет выбранных фильмов\nНажмите + чтобы добавить",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(films) { film ->
                        FilmListItem(
                            film = film,
                            onCheckChanged = { onToggleSelection(film) },
                            onItemClick = { onFilmClick(film) }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                onDeleteSelected()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}