package com.test.marvel.data.service

import com.test.marvel.data.models.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com"
        const val BASE_URL_HOST = "gateway.marvel.com"

        const val NOT_FOUND_CODE = 404

        const val API_KEY = "146936ed8ab5c314bb21475ef09182c9"
        const val M5D_DIGEST_HASH = "75432074c80f0f225c4b3d0aacf77e36"
    }

    @GET("/v1/public/characters")
    suspend fun getCharacters(): Response<CharactersResponse>
}