package com.test.marvel.data.repository

import com.test.marvel.data.models.Status
import com.test.marvel.data.service.Api
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: Api) {
    suspend fun getCharacters(offset: String): Status {
        return try {
            val request = this.api.getCharacters(offset = offset)

            if (request.code() == Api.NOT_FOUND_CODE) {
                Status.NotFound
            } else {
                Status.SuccessfulResponse(request.body())
            }
        } catch (e: Exception) {
            Status.Error(e.message!!)
        }
    }
}