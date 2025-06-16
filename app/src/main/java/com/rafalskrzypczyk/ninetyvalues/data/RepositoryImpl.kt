package com.rafalskrzypczyk.ninetyvalues.data

import com.rafalskrzypczyk.ninetyvalues.domain.Repository
import com.rafalskrzypczyk.ninetyvalues.domain.models.Entry
import com.rafalskrzypczyk.ninetyvalues.domain.models.Value
import com.rafalskrzypczyk.ninetyvalues.domain.models.toDomain
import com.rafalskrzypczyk.ninetyvalues.domain.models.toEntity
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.EntryDao
import com.rafalskrzypczyk.ninetyvalues.room.data.dao.ValueDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val valueDao: ValueDao,
    private val entryDao: EntryDao,
) : Repository {
    override fun getAllValues(): Flow<List<Value>> = valueDao.getAllValues().map {
        it.map { it.toDomain() }
    }

    override suspend fun getLatestEntry(): Entry? = entryDao.getLatestEntry()?.toDomain()

    override suspend fun getEntryById(entryId: Long): Entry? = entryDao.getEntryById(entryId)?.toDomain()

    override fun getAllEntries(): Flow<List<Entry>> = entryDao.getAllEntries().map {
        it.map { it.toDomain() }
    }

    override suspend fun saveEntry(entry: Entry) : Long = entryDao.insertEntry(entry.toEntity())
}