package com.example.wewatch.presentation.detail

sealed class DetailEffect {
    data object NavigateBack : DetailEffect()
    data class ShowError(val message: String) : DetailEffect()
}