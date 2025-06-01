package com.rafalskrzypczyk.room

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
import com.rafalskrzypczyk.room.data.models.ValueEntity
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
            val userDao = dbProvider.get().valueDao

            val userList: JSONArray =
                context.resources.openRawResource(R.raw.ninety_values).bufferedReader().use {
                    JSONArray(it.readText())
                }

            userList.takeIf { it.length() > 0 }?.let { list ->
                val entitiesList = mutableListOf<ValueEntity>()

                for (index in 0 until list.length()) {
                    val userObj = list.getJSONObject(index)
                    entitiesList.add(ValueEntity(
                        name = userObj.getString("name")
                    ))
                }
                userDao.populateWithValues(entitiesList)
                Log.e("KURWA", "successfully pre-populated users into database")
            }
        } catch (exception: Exception) {
            Log.e("KURWA", exception.localizedMessage ?: "failed to pre-populate users into database")
        }
    }
}