package com.smart.smartbalibackpaker.core.data.source.remote.service

import com.smart.smartbalibackpaker.core.model.ResponseRoute
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteService {
    @GET("json")
    fun requestRoute(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ) : Call<ResponseRoute>
}