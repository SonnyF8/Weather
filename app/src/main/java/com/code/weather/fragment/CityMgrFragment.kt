package com.code.weather.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.code.weather.R
import com.code.weather.databinding.FragmentCityMgrBinding
import com.code.weather.viewmodel.CityMgrViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class CityMgrFragment(): Fragment() {
    private val apiKey = "Google Places API key here"

    private lateinit var cityMgrViewModel: CityMgrViewModel
    private lateinit var viewBinding: FragmentCityMgrBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentCityMgrBinding.inflate(layoutInflater)

        return viewBinding.cityMgrFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cityMgrViewModel = ViewModelProvider(this).get(CityMgrViewModel::class.java)

        viewBinding.closeCityMgr.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, MainFragment()).commit()
        }

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    deleteCity(requireContext(), viewBinding, viewHolder.adapterPosition)
                }
            }
        ).attachToRecyclerView(viewBinding.cityRecycler)

        cityMgrViewModel.loadCityData(requireContext(), viewBinding)
        initPlaces(childFragmentManager.fragments.get(0) as AutocompleteSupportFragment)
    }

    private fun initPlaces(autoPlacesFragment: AutocompleteSupportFragment) {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        autoPlacesFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.LAT_LNG))

        autoPlacesFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val city = place.name!!
                val latitude = place.latLng?.latitude!!
                val longitude = place.latLng?.longitude!!

                addCity(requireContext(), viewBinding, city, latitude, longitude)
            }

            override fun onError(status: Status) {
                Log.d("Places:", "Error")
            }
        })
    }

    private fun addCity(
            context: Context, binding: FragmentCityMgrBinding,
            cityName: String, latitude: Double, longitude: Double) {
            cityMgrViewModel.addCity(context, binding, cityName, latitude, longitude
        )
    }

    private fun deleteCity(
        context: Context, binding: FragmentCityMgrBinding, cityListId: Int) {
        cityMgrViewModel.deleteCity(context, binding, cityListId)
    }
}