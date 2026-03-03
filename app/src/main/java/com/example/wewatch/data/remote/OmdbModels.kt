package com.example.wewatch.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Модель ответа от API при поиске фильмов
 * API возвращает JSON, который Gson преобразует в этот класс
 */
data class OmdbSearchResponse(
    @SerializedName("Search")        // Указываем точное имя поля в JSON
    val Search: List<OmdbFilm>?,      // Список найденных фильмов
    @SerializedName("totalResults")
    val totalResults: String,          // Общее количество результатов
    @SerializedName("Response")
    val Response: String               // Статус ответа (True/False)
)

/**
 * Модель фильма из результатов поиска
 */
data class OmdbFilm(
    @SerializedName("Title")
    val Title: String,                 // Название фильма
    @SerializedName("Year")
    val Year: String,                   // Год выпуска
    @SerializedName("imdbID")
    val imdbID: String,                 // Уникальный ID фильма на IMDB
    @SerializedName("Type")
    val Type: String,                   // Тип (movie, series, episode)
    @SerializedName("Poster")
    val Poster: String,                  // URL постера
    @SerializedName("Genre")             // Жанр (может отсутствовать в поиске)
    val Genre: String? = null            // Делаем опциональным
)

/**
 * Модель для получения детальной информации о фильме
 * Используется, когда нужно получить жанр
 */
data class OmdbFilmDetails(
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Year")
    val Year: String,
    @SerializedName("Genre")
    val Genre: String,                    // Здесь жанр точно есть
    @SerializedName("Plot")
    val Plot: String,                      // Сюжет фильма
    @SerializedName("Poster")
    val Poster: String,
    @SerializedName("imdbRating")
    val imdbRating: String                  // Рейтинг IMDB
)