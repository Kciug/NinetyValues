package com.rafalskrzypczyk.ninetyvalues.domain.models

import android.content.Context
import com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity


data class Value (
    val id: Long,
    val name: String
)

fun ValueEntity.toDomain(context: Context): Value = Value(
    id = id,
    name = when(context.resources.configuration.locales[0].language) {
        "pl" -> pl
        "en" -> en
        else -> en
    }
)