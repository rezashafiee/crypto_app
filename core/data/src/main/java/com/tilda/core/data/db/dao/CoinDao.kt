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

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Query("SELECT * FROM coins")
    fun getPagingSource(): PagingSource<Int, CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(vararg coins: CoinEntity)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Query("SELECT lastUpdate FROM coins Limit 1")
    suspend fun getLastUpdatedEpochSeconds(): Long

    @Query("SELECT COUNT(*) FROM coins")
    suspend fun getCoinCount(): Int
}
