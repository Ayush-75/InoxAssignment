package com.ayush.inoxassig.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CinemaDetail(
    @SerialName("onSeatAvailable")
    val onSeatAvailable: Boolean,
    @SerialName("screenId")
    val screenId: String = "",
    @SerialName("seat")
    val seat: String = "",
    @SerialName("theaterId")
    val theaterId: String,
    @SerialName("theaterName")
    val theaterName: String
)