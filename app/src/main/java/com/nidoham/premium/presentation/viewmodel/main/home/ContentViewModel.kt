package com.nidoham.premium.presentation.viewmodel.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nidoham.extractor.stream.StreamItem
import com.nidoham.extractor.stream.TrendsExtractor
import com.nidoham.extractor.util.KioskTranslator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI State for Content Screen
 */
sealed interface ContentUiState {
    data object Idle : ContentUiState
    data object Loading : ContentUiState
    data class Success(
        val items: List<StreamItem>,
        val kiosk: KioskTranslator
    ) : ContentUiState
    data class Error(
        val message: String,
        val kiosk: KioskTranslator? = null
    ) : ContentUiState
}

class ContentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<ContentUiState>(ContentUiState.Idle)
    val uiState: StateFlow<ContentUiState> = _uiState.asStateFlow()

    private val trendsExtractor = TrendsExtractor()
    private var fetchJob: Job? = null

    // Track which kiosk is currently loaded in this VM instance
    private var activeKiosk: KioskTranslator? = null

    /**
     * Fetch content for a specific kiosk category.
     *
     * @param kiosk The kiosk category to fetch
     * @param forceRefresh If true, bypasses cache check and forces network call
     */
    fun fetchKiosk(kiosk: KioskTranslator, forceRefresh: Boolean = false) {
        // 1. If we are already displaying this kiosk's data and not forcing a refresh, do nothing.
        if (!forceRefresh && activeKiosk == kiosk && _uiState.value is ContentUiState.Success) {
            return
        }

        fetchJob?.cancel()
        activeKiosk = kiosk

        // 2. Only show full loading state if we don't have data or if it's a completely new category
        if (_uiState.value !is ContentUiState.Success || forceRefresh) {
            _uiState.update { ContentUiState.Loading }
        }

        fetchJob = viewModelScope.launch {
            try {
                val category = resolveCategory(kiosk)
                val result = trendsExtractor.fetchInitialPage(category)

                _uiState.update {
                    ContentUiState.Success(
                        items = result.items,
                        kiosk = kiosk
                    )
                }

            } catch (e: Exception) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    e.printStackTrace()
                    _uiState.update {
                        ContentUiState.Error(
                            message = e.localizedMessage ?: "Failed to load content",
                            kiosk = kiosk
                        )
                    }
                }
            }
        }
    }

    private fun resolveCategory(kiosk: KioskTranslator): TrendsExtractor.Category =
        TrendsExtractor.Category.entries.find {
            it.kioskId.equals(kiosk.kioskId, ignoreCase = true)
        } ?: TrendsExtractor.Category.TRENDING // Fallback instead of crash

    fun retry() {
        val currentKiosk = activeKiosk
        if (currentKiosk != null) {
            fetchKiosk(currentKiosk, forceRefresh = true)
        }
    }

    fun reset() {
        fetchJob?.cancel()
        activeKiosk = null
        _uiState.update { ContentUiState.Idle }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}