package com.test.marvel.data.models

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: MutableList<Result>,
    val total: Int
)