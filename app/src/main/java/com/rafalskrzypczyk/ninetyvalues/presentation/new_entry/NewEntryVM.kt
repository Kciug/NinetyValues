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

    private var selectableValues = listOf<ValueSelectableUIModel>()
    private var orderedValues = mutableListOf<Value>()

    private var currentValuesSelectingSteps: ValuesSelectingSteps? = null
    private var adjustedStepLimit: Int = 0

    private var initialValuesAmount: Int = 0

    fun onEvent(event: NewEntryUIEvents) {
        when(event) {
            NewEntryUIEvents.LoadInitialValues -> loadInitialValues()
            NewEntryUIEvents.OnSubmit -> onSubmit()
            is NewEntryUIEvents.OnValuesReordered -> reorderValues(event.reorderedValues)
            is NewEntryUIEvents.OnValueSelected -> onValueSelected(event.valueId)
            is NewEntryUIEvents.OnValueDeselected -> onValueDeselected(event.valueId)
            NewEntryUIEvents.SaveEntry -> saveEntry()
        }
    }

    private fun loadInitialValues() {
        currentValuesSelectingSteps = ValuesSelectingSteps.getFirstStep()
        currentValuesSelectingSteps?.let { adjustedStepLimit += it.count }
        viewModelScope.launch {
            repository.getAllValues().collectLatest { initialValues ->
                selectableValues = initialValues.sortedBy { it.name }.map { it.toSelectableUIModel() }.toMutableList()
                initialValuesAmount = initialValues.count()
                _state.update {
                    it.copy(
                        valuesToSelect = selectableValues,
                        selectingStep = currentValuesSelectingSteps,
                        selectingLimit = currentValuesSelectingSteps?.count ?: 0
                    )
                }
            }
        }
    }

    private fun onSubmit() {
        currentValuesSelectingSteps?.let { currentValuesSelectingSteps = ValuesSelectingSteps.getNextStep(it) }
        if(currentValuesSelectingSteps == null) {
            proceedToFinalization()
        } else {
            proceedToNextSelectingStep()
        }
    }

    private fun proceedToNextSelectingStep() {
        selectableValues = selectableValues.filter { !it.isSelected }
        currentValuesSelectingSteps?.let { adjustedStepLimit += it.count }
        _state.update { it.copy(
            orderingMode = OrderingMode.SELECTING,
            selectingStep = currentValuesSelectingSteps,
            valuesToSelect = selectableValues,
            selectedItems = 0,
            submitAllowed = false,
            selectingLimit = currentValuesSelectingSteps?.count ?: 0
        ) }
    }

    private fun proceedToFinalization() {
        _state.update { it.copy(
            orderingMode = OrderingMode.DRAGGING,
            selectingStep = null,
            valuesToReorder = orderedValues,
            valuesToSelect = emptyList(),
            confirmationRequired = true,
            showProgressBar = false,
            submitAllowed = true
        ) }
    }

    private fun reorderValues(reorderedValues: List<Value>) {
        orderedValues = reorderedValues.toMutableList()
        _state.update { it.copy(valuesToReorder = orderedValues) }
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

    private fun onValueSelected(valueId: Long) {
        selectableValues = selectableValues.map { item ->
            if (item.id == valueId) item.copy(
                isSelected = true,
                position = initialValuesAmount - orderedValues.count()
            ) else item
        }

        orderedValues.add(0, selectableValues.first {it.id == valueId}.toDomain() )
        updateSelectedState(true)
    }

    private fun onValueDeselected(valueId: Long) {
        selectableValues = selectableValues.map { item ->
            if (item.id == valueId) item.copy(
                isSelected = false,
                position = -1
            ) else item
        }

        orderedValues.removeIf { it.id == valueId }
        updateSelectedState(false)
    }

    private fun updateSelectedState(valueAdded: Boolean) {
        val limitReached = orderedValues.count() >= adjustedStepLimit
        _state.update { it.copy(
            valuesToSelect = selectableValues.map { it.copy() },
            orderingMode = if(limitReached) OrderingMode.NONE else OrderingMode.SELECTING,
            submitAllowed = limitReached,
            selectedItems = if(valueAdded) it.selectedItems.plus(1) else it.selectedItems.minus(1)
        ) }
    }
}