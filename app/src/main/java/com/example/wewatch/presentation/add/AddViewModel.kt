package com.example.wewatch.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.data.remote.OmdbFilm
import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.usecase.AddFilmUseCase
import com.example.wewatch.domain.usecase.SearchFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(  // ← @Inject constructor
    private val addFilmUseCase: AddFilmUseCase,
    private val searchFilmsUseCase: SearchFilmsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddState())
    val state: StateFlow<AddState> = _state.asStateFlow()

    private val _effect = Channel<AddEffect>()
    val effect: Flow<AddEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: AddIntent) {
        when (intent) {
            is AddIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is AddIntent.UpdateSearchYear -> updateSearchYear(intent.year)
            is AddIntent.SearchFilms -> searchFilms(intent.query, intent.year)
            is AddIntent.SelectFilm -> selectFilm(intent.film)
            is AddIntent.AddSelectedFilm -> addSelectedFilm()
            is AddIntent.ClearSelection -> clearSelection()
            is AddIntent.NavigateBack -> navigateBack()
            is AddIntent.NavigateToSearch -> navigateToSearch()
        }
    }

    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun updateSearchYear(year: String) {
        _state.update { it.copy(searchYear = year) }
    }

    private fun searchFilms(query: String, year: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val results = searchFilmsUseCase(query, year)
                if (results.isNotEmpty()) {
                    _effect.send(AddEffect.NavigateToSearch(query, year ?: ""))
                } else {
                    _state.update { it.copy(error = "Ничего не найдено", isLoading = false) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
                _effect.send(AddEffect.ShowError(e.message ?: "Ошибка поиска"))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun selectFilm(film: OmdbFilm) {
        _state.update { it.copy(selectedFilm = film) }
    }

    private fun addSelectedFilm() {
        viewModelScope.launch {
            val film = _state.value.selectedFilm ?: return@launch

            try {
                val domainFilm = Film(
                    title = film.Title,
                    year = film.Year,
                    posterUrl = film.Poster,
                    genre = film.Genre,
                    imdbId = film.imdbID
                )
                addFilmUseCase(domainFilm)
                _effect.send(AddEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.send(AddEffect.ShowError(e.message ?: "Ошибка добавления"))
            }
        }
    }

    private fun clearSelection() {
        _state.update { it.copy(selectedFilm = null) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(AddEffect.NavigateBack)
        }
    }

    private fun navigateToSearch() {
        val query = _state.value.searchQuery
        val year = _state.value.searchYear
        if (query.isNotBlank()) {
            viewModelScope.launch {
                _effect.send(AddEffect.NavigateToSearch(query, year))
            }
        }
    }
}