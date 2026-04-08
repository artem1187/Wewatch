package com.example.wewatch.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFilmsUseCase: GetFilmsUseCase,
    private val deleteSelectedFilmsUseCase: DeleteSelectedFilmsUseCase,
    private val toggleFilmSelectionUseCase: ToggleFilmSelectionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effect = Channel<MainEffect>()
    val effect: Flow<MainEffect> = _effect.receiveAsFlow()

    init {
        handleIntent(MainIntent.LoadFilms)
    }

    fun handleIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadFilms -> loadFilms()
            is MainIntent.DeleteSelected -> deleteSelected()
            is MainIntent.ToggleSelection -> toggleSelection(intent.filmId)
            is MainIntent.NavigateToAdd -> navigateToAdd()
            is MainIntent.NavigateToDetail -> navigateToDetail(intent.filmId)
            is MainIntent.ShowDeleteDialog -> showDeleteDialog()
            is MainIntent.HideDeleteDialog -> hideDeleteDialog()
        }
    }

    private fun loadFilms() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            getFilmsUseCase()
                .catch { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
                .collect { films ->
                    _state.update { it.copy(films = films, isLoading = false) }
                }
        }
    }

    private fun deleteSelected() {
        viewModelScope.launch {
            try {
                deleteSelectedFilmsUseCase()
                _state.update { it.copy(showDeleteDialog = false) }
                _effect.send(MainEffect.ShowMessage("Фильмы удалены"))
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private fun toggleSelection(filmId: Int) {
        viewModelScope.launch {
            val film = _state.value.films.find { it.id == filmId } ?: return@launch
            toggleFilmSelectionUseCase(film)
        }
    }

    private fun navigateToAdd() {
        viewModelScope.launch {
            _effect.send(MainEffect.NavigateToAdd)
        }
    }

    private fun navigateToDetail(filmId: Int) {
        viewModelScope.launch {
            _effect.send(MainEffect.NavigateToDetail(filmId))
        }
    }

    private fun showDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = true) }
    }

    private fun hideDeleteDialog() {
        _state.update { it.copy(showDeleteDialog = false) }
    }
}