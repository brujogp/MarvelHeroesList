package com.test.marvel.data.models

sealed class Status {
    class SuccessfulResponse<out T>(val body: T) : Status()
    class Error(val message: String) : Status()

    object NoInternetConnection : Status()
    object Loading : Status()
    object NotFound : Status()
}