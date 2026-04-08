package com.example.wewatch.domain.model


data class SearchResult(
    val title: String,
    val year: String,
    val imdbId: String,
    val posterUrl: String,
    val genre: String? = null
)