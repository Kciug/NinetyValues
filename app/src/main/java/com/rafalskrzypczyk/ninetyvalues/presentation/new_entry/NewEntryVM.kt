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

    private var initialValuesAmount: Int = 0
    private val stepLimit: Int = 10
    private var stepsCount: Int = 0
    private var currentStep: Int = 1


    fun onEvent(event: NewEntryUIEvents) {
        when(event) {
            NewEntryUIEvents.LoadInitialValues -> loadInitialValues()
            NewEntryUIEvents.OnSubmit -> onSubmit()
            is NewEntryUIEvents.OnValuesReordered -> reorderValues(event.orderedList)
            is NewEntryUIEvents.OnValueSelected -> onValueSelected(event.valueId)
            is NewEntryUIEvents.OnValueDeselected -> onValueDeselected(event.valueId)
            NewEntryUIEvents.SaveEntry -> saveEntry()
        }
    }

    private fun loadInitialValues() {
        viewModelScope.launch {
            repository.getAllValues().collectLatest { initialValues ->
                selectableValues = initialValues.sortedBy { it.name }.map { it.toSelectableUIModel() }.toMutableList()
                initialValuesAmount = initialValues.count()
                stepsCount = initialValuesAmount / stepLimit
                _state.update {
                    it.copy(
                        valuesToSelect = selectableValues,
                        step = currentStep,
                        selectingLimit = stepLimit
                    )
                }
            }
        }
    }

    private fun onSubmit() {
        if(currentStep < stepsCount) {
            currentStep += 1
            proceedToNextSelectingStep()
        } else {
            proceedToFinalization()
        }
    }

    private fun proceedToNextSelectingStep() {
        val limit = if(currentStep == stepsCount) {
            initialValuesAmount - ((stepsCount - 1) * stepLimit)
        } else stepLimit

        selectableValues = selectableValues.filter { !it.isSelected }

        _state.update { it.copy(
            orderingMode = OrderingMode.SELECTING,
            step = currentStep,
            valuesToSelect = selectableValues,
            selectedItems = 0,
            submitAllowed = false,
            selectingLimit = limit
        ) }
    }

    private fun proceedToFinalization() {
        _state.update { it.copy(
            orderingMode = OrderingMode.DRAGGING,
            valuesToReorder = orderedValues,
            valuesToSelect = emptyList(),
            isFinalizationStep = true,
            showProgressBar = false,
            submitAllowed = true
        ) }
    }

    private fun reorderValues(reorderedList: List<Value>) {
        orderedValues = reorderedList.toMutableList()
        _state.update { it.copy(valuesToReorder = orderedValues.toList()) }
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
        renumberSelectedPositions()
        updateSelectedState(false)
    }

    private fun updateSelectedState(valueAdded: Boolean) {
        val limitReached = orderedValues.count() >= (stepLimit * currentStep)
        _state.update { it.copy(
            valuesToSelect = selectableValues.map { it.copy() },
            orderingMode = if(limitReached) OrderingMode.NONE else OrderingMode.SELECTING,
            submitAllowed = limitReached,
            selectedItems = if(valueAdded) it.selectedItems.plus(1) else it.selectedItems.minus(1)
        ) }
    }

    private fun renumberSelectedPositions() {
        selectableValues = selectableValues.map { item ->
            val index = orderedValues.asReversed().indexOfFirst { it.id == item.id }
            if (index != -1) item.copy(position = initialValuesAmount - index) else item
        }
    }
}