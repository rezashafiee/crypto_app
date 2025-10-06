package com.tilda.feature.crypto.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.model.CoinEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CoinLocalDataSourceImpIntegrationTest {

    private lateinit var db: CoinDatabase
    private lateinit var dataSource: CoinLocalDataSourceImp

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries() // safe for tests
            .build()
        dataSource = CoinLocalDataSourceImp(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun addCoins_insertsData_and_getItemsCountReflectsIt() = runTest {
        // given
        val initialCount = dataSource.getItemsCount()
        assertEquals(0, initialCount)

        val items = listOf(
            coinEntity(id = 1, symbol = "BTC", name = "Bitcoin", lastUpdate = 1111L),
            coinEntity(id = 2, symbol = "ETH", name = "Ethereum", lastUpdate = 2222L)
        )

        // when
        dataSource.addCoins(items)

        // then
        val countAfterInsert = dataSource.getItemsCount()
        assertEquals(items.size, countAfterInsert)
    }

    @Test
    fun replaceAllCoins_clearsOldAndInsertsNew() = runTest {
        // given
        val oldItems = listOf(
            coinEntity(id = 10, symbol = "DOGE", name = "Dogecoin", lastUpdate = 10L),
            coinEntity(id = 11, symbol = "ADA", name = "Cardano", lastUpdate = 11L)
        )
        dataSource.addCoins(oldItems)
        assertEquals(2, dataSource.getItemsCount())

        val newItems = listOf(
            coinEntity(id = 20, symbol = "SOL", name = "Solana", lastUpdate = 20L)
        )

        // when
        dataSource.replaceAllCoins(newItems)

        // then
        assertEquals(1, dataSource.getItemsCount())
        val daoItems = db.coinDao().getAllCoins().first()
        assertEquals(1, daoItems.size)
        assertEquals(20, daoItems.first().id)
        assertEquals("SOL", daoItems.first().symbol)
    }

    @Test
    fun getLastUpdated_returnsTimestampOfExistingRow_whenSingleRow() = runTest {
        // given
        val single = listOf(coinEntity(id = 100, symbol = "X", name = "Only", lastUpdate = 9999L))
        dataSource.replaceAllCoins(single)

        // when
        val ts = dataSource.getLastUpdated()

        // then
        assertEquals(9999L, ts)
    }

    // Helpers
    private fun coinEntity(
        id: Int,
        symbol: String,
        name: String,
        lastUpdate: Long,
    ) = CoinEntity(
        id = id,
        rank = id.toString(),
        symbol = symbol,
        name = name,
        currentPrice = 0.0,
        marketCap = 0.0,
        priceChange24h = 0.0,
        priceChangePercentage24h = 0.0,
        logoUrl = "",
        lastUpdate = lastUpdate
    )
}
