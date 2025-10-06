package com.tilda.feature.crypto.data.local

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.google.common.truth.Truth.assertThat
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.dao.CoinDao
import com.tilda.core.data.db.model.CoinEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class CoinLocalDataSourceImpTest {

    @MockK(relaxed = true)
    lateinit var coinDatabase: CoinDatabase

    @MockK(relaxed = true)
    lateinit var coinDao: CoinDao

    private lateinit var dataSource: CoinLocalDataSourceImp

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { coinDatabase.coinDao() } returns coinDao
        dataSource = CoinLocalDataSourceImp(coinDatabase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getItemsCount_delegatesToDao() = runTest {
        // given
        coEvery { coinDao.countItems() } returns 42

        // when
        val result = dataSource.getItemsCount()

        // then
        coVerify(exactly = 1) { coinDao.countItems() }
        assertThat(result).isEqualTo(42)
    }

    @Test
    fun getLastUpdated_delegatesToDao() = runTest {
        // given
        coEvery { coinDao.getLastUpdated() } returns 123456789L

        // when
        val result = dataSource.getLastUpdated()

        // then
        coVerify(exactly = 1) { coinDao.getLastUpdated() }
        assertThat(result).isEqualTo(123456789L)
    }

    @Test
    fun addCoins_callsDaoWithVararg() = runTest {
        // given
        val items = sampleItems()

        // when
        dataSource.addCoins(items)

        // then
        coVerify(exactly = 1) { coinDao.addCoins(*items.toTypedArray()) }
        // Ensure no unintended calls
        coVerify(exactly = 0) { coinDao.removeAllCoins() }
    }

    @Test
    fun replaceAllCoins_runsInTransactionAndReplacesData() = runTest {
        // given
        // Mock the RoomDatabase.withTransaction extension so it invokes the block immediately
        mockkStatic("androidx.room.RoomDatabaseKt")
        coEvery { (coinDatabase as RoomDatabase).withTransaction(any<suspend () -> Any>()) } coAnswers {
            val block = secondArg<suspend () -> Any>()
            block.invoke()
        }
        val items = sampleItems()

        // when
        dataSource.replaceAllCoins(items)

        // then
        coVerify(exactly = 1) { coinDao.removeAllCoins() }
        coVerify(exactly = 1) { coinDao.addCoins(*items.toTypedArray()) }
    }

    private fun sampleItems(): List<CoinEntity> = listOf(
        CoinEntity(
            id = 1,
            rank = "1",
            symbol = "BTC",
            name = "Bitcoin",
            currentPrice = 50000.0,
            marketCap = 1_000_000.0,
            priceChange24h = 100.0,
            priceChangePercentage24h = 2.0,
            logoUrl = "https://example.com/btc.png",
            lastUpdate = 1000L
        ),
        CoinEntity(
            id = 2,
            rank = "2",
            symbol = "ETH",
            name = "Ethereum",
            currentPrice = 3000.0,
            marketCap = 500_000.0,
            priceChange24h = 50.0,
            priceChangePercentage24h = 1.5,
            logoUrl = "https://example.com/eth.png",
            lastUpdate = 2000L
        )
    )
}
