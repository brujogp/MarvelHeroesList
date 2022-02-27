package com.test.marvel.data.domain

import com.test.marvel.data.models.CharactersResponse
import com.test.marvel.data.models.Status
import com.test.marvel.data.repository.MainRepository
import javax.inject.Inject

class Interact @Inject constructor(private val repository: MainRepository) {

    suspend fun getCharactersList(): Status {
        val response = this.repository.getCharacters()

        if (response is Status.SuccessfulResponse<*>) {
            (response.body as CharactersResponse).data.results.let {
                it.filter { d ->
                    d.description!!.isNotEmpty()
                }

                it.forEach { d ->
                    d.thumbnail.path =
                        "${d.thumbnail.path}/landscape_medium.${d.thumbnail.extension}"
                }
            }
        }

        return response
    }
}