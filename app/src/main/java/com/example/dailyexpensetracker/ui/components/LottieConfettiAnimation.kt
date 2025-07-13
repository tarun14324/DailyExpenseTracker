package com.example.dailyexpensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.ui.theme.size16

@Composable
fun LottieConfettiAnimation() {
    // Load the Lottie Composition from the raw resource
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.add_expense_success_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    // Display Lottie animation in center
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(size16),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
        )
    }
}