package com.example.android.politicalpreparedness.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() { this.visibility = View.VISIBLE }

fun View.gone() { this.visibility = View.GONE }

fun Date.toSimpleString(): String {
    val format = SimpleDateFormat("EEE dd MMM yyyy", Locale.US)
    return format.format(this)
}