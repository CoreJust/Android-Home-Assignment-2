package com.example.homeassignment2.dictionary

import com.example.homeassignment2.words.WordData

// The main class to work with the online dictionary
object Dictionary {
    private const val BASE_URL = "https://api.dictionaryapi.dev/"
    private val dictionaryService: DictionaryService
        get() = Client.getClient(BASE_URL).create(DictionaryService::class.java)

    suspend fun getRawWordDatas(word: String): List<RawWordData>? = dictionaryService.getWordData(word).body()
    suspend fun getWordData(word: String): List<WordData>? = makeWordDatas(getRawWordDatas(word)?.get(0))
}