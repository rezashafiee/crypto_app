package com.tilda.core.data.network

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import retrofit2.HttpException
import retrofit2.Response

/**
 * Executes a Retrofit request and converts transport/parsing failures into [NetworkError].
 *
 * @param execute Suspended Retrofit call returning a [Response].
 * @param mapper Maps the successful response body to the desired output type.
 * @return [Result.Success] with mapped data or [Result.Error] with mapped [NetworkError].
 */
suspend fun <T, R> safeRetrofitCall(
    execute: suspend () -> Response<T>,
    mapper: (T) -> R
): Result<R, NetworkError> {
    return try {
        val response = execute()
        val body = response.body()

        if (!response.isSuccessful || body == null) {
            throw HttpException(response)
        }

        Result.Success(mapper(body))
    } catch (e: Throwable) {
        currentCoroutineContext().ensureActive()
        Result.Error(e.toNetworkError())
    }
}

