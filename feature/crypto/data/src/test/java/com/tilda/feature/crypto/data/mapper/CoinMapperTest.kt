package com.tilda.feature.crypto.data.mapper

import com.tilda.core.data.db.model.CoinEntity
import com.tilda.feature.crypto.data.dto.CoinPriceDto
import com.tilda.feature.crypto.domain.model.Coin
import org.junit.Test
import java.time.ZoneId
import org.junit.Assert.assertEquals

class CoinMapperTest {

    @Test
    fun coinEntity_toCoin_mapsAllFields() {
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

        val coin = entity.toCoin()

        assertEquals(entity.id, coin.id)
        assertEquals(entity.name, coin.name)
        assertEquals(entity.currentPrice, coin.currentPrice, 0.0)
        assertEquals(entity.priceChangePercentage24h, coin.priceChangePercentage24h, 0.0)
        assertEquals(entity.priceChange24h, coin.priceChange24h, 0.0)
        assertEquals(entity.marketCap, coin.marketCap, 0.0)
        assertEquals(entity.rank, coin.rank)
        assertEquals(entity.symbol, coin.symbol)
        assertEquals(entity.logoUrl, coin.logoUrl)
        assertEquals(entity.lastUpdate, coin.lastUpdate)
    }

    @Test
    fun coin_toCoinEntity_roundTrip_preservesData() {
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

        val entity = coin.toCoinEntity()
        val backToCoin = entity.toCoin()

        assertEquals(coin.id, entity.id)
        assertEquals(coin.name, entity.name)
        assertEquals(coin.currentPrice, entity.currentPrice, 0.0)
        assertEquals(coin.priceChangePercentage24h, entity.priceChangePercentage24h, 0.0)
        assertEquals(coin.priceChange24h, entity.priceChange24h, 0.0)
        assertEquals(coin.marketCap, entity.marketCap, 0.0)
        assertEquals(coin.rank, entity.rank)
        assertEquals(coin.symbol, entity.symbol)
        assertEquals(coin.logoUrl, entity.logoUrl)
        assertEquals(coin.lastUpdate, entity.lastUpdate)

        // And mapping back should give the same Coin values
        assertEquals(coin.id, backToCoin.id)
        assertEquals(coin.name, backToCoin.name)
        assertEquals(coin.currentPrice, backToCoin.currentPrice, 0.0)
        assertEquals(coin.priceChangePercentage24h, backToCoin.priceChangePercentage24h, 0.0)
        assertEquals(coin.priceChange24h, backToCoin.priceChange24h, 0.0)
        assertEquals(coin.marketCap, backToCoin.marketCap, 0.0)
        assertEquals(coin.rank, backToCoin.rank)
        assertEquals(coin.symbol, backToCoin.symbol)
        assertEquals(coin.logoUrl, backToCoin.logoUrl)
        assertEquals(coin.lastUpdate, backToCoin.lastUpdate)
    }

    @Test
    fun coinPriceDto_toCoinPrice_convertsTimestampToUtcZonedDateTime() {
        val dto = CoinPriceDto(
            closingPrice = 101.0,
            openingPrice = 99.5,
            highestPrice = 105.0,
            lowestPrice = 98.0,
            timestamp = 1_700_000_000L,
            volume = 12345.0
        )

        val coinPrice = dto.toCoinPrice()

        assertEquals(dto.closingPrice, coinPrice.closingPrice, 0.0)
        assertEquals(dto.openingPrice, coinPrice.openingPrice, 0.0)
        assertEquals(dto.highestPrice, coinPrice.highestPrice, 0.0)
        assertEquals(dto.lowestPrice, coinPrice.lowestPrice, 0.0)
        assertEquals(dto.volume, coinPrice.volume, 0.0)
        assertEquals(ZoneId.of("UTC"), coinPrice.dateTime.zone)
        assertEquals(dto.timestamp, coinPrice.dateTime.toEpochSecond())
    }
}
