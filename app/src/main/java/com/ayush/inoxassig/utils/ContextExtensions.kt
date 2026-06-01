package com.ayush.inoxassig.utils

import android.content.Context
import java.io.IOException

/**
 * Reads a file from the assets folder and returns its content as a String.
 */
fun Context.readJsonFromAssets(fileName: String): String? {
    return try {
        assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}