package com.example.binchecker.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.BiasAbsoluteAlignment
import kotlin.math.roundToInt

fun enterScreenTransition(
    expandFrom: BiasAbsoluteAlignment.Horizontal,
    animationTime: Int = 650,
    delayTimePercent: Float = .05f,
): EnterTransition {
    val delayMillis = (animationTime * delayTimePercent).roundToInt()
    val durationMillis = animationTime - delayMillis
    return slideInHorizontally(
        animationSpec = tween(durationMillis, delayMillis, easing = FastOutSlowInEasing)
    ) { fullWidth ->
        (expandFrom.bias * fullWidth).roundToInt()
    }
}


fun exitScreenTransition(
    shrinkTowards: BiasAbsoluteAlignment.Horizontal,
    animationTime: Int = 650,
    delayTimePercent: Float = .2f
): ExitTransition {
    val delayMillis = (animationTime * delayTimePercent).roundToInt()
    val durationMillis = animationTime - delayMillis
    return slideOutHorizontally(
        animationSpec = tween(durationMillis, delayMillis, easing = FastOutSlowInEasing),
    ) { fullWidth ->
        (shrinkTowards.bias * fullWidth).roundToInt()
    }
}