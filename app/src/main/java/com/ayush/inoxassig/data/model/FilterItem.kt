package com.ayush.inoxassig.data.model

data class FilterItem(
    val name: String,
    val type: Int,
    val icon: Int? = null,
    val selected: Boolean = false // Make this a val for immutability
) {
    companion object {
        const val SWITCH = 1
        const val NORMAL = 2
    }
}