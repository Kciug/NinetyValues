package com.rafalskrzypczyk.ninetyvalues.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafalskrzypczyk.ninetyvalues.room.data.models.EntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: EntryEntity): Long

    @Query("SELECT * FROM entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries WHERE id = :id")
    suspend fun getEntryById(id: Long): EntryEntity?

    @Query("SELECT * FROM entries ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEntry(): EntryEntity?
}