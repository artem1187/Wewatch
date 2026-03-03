package com.example.wewatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "films")
data class FilmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String,
    val genre: String? = null,
    val isSelected: Boolean = false,
    val imdbId: String? = null
)