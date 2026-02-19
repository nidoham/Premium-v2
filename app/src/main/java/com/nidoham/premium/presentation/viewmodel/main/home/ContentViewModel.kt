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
import org.schabi.newpipe.extractor.Page // Import NewPipe Page

/**
 * UI State for Content Screen
 */
sealed interface ContentUiState {
    data object Idle : ContentUiState
    data object Loading : ContentUiState
    data class Success(
        val items: List<StreamItem>,
        val kiosk: KioskTranslator,
        val isPaginating: Boolean = false,
        val endReached: Boolean = false
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

    // Track current active category logic
    private var activeKioskTranslator: KioskTranslator? = null
    private var activeExtractorCategory: TrendsExtractor.Category? = null

    // Store the specific NewPipe Page object for pagination
    private var nextPageToken: Page? = null

    /**
     * Fetch content for a specific kiosk category.
     */
    fun fetchKiosk(kiosk: KioskTranslator, forceRefresh: Boolean = false) {
        // Prevent redundant refreshes if data exists and is valid
        if (!forceRefresh && activeKioskTranslator == kiosk && _uiState.value is ContentUiState.Success) {
            return
        }

        fetchJob?.cancel()
        activeKioskTranslator = kiosk

        // Reset pagination state
        nextPageToken = null

        // Show full loading only if we are starting fresh or forcing it
        if (_uiState.value !is ContentUiState.Success || forceRefresh) {
            _uiState.update { ContentUiState.Loading }
        }

        fetchJob = viewModelScope.launch {
            try {
                // Map UI Kiosk to Extractor Category
                val category = resolveCategory(kiosk)
                activeExtractorCategory = category

                // 1. Fetch Initial Page
                val result = trendsExtractor.fetchInitialPage(category)

                // 2. Store next page token
                nextPageToken = result.nextPage

                _uiState.update {
                    ContentUiState.Success(
                        items = result.items,
                        kiosk = kiosk,
                        isPaginating = false,
                        // End is reached if there is no next page object
                        endReached = !result.hasNextPage || result.nextPage == null
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

    /**
     * Load the next page of items and append them to the current list.
     */
    fun loadNextPage() {
        val currentState = _uiState.value as? ContentUiState.Success ?: return
        val category = activeExtractorCategory ?: return
        val pageToFetch = nextPageToken

        // Guard clauses: Don't load if already loading, finished, or no token
        if (currentState.isPaginating || currentState.endReached || pageToFetch == null) {
            return
        }

        // Set Pagination Loading State
        _uiState.update { currentState.copy(isPaginating = true) }

        viewModelScope.launch {
            try {
                // 1. Fetch Next Page using the stored token
                val result = trendsExtractor.fetchNextPage(category, pageToFetch)

                // 2. Update token for the *next* time
                nextPageToken = result.nextPage

                _uiState.update {
                    currentState.copy(
                        items = currentState.items + result.items, // Append items
                        isPaginating = false,
                        endReached = !result.hasNextPage || result.nextPage == null
                    )
                }
            } catch (e: Exception) {
                if (e !is kotlinx.coroutines.CancellationException) {
                    e.printStackTrace()
                    // On error, just stop the pagination spinner. User can try scrolling again.
                    _uiState.update { currentState.copy(isPaginating = false) }
                }
            }
        }
    }

    /**
     * Maps the UI [KioskTranslator] to the [TrendsExtractor.Category].
     * Matches based on the raw kioskId string.
     */
    private fun resolveCategory(kiosk: KioskTranslator): TrendsExtractor.Category {
        return TrendsExtractor.Category.entries.find {
            it.kioskId.equals(kiosk.kioskId, ignoreCase = true)
        } ?: TrendsExtractor.Category.TRENDING // Fallback to Trending if not found
    }

    fun retry() {
        val currentKiosk = activeKioskTranslator
        if (currentKiosk != null) {
            fetchKiosk(currentKiosk, forceRefresh = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}