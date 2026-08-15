package com.dora.travel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Industrial Ethereal - dark palette.
val IndustrialDarkSurface = Color(0xFF081425)
val IndustrialDarkSurfaceDim = Color(0xFF081425)
val IndustrialDarkSurfaceBright = Color(0xFF2F3A4C)

val IndustrialDarkContainerLowest = Color(0xFF040E1F)
val IndustrialDarkContainerLow = Color(0xFF111C2D)
val IndustrialDarkContainer = Color(0xFF152031)
val IndustrialDarkContainerHigh = Color(0xFF1F2A3C)
val IndustrialDarkContainerHighest = Color(0xFF2A3548)

val IndustrialDarkOnSurface = Color(0xFFD8E3FB)
val IndustrialDarkOnSurfaceVariant = Color(0xFFC4C5D9)
val IndustrialDarkInverseSurface = Color(0xFFD8E3FB)
val IndustrialDarkInverseOnSurface = Color(0xFF263143)

val IndustrialDarkPrimary = Color(0xFFB8C3FF)
val IndustrialDarkOnPrimary = Color(0xFF002387)
val IndustrialDarkPrimaryContainer = Color(0xFF2D5BFF)
val IndustrialDarkOnPrimaryContainer = Color(0xFFEFEFFF)

val IndustrialDarkSecondary = Color(0xFFEDB1FF)
val IndustrialDarkOnSecondary = Color(0xFF520070)
val IndustrialDarkSecondaryContainer = Color(0xFF6E208C)
val IndustrialDarkOnSecondaryContainer = Color(0xFFE498FF)

val IndustrialDarkTertiary = Color(0xFFBEC6E0)
val IndustrialDarkOnTertiary = Color(0xFF283044)
val IndustrialDarkTertiaryContainer = Color(0xFF656D84)
val IndustrialDarkOnTertiaryContainer = Color(0xFFEDF0FF)

val IndustrialDarkError = Color(0xFFFFB4AB)
val IndustrialDarkOnError = Color(0xFF690005)
val IndustrialDarkErrorContainer = Color(0xFF93000A)
val IndustrialDarkOnErrorContainer = Color(0xFFFFDAD6)

val IndustrialDarkOutline = Color(0xFF8E90A2)
val IndustrialDarkOutlineVariant = Color(0xFF434656)
val IndustrialDarkSurfaceTint = Color(0xFFB8C3FF)

// Industrial Ethereal - light palette.
val IndustrialLightSurface = Color(0xFFF8F9FF)
val IndustrialLightSurfaceDim = Color(0xFFD8DAE6)
val IndustrialLightSurfaceBright = Color(0xFFF8F9FF)

val IndustrialLightContainerLowest = Color(0xFFFFFFFF)
val IndustrialLightContainerLow = Color(0xFFF1F3FB)
val IndustrialLightContainer = Color(0xFFE9ECF6)
val IndustrialLightContainerHigh = Color(0xFFE1E5F0)
val IndustrialLightContainerHighest = Color(0xFFD9DEEA)

val IndustrialLightOnSurface = Color(0xFF171B28)
val IndustrialLightOnSurfaceVariant = Color(0xFF4A4D5C)
val IndustrialLightInverseSurface = Color(0xFF2C303D)
val IndustrialLightInverseOnSurface = Color(0xFFF0F2FC)

val IndustrialLightPrimary = Color(0xFF365CE8)
val IndustrialLightOnPrimary = Color(0xFFFFFFFF)
val IndustrialLightPrimaryContainer = Color(0xFFDDE1FF)
val IndustrialLightOnPrimaryContainer = Color(0xFF00164F)

val IndustrialLightSecondary = Color(0xFF8A2FA7)
val IndustrialLightOnSecondary = Color(0xFFFFFFFF)
val IndustrialLightSecondaryContainer = Color(0xFFF8D8FF)
val IndustrialLightOnSecondaryContainer = Color(0xFF320045)

val IndustrialLightTertiary = Color(0xFF565D72)
val IndustrialLightOnTertiary = Color(0xFFFFFFFF)
val IndustrialLightTertiaryContainer = Color(0xFFDCE2F9)
val IndustrialLightOnTertiaryContainer = Color(0xFF131A2B)

val IndustrialLightError = Color(0xFFBA1A1A)
val IndustrialLightOnError = Color(0xFFFFFFFF)
val IndustrialLightErrorContainer = Color(0xFFFFDAD6)
val IndustrialLightOnErrorContainer = Color(0xFF410002)

val IndustrialLightOutline = Color(0xFF76798A)
val IndustrialLightOutlineVariant = Color(0xFFC7CAD9)
val IndustrialLightSurfaceTint = Color(0xFF365CE8)

/**
 * 旧业务页面使用的语义色入口。
 *
 * 这些名称不再固定指向暗色值，而是读取当前 [MaterialTheme.colorScheme]。
 * 这样页面继续使用 `IndustrialSurface`、`IndustrialPrimary` 等业务语义名时，
 * 会自动跟随系统白昼/黑夜主题切换。
 */
val IndustrialSurface: Color
    @Composable get() = MaterialTheme.colorScheme.background

val IndustrialContainerLow: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceContainerLow

val IndustrialContainer: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceContainer

val IndustrialContainerHigh: Color
    @Composable get() = MaterialTheme.colorScheme.surfaceContainerHigh

val IndustrialOnSurface: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

val IndustrialOnSurfaceVariant: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

val IndustrialPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.primary

val IndustrialOnPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.onPrimary

val IndustrialPrimaryContainer: Color
    @Composable get() = MaterialTheme.colorScheme.primaryContainer

val IndustrialOnPrimaryContainer: Color
    @Composable get() = MaterialTheme.colorScheme.onPrimaryContainer

val IndustrialSecondary: Color
    @Composable get() = MaterialTheme.colorScheme.secondary

val IndustrialSecondaryContainer: Color
    @Composable get() = MaterialTheme.colorScheme.secondaryContainer

val IndustrialTertiary: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary

val IndustrialError: Color
    @Composable get() = MaterialTheme.colorScheme.error

val IndustrialErrorContainer: Color
    @Composable get() = MaterialTheme.colorScheme.errorContainer

val IndustrialOutline: Color
    @Composable get() = MaterialTheme.colorScheme.outline

// Gradients
val RoamingGradient = listOf(IndustrialDarkPrimaryContainer, Color(0xFF9D50BB))

// Legacy compatibility
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
