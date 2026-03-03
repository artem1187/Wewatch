package com.example.wewatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddViewModel(
    private val repository: FilmRepository
) : ViewModel() {

    private val _selectedFilm = MutableStateFlow<OmdbFilm?>(null)
    val selectedFilm: StateFlow<OmdbFilm?> = _selectedFilm.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchYear = MutableStateFlow("")
    val searchYear: StateFlow<String> = _searchYear.asStateFlow()

    fun setSelectedFilm(film: OmdbFilm) {
        _selectedFilm.value = film
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSearchYear(year: String) {
        _searchYear.value = year
    }

    fun addFilmToDatabase() {
        viewModelScope.launch {
            _selectedFilm.value?.let { film ->
                val filmEntity = FilmEntity(
                    title = film.Title,
                    year = film.Year,
                    posterUrl = film.Poster,
                    genre = film.Genre
                )
                repository.insertFilm(filmEntity)
                clearSelection()
            }
        }
    }

    fun clearSelection() {
        _selectedFilm.value = null
        _searchQuery.value = ""
        _searchYear.value = ""
    }
}