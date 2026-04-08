package com.example.wewatch.domain.model


data class Film(
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val genre: String? = null,
    val isSelected: Boolean = false,
    val imdbId: String? = null
)