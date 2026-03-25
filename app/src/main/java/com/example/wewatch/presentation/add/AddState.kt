package com.example.wewatch.presentation.add

import com.example.wewatch.data.remote.OmdbFilm

data class AddState(
    val searchQuery: String = "",
    val searchYear: String = "",
    val selectedFilm: OmdbFilm? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)