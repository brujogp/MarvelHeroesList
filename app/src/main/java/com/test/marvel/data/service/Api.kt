package com.test.marvel.data.service

import com.test.marvel.data.models.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com"
        const val NOT_FOUND_CODE = 404

        const val API_KEY = "fc65f97cde3bd46bc838dec5c8cb869e"
        const val M5D_DIGEST_HASH = "f145b80fb7d4f3d7ad3700632f3197c5"
    }

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limitResults: String = "100",
        @Query("offset") offset: String = "0"
    ): Response<CharactersResponse>
}