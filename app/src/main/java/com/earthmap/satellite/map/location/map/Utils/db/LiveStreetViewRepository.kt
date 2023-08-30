package com.earthmap.satellite.map.location.map.Utils.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.earthmap.satellite.map.location.map.Utils.db.models.FavouritesTable
import com.earthmap.satellite.map.location.map.Utils.db.models.HikingTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LiveStreetViewRepository(application: Application) {
    private var dao:LiveStreetViewDao
    private var liveData:LiveData<List<HikingTable>>
    private var liveDataFavourite:LiveData<List<FavouritesTable>>

    init {
        val databaseInstance=LiveStreetViewDB.getDatabaseInstance(application)
        dao=databaseInstance.LiveStreetViewDao()
        liveData=dao.fetchAllMyHiking()
        liveDataFavourite=dao.fetchAllMyFavourites()
    }

    //methods for database operations :-

    fun insert(table:HikingTable?) {
        InsertActivity(dao).execute(table)
    }

    fun deleteActivity(id:Int) {
        DeleteActivity(dao).execute(id)
    }

    fun deleteAllNotes() {
        DeleteAllActivity(dao).execute()
    }

    fun getAllActivities(): LiveData<List<HikingTable>> {
        getAllActivities(dao).execute()
        return liveData
    }

    fun getAllFavourites(): LiveData<List<FavouritesTable>> {
        getAllFavourites(dao).execute()
        return liveDataFavourite
    }

    fun insertFavourites(table:FavouritesTable?) {
        InsertFavourites(dao).execute(table)
    }

    fun deleteFavourites(name:String) {
        DeleteFavourites(dao).execute(name)
    }


companion object{
    class InsertActivity(dao: LiveStreetViewDao):CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(table: HikingTable?) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(table) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(table: HikingTable?) {
            dao.insertHiking(table!!)
        }
    }
    class DeleteActivity(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(id: Int) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(id) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(id: Int) {
            dao.DeleteSpecificHikingData(id)
        }

    }

    class DeleteAllActivity(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job

        fun execute() = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground() {
            dao.DeleteAllData()
        }

    }

    class getAllActivities(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job

        fun execute() = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground() {
            dao.fetchAllMyHiking()
        }

    }


    class InsertFavourites(dao: LiveStreetViewDao):CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(table: FavouritesTable?) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(table) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(table: FavouritesTable?) {
            dao.insertFavourites (table!!)
        }
    }


    class DeleteFavourites(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        fun execute(name: String) = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground(name) // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground(name: String) {
            dao.DeleteSpecificFavouriteData(name)
        }

    }

    class getAllFavourites(dao: LiveStreetViewDao) :CoroutineScope {
        private var job: Job = Job()
        private var dao:LiveStreetViewDao
        init {
            this.dao=dao
        }
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + job

        fun execute() = launch { /*launch is having main thread scope*/
            Log.d("InsertActivityTAG", "execute:")
            doInBackground() // runs in background thread without blocking the Main Thread
        }
        private fun doInBackground() {
            dao.fetchAllMyFavourites()
        }

    }

}




}





