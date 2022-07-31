package com.code.weather.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.code.weather.R
import com.code.weather.databinding.ActivityMainBinding
import com.code.weather.fragment.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.mainLayout)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(activityMainBinding.mainLayout.id, MainFragment()).commit()
    }

    override fun onResume() {
        super.onResume()
        netCheck()
    }

    private fun netCheck() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm.activeNetworkInfo != null)
            if (cm.activeNetworkInfo!!.isConnected) return

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.network_error))
            .setIconAttribute(android.R.attr.alertDialogIcon)
            .setMessage(getString(R.string.check_settings))
            .setPositiveButton(getString(R.string.exit)) { dialog, which -> finish() }
            .setCancelable(false).show()
    }
}
