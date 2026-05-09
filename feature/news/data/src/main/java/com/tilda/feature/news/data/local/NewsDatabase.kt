package com.tilda.feature.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
internal abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
