package com.ayush.inoxassig.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FoodItem(
    @SerialName("addOnItems")
    val addOnItems: List<AddOnItem> = emptyList(),
    @SerialName("calories")
    val calories: String = "",
    @SerialName("comboListItems")
    val comboListItems: List<ComboItems> = emptyList(),
    @SerialName("foodType")
    val foodType: String,
    @SerialName("isAddOnAvailable")
    val isAddOnAvailable: Boolean,
    @SerialName("isComboAvailable")
    val isComboAvailable: Boolean,
    @SerialName("isPopuplarItem")
    val isPopuplarItem: Boolean,
    @SerialName("isRepeat")
    val isRepeat: Boolean,
    @SerialName("itemCategory")
    val itemCategory: String,
    @SerialName("itemId")
    val itemId: String,
    @SerialName("itemImageURL")
    val itemImageURL: String,
    @SerialName("itemName")
    val itemName: String,
    @SerialName("itemOfferRate")
    val itemOfferRate: Double,
    @SerialName("itemRate")
    val itemRate: Double,
    @SerialName("itemWeight")
    val itemWeight: String = "",

    // IMPORTANT: Local state for cart logic.
    // @Transient prevents it from being looked for in the JSON.
    @Transient var quantity: Int = 0
)