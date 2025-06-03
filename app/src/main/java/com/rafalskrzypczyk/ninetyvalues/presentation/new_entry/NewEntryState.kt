package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import com.rafalskrzypczyk.ninetyvalues.domain.models.Value

data class NewEntryState(
    val values: List<Value> = emptyList(),
    val newEntryId: Long? = null
)
