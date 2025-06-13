package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntryVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: Repository,
    @ApplicationContext private val context: Context
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
                    entryDate = entry.timestamp ?: "",
                    headerMessageDateJoiner = getDateJoiner(entry.timestamp ?: ""),
                    values = combinedValues,
                    isPositionDifferenceAvailable = previousEntry != null
                ) }
            }
        }
    }

    private fun getDateJoiner(entryDate: String) : String {
        return when (entryDate) {
            context.getString(R.string.txt_today) -> context.getString(R.string.entry_header_date_join_today)
            context.getString(R.string.txt_yesterday)  -> context.getString(R.string.entry_header_date_join_yesterday)
            else -> context.getString(R.string.entry_header_date_join_any)
        }
    }
}