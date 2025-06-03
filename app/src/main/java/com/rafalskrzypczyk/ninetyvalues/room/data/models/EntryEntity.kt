package com.rafalskrzypczyk.ninetyvalues.room.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val orderedValueIds: List<Long>,
    val previousEntryId: Long? = null
)