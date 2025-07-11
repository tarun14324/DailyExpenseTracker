package com.example.dailyexpensetracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DailyExpenseTrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}