package com.example.wewatch.presentation.detail

import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.domain.model.Film

data class DetailState(
    val film: Film? = null,
    val details: OmdbFilmDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)