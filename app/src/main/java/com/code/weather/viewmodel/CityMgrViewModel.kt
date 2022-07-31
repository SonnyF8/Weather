package com.code.weather.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.code.weather.adapter.CityMgrAdapter
import com.code.weather.databinding.FragmentCityMgrBinding
import com.code.weather.repository.City
import com.code.weather.repository.CityDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CityMgrViewModel: ViewModel(), CoroutineScope {
    private lateinit var citiesList: List<City>

    private val job = Job()
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("Coroutine exception -->", "${exception.message}")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + handler

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun loadCityData(context: Context, binding: FragmentCityMgrBinding) {
        launch {
            withContext(Dispatchers.IO) {
                getAllCities(context)
            }.let { cities ->
                citiesList = cities
                binding.cityRecycler.adapter =
                CityMgrAdapter(cities.sortedBy { cities.get(0).cityName })
            }
        }
    }

    private suspend fun getAllCities(context: Context): List<City> =
        CityDatabase.getInstance(context).cityDao().getAllCities()

    fun addCity(
        context: Context, binding: FragmentCityMgrBinding,
        cityName: String, latitude: Double, longitude: Double) {

        launch {
            withContext(Dispatchers.IO) {
                CityDatabase.getInstance(context).cityDao().insertCity(
                    City(cityName, latitude, longitude)
                )
                loadCityData(context, binding)
            }
        }
    }

    fun deleteCity(
        context: Context, binding: FragmentCityMgrBinding, cityListId: Int) {

        launch {
            withContext(Dispatchers.IO) {
                CityDatabase.getInstance(context).cityDao().deleteCity(
                    City(
                        citiesList.get(cityListId).cityName,
                        citiesList.get(cityListId).latitude,
                        citiesList.get(cityListId).longitude
                    )
                )
                loadCityData(context, binding)
            }
        }
    }
}