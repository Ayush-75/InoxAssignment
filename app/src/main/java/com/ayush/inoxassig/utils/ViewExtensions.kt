package com.ayush.inoxassig.utils

import android.view.View

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(show: Boolean) {
    if (show) visible() else gone()
}