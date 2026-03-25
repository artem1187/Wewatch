package com.example.wewatch.presentation.detail

sealed class DetailIntent {
    data class LoadDetails(val imdbId: String) : DetailIntent()
    data object NavigateBack : DetailIntent()
}