package com.earthmap.satellite.map.location.map.Utils.db.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.earthmap.satellite.map.location.map.Utils.db.LiveStreetViewRepository
import com.earthmap.satellite.map.location.map.Utils.db.models.FavouritesTable
import com.earthmap.satellite.map.location.map.Utils.db.models.HikingTable


class LiveStreetViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: LiveStreetViewRepository
    lateinit var liveData: LiveData<List<HikingTable>>
    lateinit var liveDataFav: LiveData<List<FavouritesTable>>

    init {
        repository=LiveStreetViewRepository(application)

    }

    fun insert(table:HikingTable) {
        repository.insert(table)
    }

    fun insertFavourites(table:FavouritesTable) {
        repository.insertFavourites(table)
    }

    fun delete(id:Int) {
        repository.deleteActivity(id)
    }

    fun deleteFavourites(name:String) {
        repository.deleteFavourites(name)
    }

    fun deleteAllActivities() {
        repository.deleteAllNotes()
    }

    fun getAllActivities(): LiveData<List<HikingTable>> {
        liveData=repository.getAllActivities()
        return liveData
    }
    fun getAllFavourites(): LiveData<List<FavouritesTable>> {
        liveDataFav=repository.getAllFavourites()
        return liveDataFav
    }

}