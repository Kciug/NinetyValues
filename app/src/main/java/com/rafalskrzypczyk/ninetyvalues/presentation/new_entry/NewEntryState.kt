package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

data class NewEntryState(
    val valuesToSelect: List<ValueSelectableUIModel> = emptyList(),
    val valuesToReorder: List<Value> = emptyList(),
    val newEntryId: Long? = null,
    val orderingMode: OrderingMode = OrderingMode.SELECTING,
    val step: Int = 0,
    val selectedItems: Int = 0,
    val selectingLimit: Int = 0,
    val showProgressBar: Boolean = true,
    val submitAllowed: Boolean = false,
    val isFinalizationStep: Boolean = false,
)
