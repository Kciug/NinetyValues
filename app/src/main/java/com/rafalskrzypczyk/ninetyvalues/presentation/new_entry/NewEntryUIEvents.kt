package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

sealed interface NewEntryUIEvents {
    object LoadInitialValues : NewEntryUIEvents
    data class OnValuesReordered(val reorderedValues: List<Value>) : NewEntryUIEvents
    object SaveEntry : NewEntryUIEvents
}