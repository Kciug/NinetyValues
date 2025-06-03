package com.rafalskrzypczyk.ninetyvalues.presentation.entries_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntriesHistoryVM @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _state = MutableStateFlow(EntriesHistoryState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllEntries().collectLatest { entries ->
                _state.update { it.copy(
                    entries = entries
                ) }
            }
        }
    }
}