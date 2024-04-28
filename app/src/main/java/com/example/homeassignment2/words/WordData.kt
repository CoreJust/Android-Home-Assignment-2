package com.example.homeassignment2.words

// A sketch of what data on a word would be loaded, stored and displayed to the user.
data class WordData(
    // The actual word.
    // Would be in the item's headline and is expected to be shown capitalized.
    val word: String,

    // The part of speech the word belongs to.
    // Probably to be replaced with an Enum.
    val partOfSpeech: String,

    // An IPA transcription of the word.
    // Optional parameter, but supposed to normally be present
    val transcription: String?,

    // A link to the audio example of word's pronunciation.
    // Optional, supposedly only for some specific words.
    val audioExample: String?, // Not used as of now.

    // Word definition.
    // An obligatory part.
    val definition: String,

    // A list of possible translations for the word, can be empty.
    val translations: String,

    // Example of word usage, a sentence with a word.
    // Since there can be numerous examples, especially for ambiguous words,
    // a list is used.
    val examples: String,

    // Word history, where it originates from.
    // Optional parameter.
    val origin: String?
)
