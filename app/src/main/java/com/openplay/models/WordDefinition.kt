package com.openplay.models


data class WordDefinition(
    val word: String,
    val phonetics: List<Phonetic>,
    val meanings: List<Meaning>
)

data class Phonetic(
    val text: String,
    val audio: String
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?
)
