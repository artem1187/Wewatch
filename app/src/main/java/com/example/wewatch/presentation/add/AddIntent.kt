package com.example.wewatch.presentation.add

import com.example.wewatch.data.remote.OmdbFilm

sealed class AddIntent {
    data class UpdateSearchQuery(val query: String) : AddIntent()
    data class UpdateSearchYear(val year: String) : AddIntent()
    data class SearchFilms(val query: String, val year: String?) : AddIntent()
    data class SelectFilm(val film: OmdbFilm) : AddIntent()
    data object AddSelectedFilm : AddIntent()
    data object ClearSelection : AddIntent()
    data object NavigateBack : AddIntent()
    data object NavigateToSearch : AddIntent()
}