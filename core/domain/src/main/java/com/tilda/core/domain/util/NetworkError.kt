package com.tilda.core.domain.util

enum class NetworkError : Error {
    NO_INTERNET_ERROR,
    TOO_MANY_REQUESTS_ERROR,
    TIMEOUT_ERROR,
    SERIALIZATION_ERROR,
    SERVER_ERROR,
    UNKNOWN_ERROR
}