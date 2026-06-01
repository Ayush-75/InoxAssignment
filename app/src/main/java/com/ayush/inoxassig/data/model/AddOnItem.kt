package com.ayush.inoxassig.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddOnItem(
    @SerialName("addonItemId")
    val addonItemId: String,
    @SerialName("addonItemName")
    val addonItemName: String,
    @SerialName("addonItemRate")
    val addonItemRate: Double
)