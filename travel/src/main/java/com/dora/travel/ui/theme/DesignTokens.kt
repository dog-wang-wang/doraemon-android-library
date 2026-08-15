package com.dora.travel.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TravelSpacing(
    val base: Dp = 8.dp,
    val containerPadding: Dp = 32.dp,
    val mobilePadding: Dp = 16.dp,
    val gutter: Dp = 24.dp,
    val stackSmall: Dp = 12.dp,
    val stackMedium: Dp = 24.dp,
    val stackLarge: Dp = 48.dp,
)

@Immutable
data class TravelRadii(
    val small: Dp = 8.dp,
    val default: Dp = 16.dp,
    val medium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 48.dp,
    val full: Dp = 999.dp,
)

@Immutable
data class TravelGlass(
    val cardAlpha: Float = 0.4f,
    val elevatedCardAlpha: Float = 0.6f,
    val borderAlpha: Float = 0.1f,
    val strongBorderAlpha: Float = 0.3f,
    val glowAlpha: Float = 0.15f,
    val navAlpha: Float = 0.6f,
)

@Immutable
data class TravelGradients(
    val roaming: List<Color> = RoamingGradient,
    val declarationAction: List<Color> = listOf(IndustrialDarkPrimary, IndustrialDarkPrimaryContainer),
)

val LocalTravelSpacing = compositionLocalOf { TravelSpacing() }
val LocalTravelRadii = compositionLocalOf { TravelRadii() }
val LocalTravelGlass = compositionLocalOf { TravelGlass() }
val LocalTravelGradients = compositionLocalOf { TravelGradients() }
