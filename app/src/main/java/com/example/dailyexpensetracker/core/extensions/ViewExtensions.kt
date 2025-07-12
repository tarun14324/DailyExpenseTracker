package com.example.dailyexpensetracker.core.extensions

import android.content.Context
import android.view.View
import android.widget.Toast.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.isInvisible

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

fun View.isInVisible(): Boolean = this.isInvisible

fun View.isGone(): Boolean = this.visibility == View.GONE

fun Context.toast(message: String, duration: Int = LENGTH_SHORT) {
    makeText(this, message, duration).show()
}