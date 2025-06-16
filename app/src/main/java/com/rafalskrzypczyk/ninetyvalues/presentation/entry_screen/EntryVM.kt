package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {
    private val _state = MutableStateFlow(EntryState())
    val state = _state.asStateFlow()

    private var allValues: List<Value> = emptyList()

    val entryId: Long = checkNotNull(savedStateHandle["entryId"]) {
        "Missing entryId argument"
    }

    init {
        viewModelScope.launch {
            allValues = repository.getAllValues().first()

            val entry = repository.getEntryById(entryId)

            entry?.let { entry ->
                val orderedValues = entry.orderedValueIds.mapNotNull { id ->
                    allValues.find { it.id == id }
                }

                val previousEntry = if(entry.previousEntryId != null) repository.getEntryById(entry.previousEntryId) else null

                val combinedValues = orderedValues.map { entryValue ->
                    previousEntry?.let {
                        val currentPosition = orderedValues.indexOf(entryValue)
                        val previousPosition = it.orderedValueIds.indexOf(entryValue.id)

                        entryValue.toEntryPresentation(previousPosition - currentPosition)
                    } ?: entryValue.toEntryPresentation(null)
                }

                _state.update { it.copy(
                    entryDate = entry.timestamp ?: 0,
                    values = combinedValues,
                    isPositionDifferenceAvailable = previousEntry != null
                ) }
            }
        }
    }
}