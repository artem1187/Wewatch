package com.example.wewatch.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Объект-синглтон для создания Retrofit клиента
 * Создается только один раз за всё время работы приложения
 */
object RetrofitInstance {

    // Базовый URL OMDb API
    private const val BASE_URL = "https://www.omdbapi.com/"

    /**
     * Настройка HTTP клиента с логированием и таймаутами
     */
    private val client = OkHttpClient.Builder()
        .apply {
            // Добавляем логирование запросов и ответов
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY  // Логируем всё тело запроса
            }
            addInterceptor(logging)
        }
        .connectTimeout(30, TimeUnit.SECONDS)   // Таймаут подключения
        .readTimeout(30, TimeUnit.SECONDS)      // Таймаут чтения
        .writeTimeout(30, TimeUnit.SECONDS)     // Таймаут записи
        .build()

    /**
     * Ленивая инициализация Retrofit сервиса
     * Создается только при первом обращении
     */
    val api: OmdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())  // Конвертер JSON
            .build()
            .create(OmdbApiService::class.java)
    }
}