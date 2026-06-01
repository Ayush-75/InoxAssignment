package com.ayush.inoxassig.data.repository

import com.ayush.inoxassig.data.model.FoodItem

interface FoodRepository {
    /**
     * Reads the local JSON and returns the list of F&B items.
     * Returns an empty list if an error occurs.
     */
    suspend fun getFoodItems(): List<FoodItem>
}