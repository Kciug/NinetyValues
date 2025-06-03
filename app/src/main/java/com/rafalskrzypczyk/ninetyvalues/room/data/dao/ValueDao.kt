package com.rafalskrzypczyk.ninetyvalues.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ValueDao {
    @Query("SELECT * FROM `values`")
    fun getAllValues(): Flow<List<ValueEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun populateWithValues(valueEntities: List<ValueEntity>)
}