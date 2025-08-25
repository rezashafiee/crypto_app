package com.example.crypto.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.example.crypto.feature.crypto.domain.Coin

@Dao
interface CoinDao {

    @Insert
    fun addCoins(vararg coins: Coin)

    @Delete
    fun removeCoin()
}