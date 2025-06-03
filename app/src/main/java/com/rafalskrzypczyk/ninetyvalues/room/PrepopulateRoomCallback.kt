package com.rafalskrzypczyk.ninetyvalues.room

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject
import com.rafalskrzypczyk.ninetyvalues.R
import javax.inject.Provider

class PrepopulateRoomCallback @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dbProvider: Provider<NinetyValuesDB>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            prepopulateWithNinetyValues(context = context)
        }
    }

    suspend fun prepopulateWithNinetyValues(context: Context) {
        try {
            val valueDao = dbProvider.get().valueDao

            val userList: JSONArray =
                context.resources.openRawResource(R.raw.ninety_values).bufferedReader().use {
                    JSONArray(it.readText())
                }

            userList.takeIf { it.length() > 0 }?.let { list ->
                val entitiesList = mutableListOf<com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity>()

                for (index in 0 until list.length()) {
                    val userObj = list.getJSONObject(index)
                    entitiesList.add(
                        com.rafalskrzypczyk.ninetyvalues.room.data.models.ValueEntity(
                            name = userObj.getString("name")
                        )
                    )
                }
                valueDao.populateWithValues(
                    entitiesList
                )
                Log.e("NinetyValues", "successfully pre-populated values into database")
            }
        } catch (exception: Exception) {
            Log.e("NinetyValues", exception.localizedMessage ?: "failed to pre-populate values into database")
        }
    }
}