package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

sealed interface NewEntryUIEvents {
    object LoadInitialValues : NewEntryUIEvents
    data class OnValuesReordered(val orderedList: List<Value>) : NewEntryUIEvents
    data class OnValueSelected(val valueId: Long) : NewEntryUIEvents
    data class OnValueDeselected(val valueId: Long) : NewEntryUIEvents
    object OnSubmit : NewEntryUIEvents
    object SaveEntry : NewEntryUIEvents
}