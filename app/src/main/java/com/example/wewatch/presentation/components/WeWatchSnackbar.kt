package com.example.wewatch.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberWeWatchSnackbar(): SnackbarHostState {
    return remember { SnackbarHostState() }
}

suspend fun showSuccessMessage(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.showSnackbar(
        message = message,
        duration = SnackbarDuration.Short
    )
}

suspend fun showErrorMessage(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    snackbarHostState.showSnackbar(
        message = "Ошибка: $message",
        duration = SnackbarDuration.Long
    )
}