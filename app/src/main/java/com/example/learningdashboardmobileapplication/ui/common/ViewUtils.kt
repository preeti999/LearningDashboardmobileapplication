package com.example.learningdashboardmobileapplication.ui.common

import android.content.Context
import android.view.View
import kotlin.math.roundToInt

fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).roundToInt()

fun View.showIf(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

