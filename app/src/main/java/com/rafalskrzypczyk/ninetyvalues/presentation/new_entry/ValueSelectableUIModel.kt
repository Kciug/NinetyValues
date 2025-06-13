package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

data class ValueSelectableUIModel (
    val id: Long,
    val name: String,
    val isSelected: Boolean,
    val position: Int
)

fun Value.toSelectableUIModel() = ValueSelectableUIModel(
    id = id,
    name = name,
    isSelected = false,
    position = -1
)

fun ValueSelectableUIModel.toDomain() = Value(
    id = id,
    name = name
)