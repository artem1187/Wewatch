package com.example.wewatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.local.FilmEntity
import com.example.wewatch.data.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FilmRepository
) : ViewModel() {

    private val _films = MutableStateFlow<List<FilmEntity>>(emptyList())
    val films: StateFlow<List<FilmEntity>> = _films.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFilms()
    }

    private fun loadFilms() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllFilms().collect { filmsList ->
                _films.value = filmsList
                _isLoading.value = false
            }
        }
    }

    fun deleteSelectedFilms() {
        viewModelScope.launch {
            repository.deleteSelectedFilms()
        }
    }

    fun toggleFilmSelection(film: FilmEntity) {
        viewModelScope.launch {
            repository.updateFilm(film.copy(isSelected = !film.isSelected))
        }
    }
}
