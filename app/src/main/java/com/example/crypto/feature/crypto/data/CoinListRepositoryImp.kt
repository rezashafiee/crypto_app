package com.example.crypto.feature.crypto.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.crypto.core.domain.util.RemoteSyncResult
import com.example.crypto.feature.crypto.domain.Coin
import com.example.crypto.feature.crypto.domain.CoinListLocalDataSource
import com.example.crypto.feature.crypto.domain.CoinListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

class CoinListRepositoryImp(
    private val localDataSource: CoinListLocalDataSource,
    private val context: Context,
    override val remoteSyncEvents: SharedFlow<RemoteSyncResult>
) : CoinListRepository {

    override suspend fun getCoins(): Flow<List<Coin>> {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchCoinsWorkRequest = OneTimeWorkRequestBuilder<FetchRemoteCoinsWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            FetchRemoteCoinsWorker.WORK_NAME,
            ExistingWorkPolicy.KEEP,
            fetchCoinsWorkRequest
        )

        // Always return the Flow from the local data source for offline-first.
        // The worker will update the local data source in the background.
        return localDataSource.getCoins()
    }
}