package com.example.homeassignment2.dictionary

import com.example.homeassignment2.words.WordData

// Raw word definition is a part of raw word data that defines one of word meanings
data class RawWordDefinition(
    val definition: String?,
    val example: String?,
    val synonyms: List<String>?,
    val antonyms: List<String>?
)

// RawWordMeaning is a part of raw word data about one of word's meanings
data class RawWordMeaning(
    val partOfSpeech: String?,
    val definitions: List<RawWordDefinition>?
)

// RawWordData is the actual information about a word that are loaded from online dictionary
data class RawWordData(
    val word: String?,
    val phonetic: String?,
    val origin: String?,
    val meanings: List<RawWordMeaning>?
)

fun makeWordDatas(rawData: RawWordData?): List<WordData>? {
    if (rawData == null) return null

    val result = mutableListOf<WordData>()
    val name = rawData.word ?: ""
    val transcription = rawData.phonetic ?: ""
    val origin = rawData.origin ?: ""

    for (meaning in rawData.meanings ?: listOf()) {
        val partOfSpeech = meaning.partOfSpeech ?: ""
        val rawDefinition = meaning.definitions?.get(0)
        val definition = rawDefinition?.definition ?: ""
        val example = rawDefinition?.example ?: ""

        val data = WordData(name, partOfSpeech, transcription, null, definition, "", example, origin)
        result.add(data)
    }

    return result.toList()
}