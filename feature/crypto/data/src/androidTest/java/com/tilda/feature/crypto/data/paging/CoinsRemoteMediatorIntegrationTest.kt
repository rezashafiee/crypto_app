package com.tilda.feature.crypto.data.paging

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.MediatorResult.Success
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tilda.core.data.db.CoinDatabase
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.data.local.CoinLocalDataSourceImp
import com.tilda.feature.crypto.domain.model.Coin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime
import com.tilda.feature.crypto.domain.model.CoinPrice

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CoinsRemoteMediatorIntegrationTest {

    private lateinit var db: CoinDatabase
    private lateinit var local: CoinLocalDataSourceImp
    private lateinit var remote: FakeCoinRemoteDataSource
    private lateinit var mediator: CoinsRemoteMediator

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CoinDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        local = CoinLocalDataSourceImp(db)
        remote = FakeCoinRemoteDataSource()
        mediator = CoinsRemoteMediator(
            remoteDataSource = remote,
            localDataSource = local
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    /**
     * Creates a [PagingState] for testing purposes.
     * @param pageSize The number of items per page.
     * @return A [PagingState] instance.
     */
    private fun pagingState(pageSize: Int): PagingState<Int, CoinEntity> {
        val config = PagingConfig(pageSize = pageSize)
        return PagingState(
            pages = listOf(),
            anchorPosition = null,
            config = config,
            leadingPlaceholderCount = 0
        )
    }

    /**
     * Factory function to create a [Coin] domain model for testing.
     * @param id The identifier for the coin, also used to populate other fields.
     * @return A [Coin] instance.
     */
    private fun domainCoin(id: Int): Coin = Coin(
        id = id,
        rank = id.toString(),
        symbol = "C$id",
        name = "Coin$id",
        currentPrice = id.toDouble(),
        marketCap = id.toDouble(),
        priceChange24h = 0.0,
        priceChangePercentage24h = 0.0,
        logoUrl = "",
        lastUpdate = 0L
    )

    /**
     * Verifies that a REFRESH load fetches the first page and stores it in the database.
     */
    @Test
    fun refresh_populatesRoom_withFirstPage() = runTest {
        // given
        val pageSize = 10
        val all = (1..23).map { domainCoin(it) } // pages: 10, 10, 3
        remote.setCoins(all)

        // when
        val refreshResult = mediator.load(LoadType.REFRESH, pagingState(pageSize))

        // then
        assertThat(refreshResult).isInstanceOf(Success::class.java)
        val refreshSuccess = refreshResult as Success
        assertThat(refreshSuccess.endOfPaginationReached).isFalse()
        assertThat(local.getItemsCount()).isEqualTo(10)
        val firstPageIds = db.coinDao().getAllCoins().first().map { it.id }
        assertThat(firstPageIds).isEqualTo((1..10).toList())
    }

    /**
     * Verifies that after a REFRESH, an APPEND loads the next page and does not end pagination yet.
     */
    @Test
    fun append_afterRefresh_loadsNextPage_withoutEndingPagination() = runTest {
        // given
        val pageSize = 10
        val all = (1..23).map { domainCoin(it) } // pages: 10, 10, 3
        remote.setCoins(all)
        // Seed first page
        val refreshResult = mediator.load(LoadType.REFRESH, pagingState(pageSize))
        assertThat(refreshResult).isInstanceOf(Success::class.java)

        // when
        val append1 = mediator.load(LoadType.APPEND, pagingState(pageSize))

        // then
        assertThat(append1).isInstanceOf(Success::class.java)
        val append1Success = append1 as Success
        assertThat(append1Success.endOfPaginationReached).isFalse()
        assertThat(local.getItemsCount()).isEqualTo(20)
    }

    /**
     * Verifies that on the last page, an APPEND returns endOfPaginationReached = true and all items are stored.
     */
    @Test
    fun append_onLastPage_setsEndOfPagination_and_persistsAllItems() = runTest {
        // given
        val pageSize = 10
        val all = (1..23).map { domainCoin(it) } // pages: 10, 10, 3
        remote.setCoins(all)
        // Load first two pages
        val refreshResult = mediator.load(LoadType.REFRESH, pagingState(pageSize))
        assertThat(refreshResult).isInstanceOf(Success::class.java)
        val append1 = mediator.load(LoadType.APPEND, pagingState(pageSize))
        assertThat(append1).isInstanceOf(Success::class.java)

        // when
        val append2 = mediator.load(LoadType.APPEND, pagingState(pageSize))

        // then
        assertThat(append2).isInstanceOf(Success::class.java)
        val append2Success = append2 as Success
        assertThat(append2Success.endOfPaginationReached).isTrue()
        assertThat(local.getItemsCount()).isEqualTo(23)
        val allIds = db.coinDao().getAllCoins().first().map { it.id }
        assertThat(allIds).isEqualTo((1..23).toList())
    }

    /**
     * Verifies that initialize() skips initial refresh when cache is fresh (<= 1 hour old).
     */
    @Test
    fun initialize_skipsInitialRefresh_whenCacheIsFresh() = runTest {
        // given
        val nowSeconds = System.currentTimeMillis() / 1000
        db.coinDao().addCoins(
            CoinEntity(
                id = 1,
                rank = "1",
                symbol = "BTC",
                name = "Bitcoin",
                currentPrice = 0.0,
                marketCap = 0.0,
                priceChange24h = 0.0,
                priceChangePercentage24h = 0.0,
                logoUrl = "",
                lastUpdate = nowSeconds
            )
        )

        // when
        val action = mediator.initialize()

        // then
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
    }

    /**
     * Verifies that initialize() launches initial refresh when cache is stale (> 1 hour old).
     */
    @Test
    fun initialize_launchesInitialRefresh_whenCacheIsStale() = runTest {
        // given
        val nowSeconds = System.currentTimeMillis() / 1000
        db.coinDao().addCoins(
            CoinEntity(
                id = 2,
                rank = "2",
                symbol = "ETH",
                name = "Ethereum",
                currentPrice = 0.0,
                marketCap = 0.0,
                priceChange24h = 0.0,
                priceChangePercentage24h = 0.0,
                logoUrl = "",
                lastUpdate = nowSeconds - (2 * 60 * 60) // 2 hours ago in seconds
            )
        )

        // when
        val action = mediator.initialize()

        // then
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
    }

    /**
     * Verifies that a `PREPEND` load type immediately returns a success result
     * with `endOfPaginationReached` set to true, without modifying the database.
     * Prepending is not supported in this implementation.
     */
    @Test
    fun prepend_immediatelySignalsEnd_and_doesNotChangeDb() = runTest {
        // given
        val pageSize = 10
        val before = local.getItemsCount()

        // when
        val result = mediator.load(LoadType.PREPEND, pagingState(pageSize))

        // then
        assertThat(result).isInstanceOf(Success::class.java)
        val success = result as Success
        assertThat(success.endOfPaginationReached).isTrue()
        val after = local.getItemsCount()
        assertThat(after).isEqualTo(before)
    }
}

private class FakeCoinRemoteDataSource : CoinRemoteDataSource {
    private var coins: List<Coin> = emptyList()
    private var fail: Boolean = false

    fun setCoins(list: List<Coin>) {
        coins = list
        fail = false
    }

    fun setFailOnce() {
        fail = true
    }

    override suspend fun getCoins(
        pageSize: Int,
        page: Int,
        sortBy: String,
        sortDirection: String
    ): Result<List<Coin>, NetworkError> {
        if (fail) {
            fail = false
            return Result.Error(NetworkError.TimeoutError())
        }
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= coins.size) return Result.Success(emptyList())
        val toIndex = minOf(page * pageSize, coins.size)
        return Result.Success(coins.subList(fromIndex, toIndex))
    }

    override suspend fun getCoinHistory(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return Result.Success(emptyList())
    }
}
