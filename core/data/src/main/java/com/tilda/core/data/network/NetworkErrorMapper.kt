package com.tilda.core.data.network


import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.tilda.core.domain.util.NetworkError
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is UnknownHostException -> NetworkError.NoInternetError(message.orEmpty())
        is SocketTimeoutException -> NetworkError.TimeoutError(message.orEmpty())
        is JsonDataException -> NetworkError.SerializationError(message.orEmpty())
        is HttpException -> when (code()) {
            408 -> NetworkError.TimeoutError(message())
            429 -> NetworkError.TooManyRequestsError(message())
            in 500..599 -> NetworkError.ServerError(message())
            else -> NetworkError.UnknownError(message())
        }

        is JsonEncodingException -> NetworkError.SerializationError(message.orEmpty())
        is IOException -> NetworkError.NoInternetError(message.orEmpty())
        else -> NetworkError.UnknownError(message.orEmpty())
    }
}

