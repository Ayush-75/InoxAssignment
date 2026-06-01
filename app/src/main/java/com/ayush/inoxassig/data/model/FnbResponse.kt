package com.ayush.inoxassig.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FnbResponse(
    @SerialName("cinemaDetails")
    val cinemaDetails: List<CinemaDetail>,
    @SerialName("listOfFnbItems")
    val listOfFnbItems: List<FoodItem>,
    @SerialName("msg")
    val msg: String,
    @SerialName("status")
    val status: Int
)