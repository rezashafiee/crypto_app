package com.tilda.feature.crypto.domain.interactor

import com.tilda.core.domain.util.NetworkError
import com.tilda.core.domain.util.Result
import com.tilda.feature.crypto.domain.model.CoinPrice
import com.tilda.feature.crypto.domain.repository.CoinRepository
import java.time.ZonedDateTime

class GetCoinHistoryUseCase(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(
        coinSymbol: String,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        return coinRepository.getCoinsHistory(coinSymbol, end)
    }
}