package com.rafalskrzypczyk.ninetyvalues.domain.models

import com.rafalskrzypczyk.ninetyvalues.room.data.models.EntryEntity


data class Entry(
    val id: Long = 0,
    val timestamp: Long? = null,
    val orderedValueIds: List<Long>,
    val previousEntryId: Long? = null
)

fun Entry.toEntity(): EntryEntity = EntryEntity(
    orderedValueIds = orderedValueIds,
    previousEntryId = previousEntryId
)

fun EntryEntity.toDomain(): Entry = Entry(
    id = id,
    timestamp = timestamp,
    orderedValueIds = orderedValueIds,
    previousEntryId = previousEntryId
)