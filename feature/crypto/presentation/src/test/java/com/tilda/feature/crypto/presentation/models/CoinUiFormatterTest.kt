package com.tilda.feature.crypto.presentation.models

import com.google.common.truth.Truth.assertThat
import com.tilda.feature.crypto.domain.model.Coin
import java.util.Locale
import org.junit.After
import org.junit.Test

class CoinUiFormatterTest {

    private val originalLocale = Locale.getDefault()

    @After
    fun tearDown() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun toDisplayablePrice_usesReferenceChangeToKeepMeaningfulDecimals() {
        // given
        Locale.setDefault(Locale.GERMANY)

        // when
        val price = 1.0000035.toDisplayablePrice(referenceChange = 0.0000035)

        // then
        assertThat(price.formatted).isEqualTo("1,0000035")
    }

    @Test
    fun toDisplayablePrice_usesFallbackValueWhenReferenceChangeIsZero() {
        // given
        Locale.setDefault(Locale.US)

        // when
        val price = 0.0000035.toDisplayablePrice(referenceChange = 0.0)

        // then
        assertThat(price.formatted).isEqualTo("0.0000035")
    }

    @Test
    fun toDisplayablePrice_keepsLargeChangesReadable() {
        // given
        Locale.setDefault(Locale.US)

        // when
        val price = 57435.28628593438.toDisplayablePrice(referenceChange = 10000.0)

        // then
        assertThat(price.formatted).isEqualTo("57,435.29")
    }

    @Test
    fun toDisplayablePrice_keepsPrecisionForLargerPricesWhenChangeIsTiny() {
        // given
        Locale.setDefault(Locale.US)

        // when
        val price = 1234.0000035.toDisplayablePrice(referenceChange = 0.0000035)

        // then
        assertThat(price.formatted).isEqualTo("1,234.0000035")
    }

    @Test
    fun toDisplayableNumber_stillUsesTwoDecimalsForGeneralNumbers() {
        // given
        Locale.setDefault(Locale.GERMANY)

        // when
        val number = 1.0000035.toDisplayableNumber()

        // then
        assertThat(number.formatted).isEqualTo("1,00")
    }

    @Test
    fun toCoinUi_formatsCurrentPriceAndChangeWithSharedChangePrecision() {
        // given
        Locale.setDefault(Locale.GERMANY)

        // when
        val coin = Coin(
            id = 1,
            rank = "1",
            symbol = "LOW",
            name = "Low Price Coin",
            currentPrice = 1.00000356,
            marketCap = 100000000.0,
            priceChange24h = 0.0000035,
            priceChangePercentage24h = 1.2345,
            logoUrl = "",
            lastUpdate = 0L
        ).toCoinUi()

        // then
        assertThat(coin.currentPrice.formatted).isEqualTo("$1,0000036")
        assertThat(coin.priceChange24h.formatted).isEqualTo("$0,0000035")
        assertThat(coin.priceChangePercentage24h.formatted).isEqualTo("1,23")
    }
}
