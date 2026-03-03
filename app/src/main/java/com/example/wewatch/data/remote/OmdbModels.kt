package com.example.wewatch.data.remote

import com.google.gson.annotations.SerializedName

data class OmdbSearchResponse(
    @SerializedName("Search")
    val Search: List<OmdbFilm>?,
    @SerializedName("totalResults")
    val totalResults: String,
    @SerializedName("Response")
    val Response: String
)

data class OmdbFilm(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
    val Genre: String? = null
)

/**
 * Детальная информация о фильме
 */
data class OmdbFilmDetails(
    @SerializedName("Title")
    val Title: String,
    @SerializedName("Year")
    val Year: String,
    @SerializedName("Rated")
    val Rated: String,
    @SerializedName("Released")
    val Released: String,
    @SerializedName("Runtime")
    val Runtime: String,
    @SerializedName("Genre")
    val Genre: String,
    @SerializedName("Director")
    val Director: String,
    @SerializedName("Writer")
    val Writer: String,
    @SerializedName("Actors")
    val Actors: String,
    @SerializedName("Plot")
    val Plot: String,
    @SerializedName("Language")
    val Language: String,
    @SerializedName("Country")
    val Country: String,
    @SerializedName("Awards")
    val Awards: String,
    @SerializedName("Poster")
    val Poster: String,
    @SerializedName("imdbRating")
    val imdbRating: String,
    @SerializedName("imdbID")
    val imdbID: String,
    @SerializedName("Type")
    val Type: String,
    @SerializedName("DVD")
    val DVD: String? = null,
    @SerializedName("BoxOffice")
    val BoxOffice: String? = null,
    @SerializedName("Production")
    val Production: String? = null,
    @SerializedName("Website")
    val Website: String? = null
)