package com.dora.travel.ui.trip

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Train
import androidx.compose.ui.graphics.vector.ImageVector
import com.dora.travel.R

data class TripTimelineUiState(
    @param:StringRes val routeTitleRes: Int = R.string.travel_route_title,
    val nodes: List<TripTimelineNode> = emptyList(),
)

sealed interface TripTimelineNode {
    val marker: TimelineMarker

    data class Destination(
        override val marker: TimelineMarker,
        @param:StringRes val eyebrowRes: Int,
        @param:StringRes val statusRes: Int,
        @param:StringRes val titleRes: Int,
        @param:StringRes val subtitleRes: Int? = null,
        val icon: ImageVector,
        val metrics: List<TripMetric>,
        val preview: DestinationPreview? = null,
        val alert: TripAlert? = null,
    ) : TripTimelineNode

    data class Connection(
        override val marker: TimelineMarker,
        @param:StringRes val titleRes: Int,
        @param:StringRes val subtitleRes: Int,
        @param:StringRes val priceRes: Int,
        @param:StringRes val durationRes: Int,
        val icon: ImageVector,
    ) : TripTimelineNode
}

data class TimelineMarker(
    val tone: TimelineTone,
    val emphasized: Boolean = false,
)

enum class TimelineTone {
    Primary,
    Secondary,
    Tertiary,
}

data class TripMetric(
    @param:StringRes val labelRes: Int,
    @param:StringRes val valueRes: Int,
    @param:StringRes val unitRes: Int? = null,
)

data class DestinationPreview(
    @param:StringRes val contentDescriptionRes: Int,
)

data class TripAlert(
    @param:StringRes val titleRes: Int,
    @param:StringRes val bodyRes: Int,
    @param:StringRes val actionRes: Int,
)

object TripTimelinePreviewData {
    fun state() = TripTimelineUiState(
        nodes = listOf(
            TripTimelineNode.Destination(
                marker = TimelineMarker(TimelineTone.Primary, emphasized = true),
                eyebrowRes = R.string.travel_flight_meta,
                statusRes = R.string.travel_status_arrived,
                titleRes = R.string.travel_flight_title,
                subtitleRes = R.string.travel_flight_subtitle,
                icon = Icons.Default.Flight,
                metrics = listOf(
                    TripMetric(
                        labelRes = R.string.travel_baggage_label,
                        valueRes = R.string.travel_baggage_value,
                        unitRes = R.string.travel_baggage_unit,
                    ),
                    TripMetric(
                        labelRes = R.string.travel_arrival_label,
                        valueRes = R.string.travel_arrival_value,
                    ),
                ),
                preview = DestinationPreview(R.string.travel_flight_title),
            ),
            TripTimelineNode.Connection(
                marker = TimelineMarker(TimelineTone.Secondary),
                titleRes = R.string.travel_transport_title,
                subtitleRes = R.string.travel_transport_subtitle,
                priceRes = R.string.travel_transport_price,
                durationRes = R.string.travel_transport_duration,
                icon = Icons.Default.Train,
            ),
            TripTimelineNode.Destination(
                marker = TimelineMarker(TimelineTone.Tertiary, emphasized = true),
                eyebrowRes = R.string.travel_checkpoint_label,
                statusRes = R.string.travel_status_pending,
                titleRes = R.string.travel_checkpoint_title,
                icon = Icons.AutoMirrored.Filled.Article,
                metrics = listOf(
                    TripMetric(
                        labelRes = R.string.travel_wait_label,
                        valueRes = R.string.travel_wait_value,
                    ),
                    TripMetric(
                        labelRes = R.string.travel_weather_label,
                        valueRes = R.string.travel_weather_value,
                    ),
                ),
                alert = TripAlert(
                    titleRes = R.string.travel_declaration_title,
                    bodyRes = R.string.travel_declaration_body,
                    actionRes = R.string.travel_declaration_action,
                ),
            ),
        ),
    )
}
