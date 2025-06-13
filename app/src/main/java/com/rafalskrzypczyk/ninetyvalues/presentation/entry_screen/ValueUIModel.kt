package com.rafalskrzypczyk.ninetyvalues.presentation.entry_screen

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

data class ValueUIModel(
    val id: Long,
    val name: String,
    val positionChange: Int
)

fun Value.toEntryPresentation(positionChange: Int?) = ValueUIModel(
    id = id,
    name = name,
    positionChange = positionChange ?: 0
)
