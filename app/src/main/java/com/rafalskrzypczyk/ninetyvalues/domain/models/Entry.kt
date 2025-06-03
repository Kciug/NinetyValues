package com.rafalskrzypczyk.ninetyvalues.domain.models

import android.content.Context
import com.rafalskrzypczyk.ninetyvalues.room.data.models.EntryEntity
import com.rafalskrzypczyk.ninetyvalues.utils.toFormattedDate


data class Entry(
    val id: Long = 0,
    val timestamp: String? = null,
    val orderedValueIds: List<Long>,
    val previousEntryId: Long? = null
)

fun Entry.toEntity(): EntryEntity = EntryEntity(
    orderedValueIds = orderedValueIds,
    previousEntryId = previousEntryId
)

fun EntryEntity.toDomain(context: Context): Entry = Entry(
    id = id,
    timestamp = timestamp.toFormattedDate(context),
    orderedValueIds = orderedValueIds,
    previousEntryId = previousEntryId
)