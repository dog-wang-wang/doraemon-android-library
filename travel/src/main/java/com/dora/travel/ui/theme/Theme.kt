package com.dora.travel.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val IndustrialEtherealDarkColorScheme = darkColorScheme(
    primary = IndustrialDarkPrimary,
    onPrimary = IndustrialDarkOnPrimary,
    primaryContainer = IndustrialDarkPrimaryContainer,
    onPrimaryContainer = IndustrialDarkOnPrimaryContainer,
    secondary = IndustrialDarkSecondary,
    onSecondary = IndustrialDarkOnSecondary,
    secondaryContainer = IndustrialDarkSecondaryContainer,
    onSecondaryContainer = IndustrialDarkOnSecondaryContainer,
    tertiary = IndustrialDarkTertiary,
    onTertiary = IndustrialDarkOnTertiary,
    tertiaryContainer = IndustrialDarkTertiaryContainer,
    onTertiaryContainer = IndustrialDarkOnTertiaryContainer,
    error = IndustrialDarkError,
    onError = IndustrialDarkOnError,
    errorContainer = IndustrialDarkErrorContainer,
    onErrorContainer = IndustrialDarkOnErrorContainer,
    background = IndustrialDarkSurface,
    onBackground = IndustrialDarkOnSurface,
    surface = IndustrialDarkContainer,
    onSurface = IndustrialDarkOnSurface,
    surfaceVariant = IndustrialDarkContainerHighest,
    onSurfaceVariant = IndustrialDarkOnSurfaceVariant,
    outline = IndustrialDarkOutline,
    outlineVariant = IndustrialDarkOutlineVariant,
    inverseSurface = IndustrialDarkInverseSurface,
    inverseOnSurface = IndustrialDarkInverseOnSurface,
    surfaceTint = IndustrialDarkSurfaceTint,
    surfaceDim = IndustrialDarkSurfaceDim,
    surfaceBright = IndustrialDarkSurfaceBright,
    surfaceContainerLowest = IndustrialDarkContainerLowest,
    surfaceContainerLow = IndustrialDarkContainerLow,
    surfaceContainer = IndustrialDarkContainer,
    surfaceContainerHigh = IndustrialDarkContainerHigh,
    surfaceContainerHighest = IndustrialDarkContainerHighest,
)

private val IndustrialEtherealLightColorScheme = lightColorScheme(
    primary = IndustrialLightPrimary,
    onPrimary = IndustrialLightOnPrimary,
    primaryContainer = IndustrialLightPrimaryContainer,
    onPrimaryContainer = IndustrialLightOnPrimaryContainer,
    secondary = IndustrialLightSecondary,
    onSecondary = IndustrialLightOnSecondary,
    secondaryContainer = IndustrialLightSecondaryContainer,
    onSecondaryContainer = IndustrialLightOnSecondaryContainer,
    tertiary = IndustrialLightTertiary,
    onTertiary = IndustrialLightOnTertiary,
    tertiaryContainer = IndustrialLightTertiaryContainer,
    onTertiaryContainer = IndustrialLightOnTertiaryContainer,
    error = IndustrialLightError,
    onError = IndustrialLightOnError,
    errorContainer = IndustrialLightErrorContainer,
    onErrorContainer = IndustrialLightOnErrorContainer,
    background = IndustrialLightSurface,
    onBackground = IndustrialLightOnSurface,
    surface = IndustrialLightContainer,
    onSurface = IndustrialLightOnSurface,
    surfaceVariant = IndustrialLightContainerHighest,
    onSurfaceVariant = IndustrialLightOnSurfaceVariant,
    outline = IndustrialLightOutline,
    outlineVariant = IndustrialLightOutlineVariant,
    inverseSurface = IndustrialLightInverseSurface,
    inverseOnSurface = IndustrialLightInverseOnSurface,
    surfaceTint = IndustrialLightSurfaceTint,
    surfaceDim = IndustrialLightSurfaceDim,
    surfaceBright = IndustrialLightSurfaceBright,
    surfaceContainerLowest = IndustrialLightContainerLowest,
    surfaceContainerLow = IndustrialLightContainerLow,
    surfaceContainer = IndustrialLightContainer,
    surfaceContainerHigh = IndustrialLightContainerHigh,
    surfaceContainerHighest = IndustrialLightContainerHighest,
)

@Composable
fun DoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val radii = TravelRadii()
    val colorScheme = if (darkTheme) {
        IndustrialEtherealDarkColorScheme
    } else {
        IndustrialEtherealLightColorScheme
    }
    val gradients = TravelGradients(
        roaming = listOf(colorScheme.primaryContainer, colorScheme.secondary),
        declarationAction = listOf(colorScheme.primary, colorScheme.primaryContainer),
    )

    CompositionLocalProvider(
        LocalTravelSpacing provides TravelSpacing(),
        LocalTravelRadii provides radii,
        LocalTravelGlass provides TravelGlass(),
        LocalTravelGradients provides gradients,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
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
