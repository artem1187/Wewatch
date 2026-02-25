package com.example.wewatch.data.local


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmDao {

    @Query("SELECT * FROM films")
    fun getAllFilms(): Flow<List<FilmEntity>>

    @Insert
    suspend fun insertFilm(film: FilmEntity)

    @Update
    suspend fun updateFilm(film: FilmEntity)

    @Delete
    suspend fun deleteFilm(film: FilmEntity)

    @Query("DELETE FROM films WHERE isSelected = 1")
    suspend fun deleteSelectedFilms()
}