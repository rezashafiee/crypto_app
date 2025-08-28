package com.example.crypto.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.crypto.core.data.db.model.CoinEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins")
    fun getAllCoins(): Flow<List<CoinEntity>>

    @Insert
    fun addCoins(vararg coins: CoinEntity)

    @Delete
    fun removeCoins()
}