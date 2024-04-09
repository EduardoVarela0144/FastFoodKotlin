package com.example.fastfood.model

import java.io.Serializable

data class RestaurantResponse (
    val results: List<Restaurant>
): Serializable