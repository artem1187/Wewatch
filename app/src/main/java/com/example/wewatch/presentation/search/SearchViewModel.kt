package com.example.wewatch.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.domain.usecase.SearchFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchFilmsUseCase: SearchFilmsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _effect = Channel<SearchEffect>()
    val effect: Flow<SearchEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.Search -> searchFilms(intent.query, intent.year)
            is SearchIntent.SelectFilm -> selectFilm(intent.film)
            is SearchIntent.ClearResults -> clearResults()
            is SearchIntent.NavigateBack -> navigateBack()
        }
    }

    private fun searchFilms(query: String, year: String?) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    query = query,
                    year = year ?: ""
                )
            }

            try {
                val results = searchFilmsUseCase(query, year)
                _state.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false
                    )
                }

                if (results.isEmpty()) {
                    _state.update { it.copy(error = "Ничего не найдено") }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка поиска",
                        isLoading = false
                    )
                }
                _effect.send(SearchEffect.ShowError(e.message ?: "Ошибка поиска"))
            }
        }
    }

    private fun selectFilm(film: OmdbFilm) {
        viewModelScope.launch {
            _effect.send(SearchEffect.FilmSelected(film))
        }
    }

    private fun clearResults() {
        _state.update { it.copy(searchResults = emptyList(), error = null) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(SearchEffect.NavigateBack)
        }
    }
}