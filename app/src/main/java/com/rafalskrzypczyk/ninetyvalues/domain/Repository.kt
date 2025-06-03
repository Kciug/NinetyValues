package com.rafalskrzypczyk.ninetyvalues.domain

import com.rafalskrzypczyk.ninetyvalues.domain.models.Entry
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllValues(): Flow<List<Value>>
    suspend fun getLatestEntry(): Entry?
    suspend fun getEntryById(entryId: Long) : Entry?
    fun getAllEntries(): Flow<List<Entry>>
    suspend fun saveEntry(entry: Entry) : Long
}