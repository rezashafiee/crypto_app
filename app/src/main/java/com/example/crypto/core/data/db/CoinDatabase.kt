package com.example.crypto.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.crypto.core.data.db.dao.CoinDao
import com.example.crypto.core.data.db.model.CoinEntity

@Database(entities = [CoinEntity::class], version = 1)
abstract class CoinDatabase: RoomDatabase() {
    abstract fun coinDao(): CoinDao
}