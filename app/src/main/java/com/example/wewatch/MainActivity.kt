package com.example.wewatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.wewatch.data.local.AppDatabase
import com.example.wewatch.data.repository.FilmRepository
import com.example.wewatch.presentation.factory.*
import com.example.wewatch.presentation.navigation.SetupNavGraph
import com.example.wewatch.presentation.viewmodel.*
import com.example.wewatch.ui.theme.WewatchTheme

class MainActivity : ComponentActivity() {

    private lateinit var filmRepository: FilmRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация БД и репозитория
        val database = AppDatabase.getInstance(this)
        val apiKey = "bbf81799"
        filmRepository = FilmRepository(database, apiKey)

        // Создание ViewModel
        val mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(filmRepository)
        )[MainViewModel::class.java]

        val addViewModel = ViewModelProvider(
            this,
            AddViewModelFactory(filmRepository)
        )[AddViewModel::class.java]

        val searchViewModel = ViewModelProvider(
            this,
            SearchViewModelFactory(filmRepository)
        )[SearchViewModel::class.java]

        val detailViewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(filmRepository)
        )[DetailViewModel::class.java]

        setContent {
            WewatchTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(
                        mainViewModel = mainViewModel,
                        addViewModel = addViewModel,
                        searchViewModel = searchViewModel,
                        detailViewModel = detailViewModel
                    )
                }
            }
        }
    }
}