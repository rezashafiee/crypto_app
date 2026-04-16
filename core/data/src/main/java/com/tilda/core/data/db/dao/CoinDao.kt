package com.tilda.core.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tilda.core.data.db.model.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    /** Observes all stored coins. */
    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinEntity>>

    /** Returns a [PagingSource] over all stored coins. */
    @Query("SELECT * FROM coins")
    fun getPagingSource(): PagingSource<Int, CoinEntity>

    /** Inserts or replaces the provided [coins]. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(vararg coins: CoinEntity)

    /** Deletes all stored coin rows. */
    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    /** Returns the latest update timestamp in epoch seconds from stored coins. */
    @Query("SELECT lastUpdate FROM coins Limit 1")
    suspend fun getLastUpdatedEpochSeconds(): Long

    /** Returns the total number of stored coins. */
    @Query("SELECT COUNT(*) FROM coins")
    suspend fun getCoinCount(): Int
}
