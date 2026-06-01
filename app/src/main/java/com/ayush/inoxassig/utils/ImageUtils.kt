package com.ayush.inoxassig.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtils {
    /**
     * Decodes a Base64 string into a Bitmap.
     * Returns null if the string is empty or invalid.
     */
    fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
        if (base64String.isNullOrEmpty()) return null
        return try {
            // Some Base64 strings might have headers like "data:image/jpeg;base64,"
            // If your JSON has these, you'd need to strip them.
            val cleanString = base64String.substringAfter(",")
            val decodedBytes = Base64.decode(cleanString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}