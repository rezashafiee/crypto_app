package com.example.crypto.feature.crypto.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.crypto.core.domain.util.NetworkError
import com.example.crypto.core.domain.util.RemoteSyncResult
import com.example.crypto.core.domain.util.onError
import com.example.crypto.core.domain.util.onSuccess
import com.example.crypto.feature.crypto.data.mappers.toCoinEntity
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import com.example.crypto.feature.crypto.domain.CoinListRemoteDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FetchRemoteCoinsWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val remoteDataSource: CoinListRemoteDataSource by inject()
    private val localDataSource: CoinListLocalDataSource by inject()
    private val remoteSyncResultFlow: MutableSharedFlow<RemoteSyncResult> by inject()

    companion object {
        const val WORK_NAME = "FetchRemoteCoinsWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            val remoteCoinsResult = remoteDataSource.getCoins()
            remoteCoinsResult.onSuccess { coins ->
                localDataSource.insertCoins(coins.map { it.toCoinEntity() })
                Log.d(WORK_NAME, "Successfully fetched and stored remote coins.")
                remoteSyncResultFlow.emit(RemoteSyncResult.Success)
                return Result.success()
            }

            remoteCoinsResult.onError { error ->
                Log.e(WORK_NAME, "Error fetching remote coins: $error")
                remoteSyncResultFlow.emit(RemoteSyncResult.Error(error))
                return Result.retry()
            }
            val unexpectedStateMessage =
                "Remote coin fetch result was not explicitly success or error."
            Log.w(WORK_NAME, unexpectedStateMessage)
            remoteSyncResultFlow.emit(RemoteSyncResult.Error(NetworkError.UNKNOWN_ERROR))
            Result.failure()

        } catch (e: Exception) {
            val exceptionMessage = "Exception during doWork in FetchRemoteCoinsWorker"
            Log.e(WORK_NAME, exceptionMessage, e)
            remoteSyncResultFlow.emit(RemoteSyncResult.Error(NetworkError.UNKNOWN_ERROR))
            Result.retry()
        }
    }
}