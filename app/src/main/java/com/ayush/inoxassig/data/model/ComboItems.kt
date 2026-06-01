package com.ayush.inoxassig.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComboItems(
    @SerialName("foodType")
    val foodType: String,
    @SerialName("itemAmount")
    val itemAmount: Double,
    @SerialName("itemBasePrice")
    val itemBasePrice: Double,
    @SerialName("itemCGSTTaxPer")
    val itemCGSTTaxPer: Double,
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("itemOfferBasePrice")
    val itemOfferBasePrice: Double,
    @SerialName("itemOfferTaxValue")
    val itemOfferTaxValue: Double,
    @SerialName("itemOtherTaxPer")
    val itemOtherTaxPer: Double,
    @SerialName("itemQty")
    val itemQty: Int,
    @SerialName("itemRate")
    val itemRate: Double,
    @SerialName("itemSGSTTaxPer")
    val itemSGSTTaxPer: Double,
    @SerialName("itemSaleAmount")
    val itemSaleAmount: Double,
    @SerialName("itemSaleRate")
    val itemSaleRate: Double,
    @SerialName("itemTaxValue")
    val itemTaxValue: Double,
    @SerialName("itemUTGSTTaxPer")
    val itemUTGSTTaxPer: Double
)