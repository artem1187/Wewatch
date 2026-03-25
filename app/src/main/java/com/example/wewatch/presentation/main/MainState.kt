package com.example.wewatch.presentation.main

import com.example.wewatch.domain.model.Film

data class MainState(
    val films: List<Film> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteDialog: Boolean = false
)