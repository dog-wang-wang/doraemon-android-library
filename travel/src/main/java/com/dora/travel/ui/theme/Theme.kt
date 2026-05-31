package com.dora.travel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val IndustrialEtherealColorScheme = darkColorScheme(
    primary = IndustrialPrimary,
    onPrimary = IndustrialOnPrimary,
    primaryContainer = IndustrialPrimaryContainer,
    onPrimaryContainer = IndustrialOnPrimaryContainer,
    secondary = IndustrialSecondary,
    onSecondary = IndustrialOnSecondary,
    secondaryContainer = IndustrialSecondaryContainer,
    onSecondaryContainer = IndustrialOnSecondaryContainer,
    tertiary = IndustrialTertiary,
    onTertiary = IndustrialOnTertiary,
    error = IndustrialError,
    onError = IndustrialOnError,
    background = IndustrialSurface,
    onBackground = IndustrialOnSurface,
    surface = IndustrialContainer,
    onSurface = IndustrialOnSurface,
    surfaceVariant = IndustrialContainerHighest,
    onSurfaceVariant = IndustrialOnSurfaceVariant,
    outline = IndustrialOutline,
    outlineVariant = IndustrialOutlineVariant
)

@Composable
fun DoraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = IndustrialEtherealColorScheme,
        typography = Typography,
        content = content
    )
}
