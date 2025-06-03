package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

data class EntryState(
    val entryDate: String = "",
    val headerMessageDateJoiner: String = "",
    val values: List<ValueUIModel> = emptyList()
)
