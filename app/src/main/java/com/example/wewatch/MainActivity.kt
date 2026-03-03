package com.example.wewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.presentation.factory.MainViewModelFactory
import com.example.wewatch.presentation.viewmodel.MainViewModel
import com.example.wewatch.ui.theme.WewatchTheme

class MainActivity : ComponentActivity() {

    private lateinit var filmRepository: FilmRepository
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация БД и репозитория
        val database = AppDatabase.getInstance(this)
        val apiKey = "bbf81799"
        filmRepository = FilmRepository(database, apiKey)

        // Создание ViewModel
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(filmRepository)
        )[MainViewModel::class.java]

        setContent {
            WewatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Пока просто заглушка, позже заменим на реальный экран
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Главный экран с фильмами")
    }
}