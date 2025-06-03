package com.rafalskrzypczyk.ninetyvalues.domain.models

import com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity


data class Value (
    val id: Long,
    val name: String
)

fun ValueEntity.toDomain(): Value = Value(
    id = id,
    name = name
)