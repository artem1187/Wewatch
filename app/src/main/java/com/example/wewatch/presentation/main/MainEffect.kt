package com.example.wewatch.presentation.main

sealed class MainEffect {
    data object NavigateToAdd : MainEffect()
    data class NavigateToDetail(val filmId: Int) : MainEffect()
    data class ShowMessage(val message: String) : MainEffect()
}