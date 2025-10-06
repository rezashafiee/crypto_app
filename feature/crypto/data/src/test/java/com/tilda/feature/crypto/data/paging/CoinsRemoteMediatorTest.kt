package com.tilda.feature.crypto.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tilda.core.data.db.model.CoinEntity
import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.data.datasource.CoinLocalDataSource
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.domain.model.Coin
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

@OptIn(ExperimentalPagingApi::class)
class CoinsRemoteMediatorTest {

    private val remote: CoinRemoteDataSource = mockk()
    private val local: CoinLocalDataSource = mockk()

    private fun buildMediator() = CoinsRemoteMediator(remote, local)

    private fun pagingState(pageSize: Int): PagingState<Int, CoinEntity> {
        val config = PagingConfig(pageSize = pageSize)
        // Empty state is enough for our mediator logic
        return PagingState(pages = listOf(), anchorPosition = null, config = config, leadingPlaceholderCount = 0)
    }

    private fun coin(id: Int): Coin = Coin(
        id = id,
        rank = "1",
        symbol = "C$id",
        name = "Coin$id",
        currentPrice = 1.0,
        marketCap = 1.0,
        priceChange24h = 0.0,
        priceChangePercentage24h = 0.0,
        logoUrl = "",
        lastUpdate = 0L
    )

    @Test
    fun initialize_returnsSkip_whenCacheIsFresh() = runTest {
        // given
        val nowSeconds = System.currentTimeMillis() / 1000
        coEvery { local.getLastUpdated() } returns nowSeconds

        // when
        val action = buildMediator().initialize()

        // then
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH)
    }

    @Test
    fun initialize_returnsLaunch_whenCacheIsStale() = runTest {
        // given
        val twoHoursAgoSeconds = System.currentTimeMillis() / 1000 - (2 * 60 * 60)
        coEvery { local.getLastUpdated() } returns twoHoursAgoSeconds

        // when
        val action = buildMediator().initialize()

        // then
        assertThat(action).isEqualTo(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH)
    }

    @Test
    fun load_refresh_success_replacesData_andSignalsEnd_whenLessThanPageSize() = runTest {
        // given
        val pageSize = 10
        val coins = listOf(coin(1), coin(2), coin(3))
        coEvery { remote.getCoins(pageSize = pageSize, page = 1, sortBy = any(), sortDirection = any()) } returns Result.Success(coins)
        coEvery { local.replaceAllCoins(any()) } returns Unit

        // when
        val result = buildMediator().load(LoadType.REFRESH, pagingState(pageSize))

        // then
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        val success = result as RemoteMediator.MediatorResult.Success
        assertThat(success.endOfPaginationReached).isTrue()

        coVerify(exactly = 1) { local.replaceAllCoins(match { it.size == coins.size }) }
        confirmVerified(local)
    }

    @Test
    fun load_append_success_addsData_andKeepsPaging_whenEqualToPageSize() = runTest {
        // given
        val pageSize = 10
        // When local has 20 items, next page should be 3 (20/10 + 1)
        coEvery { local.getItemsCount() } returns 20
        val coins = (1..pageSize).map { coin(it) }
        coEvery { remote.getCoins(pageSize = pageSize, page = 3, sortBy = any(), sortDirection = any()) } returns Result.Success(coins)
        coEvery { local.addCoins(any()) } returns Unit

        // when
        val result = buildMediator().load(LoadType.APPEND, pagingState(pageSize))

        // then
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        val success = result as RemoteMediator.MediatorResult.Success
        assertThat(success.endOfPaginationReached).isFalse()

        coVerify(exactly = 1) { local.addCoins(match { it.size == coins.size }) }
        coVerify(exactly = 1) { local.getItemsCount() }
        confirmVerified(local)
    }

    @Test
    fun load_prepend_immediatelySignalsEnd_withoutRemoteOrLocalCalls() = runTest {
        // given
        val pageSize = 10

        // when
        val result = buildMediator().load(LoadType.PREPEND, pagingState(pageSize))

        // then
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Success::class.java)
        val success = result as RemoteMediator.MediatorResult.Success
        assertThat(success.endOfPaginationReached).isTrue()

        // No calls expected to remote or local
        coVerify { local wasNot Called }
        coVerify { remote wasNot Called }
        confirmVerified(local)
        confirmVerified(remote)
    }

    @Test
    fun load_returnsError_whenRemoteFails() = runTest {
        // given
        val pageSize = 10
        coEvery { remote.getCoins(pageSize = pageSize, page = 1, sortBy = any(), sortDirection = any()) } returns Result.Error(NetworkError.TimeoutError())

        // when
        val result = buildMediator().load(LoadType.REFRESH, pagingState(pageSize))

        // then
        assertThat(result).isInstanceOf(RemoteMediator.MediatorResult.Error::class.java)
    }
}
