package com.smart.smartbalibackpaker.core.data.source.remote.service

import com.smart.smartbalibackpaker.core.data.source.remote.response.NearbySearchResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("nearbysearch/json")
    fun searchNearby(
        @Query("keyword") keyword : String,
        @Query("location") location : String,
        @Query("radius") radius : String,
        @Query("key") key: String
    ): Call<NearbySearchResponse>
}