package com.dora.travel.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

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
    tertiaryContainer = IndustrialTertiaryContainer,
    onTertiaryContainer = IndustrialOnTertiaryContainer,
    error = IndustrialError,
    onError = IndustrialOnError,
    errorContainer = IndustrialErrorContainer,
    onErrorContainer = IndustrialOnErrorContainer,
    background = IndustrialSurface,
    onBackground = IndustrialOnSurface,
    surface = IndustrialContainer,
    onSurface = IndustrialOnSurface,
    surfaceVariant = IndustrialContainerHighest,
    onSurfaceVariant = IndustrialOnSurfaceVariant,
    outline = IndustrialOutline,
    outlineVariant = IndustrialOutlineVariant,
    inverseSurface = IndustrialInverseSurface,
    inverseOnSurface = IndustrialInverseOnSurface,
    surfaceTint = IndustrialSurfaceTint,
    surfaceDim = IndustrialSurfaceDim,
    surfaceBright = IndustrialSurfaceBright,
    surfaceContainerLowest = IndustrialContainerLowest,
    surfaceContainerLow = IndustrialContainerLow,
    surfaceContainer = IndustrialContainer,
    surfaceContainerHigh = IndustrialContainerHigh,
    surfaceContainerHighest = IndustrialContainerHighest,
)

@Composable
fun DoraTheme(
    content: @Composable () -> Unit
) {
    val radii = TravelRadii()

    CompositionLocalProvider(
        LocalTravelSpacing provides TravelSpacing(),
        LocalTravelRadii provides radii,
        LocalTravelGlass provides TravelGlass(),
        LocalTravelGradients provides TravelGradients(),
    ) {
        MaterialTheme(
            colorScheme = IndustrialEtherealColorScheme,
            typography = Typography,
            shapes = Shapes(
                extraSmall = RoundedCornerShape(radii.small),
                small = RoundedCornerShape(radii.default),
                medium = RoundedCornerShape(radii.medium),
                large = RoundedCornerShape(radii.large),
                extraLarge = RoundedCornerShape(radii.extraLarge),
            ),
            content = content
        )
    }
}
