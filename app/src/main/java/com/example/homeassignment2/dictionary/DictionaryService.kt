package com.example.homeassignment2.dictionary

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Service to request the information on a certain word from the online dictionary
interface DictionaryService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordData(@Path("word") word: String): Response<List<RawWordData>>
}