package com.example.dailyexpensetracker.core.utils

import android.os.Build
import androidx.compose.ui.graphics.Color
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

object Utils {

    fun formatDateToHumanReadableForm(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatCurrency(amount: Double): String {
        val indianLocale = Locale.Builder()
            .setLanguage("en")
            .setRegion("IN")
            .build()
        val currencyFormatter = NumberFormat.getCurrencyInstance(indianLocale)
        return currencyFormatter.format(amount).dropLast(3)
    }

    fun formatDayMonthYear(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDayMonth(dateInMillis: String): String {
        val dateFormatter = SimpleDateFormat("dd/MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatStringDateToMonthDayYear(date: String): String {
        val millis = getMillisFromDate(date)
        return formatDayMonthYear(millis)
    }

    private fun getMillisFromDate(date: String): Long {
        return getMilliFromDate(date)
    }

    private fun getMilliFromDate(dateFormat: String?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
    }

    fun getGreetingMessage(): String {
        val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalTime.now().hour
        } else {
            val calendar = java.util.Calendar.getInstance()
            calendar.get(java.util.Calendar.HOUR_OF_DAY)
        }

        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}