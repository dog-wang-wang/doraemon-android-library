package com.dora.travel.ui.trip

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TripTimelineViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TripTimelinePreviewData.state())
    val uiState: StateFlow<TripTimelineUiState> = _uiState.asStateFlow()
}
