package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.flow.collectLatest
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
            Log.d("NinetyValues", "entryId: $entryId")

            allValues = repository.getAllValues().first()

            val entry = repository.getEntryById(entryId)

            Log.d("NinetyValues", "entry: $entry")

            entry?.let { entry ->
                Log.d("NinetyValues", "entry: $entry")
                val orderedValues = entry.orderedValueIds.mapNotNull { id ->
                    allValues.find { it.id == id }
                }.map { ValueUIModel(
                    id = it.id,
                    name = it.name
                ) }

                _state.update { it.copy(
                    entryDate = entry.timestamp ?: "",
                    headerMessageDateJoiner = getDateJoiner(entry.timestamp ?: ""),
                    values = orderedValues
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