package com.rafalskrzypczyk.room.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafalskrzypczyk.room.NinetyValuesDB
import com.rafalskrzypczyk.room.PrepopulateRoomCallback
import com.rafalskrzypczyk.room.RoomConstants
import com.rafalskrzypczyk.room.data.dao.ValueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomCallback(
        @ApplicationContext context: Context,
        dbProvider: Provider<NinetyValuesDB>
    ): RoomDatabase.Callback {
        return PrepopulateRoomCallback(context, dbProvider)
    }

    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
        callback: RoomDatabase.Callback
    ): NinetyValuesDB {
        return Room.databaseBuilder(context, NinetyValuesDB::class.java, RoomConstants.DATABASE_NAME)
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideValueDao(database: NinetyValuesDB): ValueDao {
        return database.valueDao
    }
}