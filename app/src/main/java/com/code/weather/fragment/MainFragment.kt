package com.code.weather.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.code.weather.R
import com.code.weather.databinding.FragmentMainBinding
import com.code.weather.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout

class MainFragment(): Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMainBinding.inflate(layoutInflater)

        return viewBinding.mainFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val pullToRefresh: SwipeRefreshLayout = viewBinding.pullToRefresh
        pullToRefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                mainViewModel.clearWeatherResults()
                loadWeatherData(viewBinding.tabLayout.selectedTabPosition)
                viewBinding.progress.visibility = View.GONE
                pullToRefresh.setRefreshing(false)
            }
        })

        viewBinding.addCity.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_layout, CityMgrFragment()).commit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tabLayout = viewBinding.tabLayout

        tabLayout.addTab(
            tabLayout.newTab()
                .setText(context?.getString(R.string.current_tab_name))
        )
        tabLayout.addTab(
            tabLayout.newTab()
                .setText(context?.getString(R.string.hourly_tab_name))
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                loadWeatherData(tabLayout.selectedTabPosition)
            }
        })

        loadWeatherData(tabLayout.selectedTabPosition)
    }

    private fun loadWeatherData(selectedTabPosition: Int) {
        mainViewModel.loadWeatherData(requireContext(), viewBinding, selectedTabPosition)
    }
}