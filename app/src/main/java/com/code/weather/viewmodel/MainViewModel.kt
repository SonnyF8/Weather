package com.code.weather.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.code.weather.NetworkData
import com.code.weather.adapter.CurrentAdapter
import com.code.weather.adapter.HourlyAdapter
import com.code.weather.databinding.FragmentMainBinding
import com.code.weather.repository.City
import com.code.weather.repository.CityDatabase
import com.code.weather.repository.model.Current
import com.code.weather.repository.model.Hourly
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel: ViewModel(), CoroutineScope {
    companion object {
        private const val CURRENT_WEATHER: Int = 0
        private const val HOURLY_WEATHER: Int = 1
    }

    private val currentResult: ArrayList<Current> = arrayListOf()
    private val hourlyResult: ArrayList<Hourly> = arrayListOf()

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

    fun loadWeatherData(context: Context, binding: FragmentMainBinding, callType: Int) {
        launch {
            withContext(Dispatchers.IO) {
                getAllCities(context)
            }.let { cities ->
                when (callType) {
                    CURRENT_WEATHER -> {
                        if (currentResult.isEmpty()) {
                            loadCurrentData(context, binding, cities)
                        } else {
                            binding.recycler.adapter =
                                CurrentAdapter(currentResult.sortedBy { it.name })
                        }
                    }

                    HOURLY_WEATHER -> {
                        clearWeatherResults()
                        loadHourlyData(context, binding, cities)
                    }
                }
            }
        }
    }

    private suspend fun loadCurrentData(
        context: Context, binding: FragmentMainBinding, cityList: List<City>) {

        coroutineScope {
            cityList.map { city ->
                try {
                    launch {
                        showProgress(binding)
                        currentResult.add(currentDataCall(city)!!)

                        binding.recycler.adapter =
                            CurrentAdapter(currentResult.sortedBy { it.name })

                        hideProgress(binding)
                        Log.d("Coroutine Result -->", "$currentResult")
                    }
                } catch (exception: Exception) {
                    Log.d("Coroutine Exception -->", "${exception.message}")
                }
            }
        }
    }

    private suspend fun loadHourlyData(
        context: Context, binding: FragmentMainBinding, cityList: List<City>) {

        coroutineScope {
            cityList.map { city ->
                try {
                    launch {
                        showProgress(binding)
                        hourlyResult.add(hourlyDataCall(city)!!)

                        binding.recycler.adapter =
                            HourlyAdapter(hourlyResult.sortedBy { it.city?.name })

                        hideProgress(binding)
                        Log.d("Coroutine Result -->", "$hourlyResult")
                    }
                } catch (exception: Exception) {
                    Log.d("Coroutine Exception -->", "${exception.message}")
                }
            }
        }
    }

    private suspend fun getAllCities(context: Context): List<City> =
        CityDatabase.getInstance(context).cityDao().getAllCities()

    private suspend fun currentDataCall(city: City): Current? =
        withContext(Dispatchers.IO) {
            NetworkData().getCurrentWeatherData(city.latitude, city.longitude)
        }

    suspend private fun hourlyDataCall(city: City): Hourly? =
        withContext(Dispatchers.IO) {
            NetworkData().getHourlyWeatherData(city.latitude, city.longitude)
        }

    fun clearWeatherResults() {
        hourlyResult.clear()
        currentResult.clear()
    }

    fun showProgress(binding: FragmentMainBinding) {
        binding.progress.visibility = View.VISIBLE
    }

    fun hideProgress(binding: FragmentMainBinding) {
        binding.progress.visibility = View.GONE
    }
}