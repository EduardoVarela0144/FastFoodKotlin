package com.example.fastfood.services
import com.example.fastfood.model.Restaurant
import com.example.fastfood.model.RestaurantDirection
import com.example.fastfood.model.RestaurantResponse

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call


interface RestaurantApiService {
    @GET("nearbysearch/json")
    fun  getRestaurantData(
        @Query("location") location:String,
        @Query("radius") radius:String,
        @Query("type") type:String,
        @Query("key") key:String
    ):Call<RestaurantResponse>

    @GET("details/json")
    fun  getRestaurantDirection(
        @Query("place_id") place_id:String,
        @Query("fields") fields:String,
        @Query("key") key:String
    ):Call<RestaurantDirection>
}