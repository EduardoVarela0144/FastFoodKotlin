package com.example.fastfood.model

import java.io.Serializable

data class Restaurant(
    val name:String,
    var opening_hours:Hours,
    val photos:List<Photos>,
    val vicinity:String,
    val user_ratings_total:Int,
    val rating:Double,
    val reference:String,
    ): Serializable

