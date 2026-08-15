package com.dora.travel.ui.trip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dora.travel.ui.theme.IndustrialContainer
import com.dora.travel.ui.theme.IndustrialContainerHigh
import com.dora.travel.ui.theme.IndustrialContainerLow
import com.dora.travel.ui.theme.IndustrialError
import com.dora.travel.ui.theme.IndustrialErrorContainer
import com.dora.travel.ui.theme.IndustrialOnPrimary
import com.dora.travel.ui.theme.IndustrialOnSurface
import com.dora.travel.ui.theme.IndustrialOnSurfaceVariant
import com.dora.travel.ui.theme.IndustrialPrimary
import com.dora.travel.ui.theme.IndustrialPrimaryContainer
import com.dora.travel.ui.theme.IndustrialSecondary
import com.dora.travel.ui.theme.IndustrialSurface
import com.dora.travel.ui.theme.IndustrialTertiary
import com.dora.travel.ui.theme.LocalTravelGlass
import com.dora.travel.ui.theme.LocalTravelGradients
import com.dora.travel.ui.theme.LocalTravelRadii
import com.dora.travel.ui.theme.LocalTravelSpacing

private object TripTimelineSize {
    val TopBarHeight = 64.dp
    val BottomBarHeight = 64.dp
    val BottomBarHorizontalPadding = 16.dp
    val TimelineRailX = 18.dp
    val TimelineMarkerLarge = 16.dp
    val TimelineMarkerSmall = 8.dp
    val TimelineContentOffset = 48.dp
    val IconButton = 40.dp
    val CardPreviewHeight = 128.dp
    val MetricHeight = 73.dp
    val ConnectionIcon = 40.dp
}

@Composable
fun TripTimelineRoute(
    modifier: Modifier = Modifier,
    viewModel: TripTimelineViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    TripTimelineScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun TripTimelineScreen(
    uiState: TripTimelineUiState,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTravelSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(IndustrialSurface),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = spacing.mobilePadding,
                top = TripTimelineSize.TopBarHeight + spacing.mobilePadding,
                end = spacing.mobilePadding,
                bottom = TripTimelineSize.BottomBarHeight + spacing.stackLarge * 2,
            ),
            verticalArrangement = Arrangement.spacedBy(spacing.stackLarge),
        ) {
            item {
                TimelineNodes(uiState.nodes)
            }
        }

        TripTopBar(
            title = stringResource(uiState.routeTitleRes),
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun TripTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(TripTimelineSize.TopBarHeight),
        color = IndustrialSurface.copy(alpha = 0.86f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = LocalTravelGlass.current.borderAlpha)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LocalTravelSpacing.current.mobilePadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
            ) {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    tint = IndustrialPrimary,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = title,
                    color = IndustrialOnSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(onClick = { }, modifier = Modifier.size(TripTimelineSize.IconButton)) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = IndustrialPrimary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun TimelineNodes(nodes: List<TripTimelineNode>) {
    Box {
        TimelineRail()
        Column(verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackLarge)) {
            nodes.forEach { node ->
                when (node) {
                    is TripTimelineNode.Destination -> DestinationTimelineNode(node)
                    is TripTimelineNode.Connection -> ConnectionTimelineNode(node)
                }
            }
        }
    }
}

