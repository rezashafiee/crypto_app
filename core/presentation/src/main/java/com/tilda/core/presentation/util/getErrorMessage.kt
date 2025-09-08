package com.tilda.core.presentation.util

import android.content.Context
import com.tilda.core.domain.util.DomainError
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.presentation.R

fun getErrorMessage(context: Context, error: DomainError) =
    when (error) {
        is NetworkError.NoInternetError -> context.getString(R.string.no_internet_error)
        is NetworkError.ServerError -> context.getString(R.string.server_error)
        is NetworkError.TimeoutError -> context.getString(R.string.timout_error)
        is NetworkError.SerializationError -> context.getString(R.string.serialization_error)
        is NetworkError.TooManyRequestsError -> context.getString(R.string.too_many_requests_error)
        is NetworkError.UnknownError -> context.getString(R.string.unknown_error)
        else -> context.getString(R.string.unexpected_error)
    }