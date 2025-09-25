package com.tilda.feature.crypto.domain.interactor

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.tilda.feature.crypto.domain.model.Coin
import com.tilda.feature.crypto.domain.repository.CoinListRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@Suppress("UnusedFlow")
class GetPagedCoinsUseCaseTest {

    private val repository: CoinListRepository = mockk()
    private val useCase = GetPagedCoinsUseCase(repository)

    @Test
    fun `invoke should return paged coins from repository`() = runTest {
        // Given
        val fakeCoin = Coin(
            id = 1,
            rank = "1",
            symbol = "BTC",
            "Bitcoin",
            currentPrice = 1.10000,
            marketCap = 1.10000,
            priceChangePercentage24h = 1.10000,
            logoUrl = "image",
            lastUpdate = 1L,
            priceChange24h = 1.10000
        )
        val pagingDataFlow = flowOf(PagingData.from(listOf(fakeCoin)))
        every { repository.getPagedDomainCoins() } returns pagingDataFlow

        // When
        val result = useCase.invoke()

        // Then
        assert(result.asSnapshot().size == 1)
        assert(result.asSnapshot().first() == fakeCoin)

        verify(exactly = 1) {
            repository.getPagedDomainCoins()
        }
        confirmVerified(repository)
    }

}
