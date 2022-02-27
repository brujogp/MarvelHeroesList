package com.test.marvel.data.domain

import android.util.Log
import com.test.marvel.data.models.CharactersResponse
import com.test.marvel.data.models.Status
import com.test.marvel.data.repository.MainRepository
import okhttp3.HttpUrl
import javax.inject.Inject

class Interact @Inject constructor(private val repository: MainRepository) {
    companion object {
        const val TAG = "Interact"
    }

    suspend fun getCharactersList(): Status {
        val response = this.repository.getCharacters()

        if (response is Status.SuccessfulResponse<*>) {
            (response.body as CharactersResponse).data.results.let {
                it.forEach { d ->
                    d.thumbnail.path =
                        "${d.thumbnail.path}/standard_fantastic.${d.thumbnail.extension}".replace(
                            "http",
                            "https"
                        )
                }

                var i = 0
                while (i < it.size) {
                    if (it[i].description.isEmpty() || it[i].description.isBlank()) {
                        it.remove(it[i])
                        i--
                    }
                    i++
                }
            }
        }

        return response
    }
}