package com.test.marvel.data.models.comics

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)