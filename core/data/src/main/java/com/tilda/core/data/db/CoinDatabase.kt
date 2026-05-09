package com.tilda.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tilda.core.data.db.dao.CoinDao
import com.tilda.core.data.db.dao.FavoriteCoinDao
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.data.db.model.FavoriteCoinEntity

@Database(
    entities = [
        CoinEntity::class,
        FavoriteCoinEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
    abstract fun favoriteCoinDao(): FavoriteCoinDao
}
