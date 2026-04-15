package com.tilda.feature.crypto.data.remote

import com.tilda.feature.crypto.data.dto.CoinHistoryResponse
import com.tilda.feature.crypto.data.dto.CoinListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApiService {

    @GET("asset/v1/top/list")
    suspend fun getCoins(
        @Query("page_size") pageSize: Int,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
        @Query("sort_direction") sortDirection: String
    ): Response<CoinListResponse>

    @GET("index/cc/v1/historical/hours")
    suspend fun getCoinHistory(
        @Query("market") market: String,
        @Query("instrument") instrument: String,
        @Query("limit") limit: Int,
        @Query("to_ts") toTs: Long
    ): Response<CoinHistoryResponse>
}

