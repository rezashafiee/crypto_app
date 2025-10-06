package com.tilda.feature.crypto.data.mapper

import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.dto.CoinPriceDto
import com.tilda.feature.crypto.domain.model.Coin
import org.junit.Test
import java.time.ZoneId
import com.google.common.truth.Truth.assertThat

class CoinMapperTest {

    @Test
    fun coinEntity_toCoin_mapsAllFields() {
        // given
        val entity = CoinEntity(
            id = 10,
            rank = "5",
            symbol = "SOL",
            name = "Solana",
            currentPrice = 150.5,
            marketCap = 50_000.0,
            priceChange24h = -2.5,
            priceChangePercentage24h = -1.25,
            logoUrl = "https://example.com/sol.png",
            lastUpdate = 1_700_000_000L
        )

        // when
        val coin = entity.toCoin()

        // then
        assertThat(coin.id).isEqualTo(entity.id)
        assertThat(coin.name).isEqualTo(entity.name)
        assertThat(coin.currentPrice).isEqualTo(entity.currentPrice)
        assertThat(coin.priceChangePercentage24h).isEqualTo(entity.priceChangePercentage24h)
        assertThat(coin.priceChange24h).isEqualTo(entity.priceChange24h)
        assertThat(coin.marketCap).isEqualTo(entity.marketCap)
        assertThat(coin.rank).isEqualTo(entity.rank)
        assertThat(coin.symbol).isEqualTo(entity.symbol)
        assertThat(coin.logoUrl).isEqualTo(entity.logoUrl)
        assertThat(coin.lastUpdate).isEqualTo(entity.lastUpdate)
    }

    @Test
    fun coin_toCoinEntity_roundTrip_preservesData() {
        // given
        val coin = Coin(
            id = 7,
            name = "Cardano",
            currentPrice = 0.33,
            priceChangePercentage24h = 3.14,
            priceChange24h = 0.01,
            marketCap = 12_345.67,
            rank = "9",
            symbol = "ADA",
            logoUrl = "https://example.com/ada.png",
            lastUpdate = 1_690_000_000L
        )

        // when
        val entity = coin.toCoinEntity()
        val backToCoin = entity.toCoin()

        // then
        assertThat(entity.id).isEqualTo(coin.id)
        assertThat(entity.name).isEqualTo(coin.name)
        assertThat(entity.currentPrice).isEqualTo(coin.currentPrice)
        assertThat(entity.priceChangePercentage24h).isEqualTo(coin.priceChangePercentage24h)
        assertThat(entity.priceChange24h).isEqualTo(coin.priceChange24h)
        assertThat(entity.marketCap).isEqualTo(coin.marketCap)
        assertThat(entity.rank).isEqualTo(coin.rank)
        assertThat(entity.symbol).isEqualTo(coin.symbol)
        assertThat(entity.logoUrl).isEqualTo(coin.logoUrl)
        assertThat(entity.lastUpdate).isEqualTo(coin.lastUpdate)

        // And mapping back should give the same Coin values
        assertThat(backToCoin.id).isEqualTo(coin.id)
        assertThat(backToCoin.name).isEqualTo(coin.name)
        assertThat(backToCoin.currentPrice).isEqualTo(coin.currentPrice)
        assertThat(backToCoin.priceChangePercentage24h).isEqualTo(coin.priceChangePercentage24h)
        assertThat(backToCoin.priceChange24h).isEqualTo(coin.priceChange24h)
        assertThat(backToCoin.marketCap).isEqualTo(coin.marketCap)
        assertThat(backToCoin.rank).isEqualTo(coin.rank)
        assertThat(backToCoin.symbol).isEqualTo(coin.symbol)
        assertThat(backToCoin.logoUrl).isEqualTo(coin.logoUrl)
        assertThat(backToCoin.lastUpdate).isEqualTo(coin.lastUpdate)
    }

    @Test
    fun coinPriceDto_toCoinPrice_convertsTimestampToUtcZonedDateTime() {
        // given
        val dto = CoinPriceDto(
            closingPrice = 101.0,
            openingPrice = 99.5,
            highestPrice = 105.0,
            lowestPrice = 98.0,
            timestamp = 1_700_000_000L,
            volume = 12345.0
        )

        // when
        val coinPrice = dto.toCoinPrice()

        // then
        assertThat(coinPrice.closingPrice).isEqualTo(dto.closingPrice)
        assertThat(coinPrice.openingPrice).isEqualTo(dto.openingPrice)
        assertThat(coinPrice.highestPrice).isEqualTo(dto.highestPrice)
        assertThat(coinPrice.lowestPrice).isEqualTo(dto.lowestPrice)
        assertThat(coinPrice.volume).isEqualTo(dto.volume)
        assertThat(coinPrice.dateTime.zone).isEqualTo(ZoneId.of("UTC"))
        assertThat(coinPrice.dateTime.toEpochSecond()).isEqualTo(dto.timestamp)
    }
}
