package com.example.wewatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.data.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: FilmRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<OmdbFilm>>(emptyList())
    val searchResults: StateFlow<List<OmdbFilm>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Добавляем состояние для загрузки деталей
    private val _isLoadingDetails = MutableStateFlow(false)
    val isLoadingDetails: StateFlow<Boolean> = _isLoadingDetails.asStateFlow()

    fun searchFilms(query: String, year: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Используем новый метод с деталями
                val results = repository.searchFilmWithDetails(query, year)
                _searchResults.value = results

                if (results.isEmpty()) {
                    _errorMessage.value = "Ничего не найдено"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * НОВЫЙ МЕТОД: Получить детали конкретного фильма
     */
    fun loadFilmDetails(imdbId: String, onResult: (OmdbFilm?) -> Unit) {
        viewModelScope.launch {
            _isLoadingDetails.value = true
            try {
                val details = repository.getFilmDetails(imdbId)
                if (details != null) {
                    val film = OmdbFilm(
                        Title = details.Title,
                        Year = details.Year,
                        imdbID = details.imdbID,
                        Type = details.Type,
                        Poster = details.Poster,
                        Genre = details.Genre
                    )
                    onResult(film)
                } else {
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            } finally {
                _isLoadingDetails.value = false
            }
        }
    }

    fun clearResults() {
        _searchResults.value = emptyList()
        _errorMessage.value = null
    }
}