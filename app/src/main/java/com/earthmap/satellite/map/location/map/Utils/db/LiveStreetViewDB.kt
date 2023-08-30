package com.earthmap.satellite.map.location.map.Utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.earthmap.satellite.map.location.map.Utils.db.models.FavouritesTable
import com.earthmap.satellite.map.location.map.Utils.db.models.HikingTable

@Database(entities = [HikingTable::class, FavouritesTable::class], version = 4, exportSchema = false)
abstract class LiveStreetViewDB: RoomDatabase() {

    abstract fun LiveStreetViewDao(): LiveStreetViewDao
    companion object{
        @Volatile
        private var INSTANCE:LiveStreetViewDB?=null
        private val DB_NAME = "myLiveStreet_db"
        fun getDatabaseInstance(mContext: Context):LiveStreetViewDB{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    mContext,
                    LiveStreetViewDB::class.java, DB_NAME
                ).build()
                INSTANCE=instance
                instance
            }

        }
    }
}