package com.tilda.core.domain.util

sealed class RemoteSyncResult {
    object Success : RemoteSyncResult()
    data class Error(val error: NetworkError) : RemoteSyncResult()
}