package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import com.rafalskrzypczyk.ninetyvalues.domain.models.Entry
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewEntryVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _state = MutableStateFlow(NewEntryState())
    val state = _state.asStateFlow()

    private var orderedValues = emptyList<Value>()

    fun onEvent(event: NewEntryUIEvents) {
        when(event) {
            NewEntryUIEvents.LoadInitialValues -> loadInitialValues()
            NewEntryUIEvents.SaveEntry -> saveEntry()
            is NewEntryUIEvents.OnValuesReordered -> reorderValues(event.reorderedValues)
        }
    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            repository.getAllValues().collectLatest { values ->
                orderedValues = values.sortedBy { it.name }
                _state.update {
                    it.copy(values = orderedValues)
                }
            }
        }
    }

    private fun reorderValues(reorderedValues: List<Value>) {
        orderedValues = reorderedValues
        _state.update { it.copy(values = orderedValues) }
    }

    private fun saveEntry() {
        viewModelScope.launch {
            val lastEntry = repository.getLatestEntry()

            val newEntryId = repository.saveEntry(
                Entry(
                    orderedValueIds = orderedValues.map { it.id },
                    previousEntryId = lastEntry?.id
                )
            )

            _state.update { it.copy(newEntryId = newEntryId) }
        }
    }
}