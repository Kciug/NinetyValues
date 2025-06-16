package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

data class EntryState(
    val entryDate: Long? = 0,
    val values: List<ValueUIModel> = emptyList(),
    val isPositionDifferenceAvailable: Boolean = false
)