@Composable
private fun BoxScope.TimelineRail() {
    val railColor = IndustrialPrimary.copy(alpha = 0.3f)

    Canvas(
        modifier = Modifier
            .matchParentSize()
            .padding(vertical = LocalTravelSpacing.current.stackSmall),
    ) {
        val x = TripTimelineSize.TimelineRailX.toPx()
        drawLine(
            color = railColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun DestinationTimelineNode(node: TripTimelineNode.Destination) {
    TimelineRow(marker = node.marker) {
        RoamingSurface(emphasized = node.marker.emphasized) {
            Column(
                modifier = Modifier.padding(LocalTravelSpacing.current.stackMedium),
                verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackMedium),
            ) {
                DestinationHeader(node)
                if (node.alert != null) {
                    DeclarationAlert(node.alert)
                } else {
                    MetricsGrid(node.metrics)
                    AirportPreview(node.preview)
                }
                if (node.alert != null) {
                    MetricsGrid(node.metrics)
                }
            }
        }
    }
}

@Composable
private fun DestinationHeader(node: TripTimelineNode.Destination) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.base),
            ) {
                Text(
                    text = stringResource(node.eyebrowRes),
                    color = markerColor(node.marker.tone),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                StatusChip(text = stringResource(node.statusRes), tone = node.marker.tone)
            }
            Text(
                text = stringResource(node.titleRes),
                color = IndustrialOnSurface,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            node.subtitleRes?.let {
                Text(
                    text = stringResource(it),
                    color = IndustrialOnSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Icon(
            imageVector = node.icon,
            contentDescription = null,
            tint = markerColor(node.marker.tone),
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun ConnectionTimelineNode(node: TripTimelineNode.Connection) {
    TimelineRow(marker = node.marker) {
        RoamingSurface(
            shape = RoundedCornerShape(LocalTravelRadii.current.full),
            containerColor = IndustrialContainerHigh.copy(alpha = LocalTravelGlass.current.cardAlpha),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LocalTravelSpacing.current.stackMedium, vertical = 17.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconBubble(icon = node.icon, tone = node.marker.tone)
                    Column {
                        Text(
                            text = stringResource(node.titleRes),
                            color = IndustrialOnSurface,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = stringResource(node.subtitleRes),
                            color = IndustrialOnSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(node.priceRes),
                        color = IndustrialSecondary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(node.durationRes),
                        color = IndustrialOnSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineRow(
    marker: TimelineMarker,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(start = markerStartPadding(marker))
                .size(if (marker.emphasized) TripTimelineSize.TimelineMarkerLarge else TripTimelineSize.TimelineMarkerSmall)
                .align(if (marker.emphasized) Alignment.TopStart else Alignment.CenterStart)
                .shadow(12.dp, CircleShape)
                .background(markerColor(marker.tone), CircleShape)
                .border(
                    width = if (marker.emphasized) 4.dp else 2.dp,
                    color = IndustrialSurface,
                    shape = CircleShape,
                ),
        )

        Box(
            modifier = Modifier
                .padding(start = TripTimelineSize.TimelineContentOffset)
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}

@Composable
private fun markerStartPadding(marker: TimelineMarker) =
    if (marker.emphasized) {
        TripTimelineSize.TimelineRailX - TripTimelineSize.TimelineMarkerLarge / 2
    } else {
        TripTimelineSize.TimelineRailX - TripTimelineSize.TimelineMarkerSmall / 2
    }

@Composable
private fun MetricsGrid(metrics: List<TripMetric>) {
    Row(horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
        metrics.forEach { metric ->
            MetricCard(metric, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MetricCard(metric: TripMetric, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(TripTimelineSize.MetricHeight),
        shape = RoundedCornerShape(LocalTravelRadii.current.small),
        color = IndustrialContainer.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
    ) {
        Column(
            modifier = Modifier.padding(13.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(metric.labelRes),
                color = IndustrialOnSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = stringResource(metric.valueRes),
                    color = IndustrialPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                metric.unitRes?.let {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(it),
                        color = IndustrialOnSurfaceVariant.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeclarationAlert(alert: TripAlert) {
    Surface(
        shape = RoundedCornerShape(LocalTravelRadii.current.small),
        color = IndustrialErrorContainer.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, IndustrialError.copy(alpha = 0.24f)),
    ) {
        Column(
            modifier = Modifier.padding(17.dp),
            verticalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.base),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(LocalTravelSpacing.current.stackSmall)) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = IndustrialError,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(alert.titleRes),
                    color = IndustrialError,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = stringResource(alert.bodyRes),
                color = IndustrialOnSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            GradientPillButton(
                text = stringResource(alert.actionRes),
                icon = Icons.AutoMirrored.Filled.OpenInNew,
                onClick = { },
            )
        }
    }
}

@Composable
private fun GradientPillButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                brush = Brush.horizontalGradient(LocalTravelGradients.current.declarationAction),
                shape = RoundedCornerShape(LocalTravelRadii.current.full),
            ),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = LocalTravelSpacing.current.stackMedium),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = IndustrialOnPrimary,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(LocalTravelSpacing.current.base))
        Text(
            text = text,
            color = IndustrialOnPrimary,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun AirportPreview(preview: DestinationPreview?) {
    val shape = RoundedCornerShape(LocalTravelRadii.current.small)
    val previewGradient = listOf(
        IndustrialPrimary.copy(alpha = 0.24f),
        IndustrialContainer.copy(alpha = 0.88f),
    )
    val horizonColor = IndustrialPrimary.copy(alpha = 0.28f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TripTimelineSize.CardPreviewHeight)
            .clip(shape)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)), shape)
            .background(IndustrialContainerLow),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.verticalGradient(previewGradient)
            )
            val horizonY = size.height * 0.42f
            drawLine(
                color = horizonColor,
                start = Offset(0f, horizonY),
                end = Offset(size.width, horizonY),
                strokeWidth = 1.dp.toPx(),
            )
            repeat(9) { index ->
                val progress = index / 8f
                val startX = size.width * progress
                drawLine(
                    color = Color.White.copy(alpha = 0.12f),
                    start = Offset(size.width / 2f, horizonY),
                    end = Offset(startX, size.height),
                    strokeWidth = 1.dp.toPx(),
                )
            }
        }
        Text(
            text = preview?.let { stringResource(it.contentDescriptionRes) }.orEmpty(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(LocalTravelSpacing.current.stackSmall),
            color = IndustrialOnSurface.copy(alpha = 0.42f),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun StatusChip(text: String, tone: TimelineTone) {
    Surface(
        shape = RoundedCornerShape(LocalTravelRadii.current.full),
        color = markerColor(tone).copy(alpha = 0.18f),
        border = BorderStroke(1.dp, markerColor(tone).copy(alpha = 0.28f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            color = markerColor(tone),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
        )
    }
}

@Composable
private fun IconBubble(icon: ImageVector, tone: TimelineTone) {
    Box(
        modifier = Modifier
            .size(TripTimelineSize.ConnectionIcon)
            .background(markerColor(tone).copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = markerColor(tone),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun RoamingSurface(
    modifier: Modifier = Modifier,
    emphasized: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(LocalTravelRadii.current.large),
    containerColor: Color = IndustrialContainer.copy(alpha = LocalTravelGlass.current.cardAlpha),
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (emphasized) {
                    Modifier.shadow(
                        elevation = 20.dp,
                        shape = shape,
                        ambientColor = IndustrialPrimary.copy(alpha = LocalTravelGlass.current.glowAlpha),
                        spotColor = IndustrialPrimary.copy(alpha = LocalTravelGlass.current.glowAlpha),
                    )
                } else {
                    Modifier
                }
            ),
        shape = shape,
        color = containerColor,
        border = BorderStroke(
            1.dp,
            Brush.linearGradient(
                listOf(
                    IndustrialPrimary.copy(alpha = LocalTravelGlass.current.borderAlpha),
                    Color.White.copy(alpha = 0.05f),
                ),
            ),
        ),
        content = content,
    )
}

@Composable
private fun markerColor(tone: TimelineTone) = when (tone) {
    TimelineTone.Primary -> IndustrialPrimary
    TimelineTone.Secondary -> IndustrialSecondary
    TimelineTone.Tertiary -> IndustrialTertiary
}

@Preview
@Composable
private fun TripTimelineScreenPreview() {
    com.dora.travel.ui.theme.DoraTheme {
        TripTimelineScreen(
            uiState = TripTimelinePreviewData.state(),
        )
    }
}
