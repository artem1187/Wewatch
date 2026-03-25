package com.example.wewatch.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wewatch.domain.model.Film
import com.example.wewatch.domain.usecase.GetFilmDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(  // ← @Inject constructor
    private val getFilmDetailsUseCase: GetFilmDetailsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _effect = Channel<DetailEffect>()
    val effect: Flow<DetailEffect> = _effect.receiveAsFlow()

    fun handleIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.LoadDetails -> loadDetails(intent.imdbId)
            is DetailIntent.NavigateBack -> navigateBack()
        }
    }

    private fun loadDetails(imdbId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val details = getFilmDetailsUseCase(imdbId)
                _state.update {
                    it.copy(
                        details = details,
                        isLoading = false
                    )
                }

                if (details == null) {
                    _state.update { it.copy(error = "Не удалось загрузить детали") }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Ошибка загрузки",
                        isLoading = false
                    )
                }
                _effect.send(DetailEffect.ShowError(e.message ?: "Ошибка загрузки"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effect.send(DetailEffect.NavigateBack)
        }
    }

    fun setFilm(film: Film) {
        _state.update { it.copy(film = film) }
    }
}