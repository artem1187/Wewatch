package com.example.wewatch.presentation.main

sealed class MainIntent {
    data object LoadFilms : MainIntent()
    data object DeleteSelected : MainIntent()
    data class ToggleSelection(val filmId: Int) : MainIntent()
    data object NavigateToAdd : MainIntent()
    data class NavigateToDetail(val filmId: Int) : MainIntent()
    data object ShowDeleteDialog : MainIntent()
    data object HideDeleteDialog : MainIntent()
}