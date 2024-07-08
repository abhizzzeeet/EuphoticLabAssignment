package com.example.euphoticlabsassignment.retrofit

import com.example.euphoticlabsassignment.models.Recommendation
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {
    @GET("/dev/nosh-assignment")
    fun getRecommendation() : Call<List<Recommendation>>
}