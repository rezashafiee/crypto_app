package com.tilda.feature.crypto.data.repository

import androidx.paging.Pager
import com.tilda.core.domain.util.Result
import com.tilda.core.domain.util.Result.Success
import com.tilda.feature.crypto.data.datasource.CoinRemoteDataSource
import com.tilda.feature.crypto.domain.model.CoinPrice
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

class CoinRepositoryImpTest {

    @MockK(relaxed = true)
    lateinit var pager: Pager<Int, com.tilda.core.data.db.model.CoinEntity>

    @MockK(relaxed = true)
    lateinit var remote: CoinRemoteDataSource

    private lateinit var repo: CoinRepositoryImp

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = CoinRepositoryImp(pager, remote)
    }

    @Test
    fun getCoinsHistory_delegatesToRemoteDataSource() = runTest {
        val end = ZonedDateTime.now()
        val expected = listOf(
            CoinPrice(1.0, 2.0, 0.5, 1.5, end, 100.0)
        )
        coEvery { remote.getCoinHistory("btc", end) } returns Success(expected)

        val result = repo.getCoinsHistory("btc", end)

        coVerify(exactly = 1) { remote.getCoinHistory("btc", end) }
        assert(result is Result.Success && result.data == expected)
    }
}
