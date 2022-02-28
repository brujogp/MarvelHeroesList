package com.test.marvel.data.models.comics

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)