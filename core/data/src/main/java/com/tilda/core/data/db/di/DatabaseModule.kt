package com.tilda.core.data.db.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.dao.CoinDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    private val migration1To2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS favorite_coins (
                        coinId INTEGER NOT NULL,
                        PRIMARY KEY(coinId)
                    )
                """.trimIndent()
            )
        }
    }

    @Provides
    @Singleton
    fun provideCoinDatabase(
        @ApplicationContext context: Context
    ): CoinDatabase {
        return Room.databaseBuilder(
            context,
            CoinDatabase::class.java,
            "coin_database"
        )
            .addMigrations(migration1To2)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinDao(database: CoinDatabase): CoinDao {
        return database.coinDao()
    }
}
