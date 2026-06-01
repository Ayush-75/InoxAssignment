package com.ayush.inoxassig.data.repository

import android.content.Context
import com.ayush.inoxassig.data.model.FnbResponse
import com.ayush.inoxassig.data.model.FoodItem
import com.ayush.inoxassig.utils.readJsonFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val context: Context,
    private val json: Json
) : FoodRepository {

    override suspend fun getFoodItems(): List<FoodItem> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.readJsonFromAssets("fnb.json")
            if (jsonString != null) {
                val response = json.decodeFromString<FnbResponse>(jsonString)
                response.listOfFnbItems
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}