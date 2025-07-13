package com.example.dailyexpensetracker.core.extensions

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText

fun Context.toast(message: String, duration: Int = LENGTH_SHORT) {
    makeText(this, message, duration).show()
}