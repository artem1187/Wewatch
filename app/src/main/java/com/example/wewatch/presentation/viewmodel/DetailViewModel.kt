package com.example.wewatch.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.remote.OmdbFilmDetails
import com.example.wewatch.data.repository.FilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: FilmRepository
) : ViewModel() {

    private val _filmDetails = MutableStateFlow<OmdbFilmDetails?>(null)
    val filmDetails: StateFlow<OmdbFilmDetails?> = _filmDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadFilmDetails(imdbId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val details = repository.getFilmDetails(imdbId)
                _filmDetails.value = details

                if (details == null) {
                    _errorMessage.value = "Не удалось загрузить детали фильма"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}