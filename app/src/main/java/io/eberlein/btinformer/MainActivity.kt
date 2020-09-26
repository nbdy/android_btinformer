package io.eberlein.btinformer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.eberlein.btinformer.services.LocationService
import io.eberlein.btinformer.services.OUIService
import io.eberlein.btinformer.services.ScannerService
import io.eberlein.btinformer.ui.*
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

// todo: fix permission requests
class MainActivity : AppCompatActivity() {
    private val permissionsBt = arrayOf(
        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED
    )
    private val permissionsBtCode = 420

    private val permissionsGPS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val permissionsGPSCode = 666

    private fun granted(grantResults: IntArray): Boolean {
        return grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    private var canUseGPS = false
    private var canUseBT = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var v = ""
        when(requestCode) {
            permissionsBtCode -> {
                if(!granted(grantResults)) v = "bluetooth"
                else canUseBT = true
            }

            permissionsGPSCode -> {
                if(!granted(grantResults)) v = "gps"
                else canUseGPS = true
            }
        }

        if(v.isNotEmpty()) Toast.makeText(this, "Cannot use $v without permission.", Toast.LENGTH_LONG).show()

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private lateinit var scanServiceIntent: Intent
    private lateinit var ouiServiceIntent: Intent
    private lateinit var locationServiceIntent: Intent

    private lateinit var serviceIntents: ArrayList<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
        checkPermissions()
        Paper.init(this)
        NotificationHelper.setup(this)
        startServices()
    }

    private fun setupUi(){
        setSupportActionBar(toolbar)
        nav_view.setNavigationItemSelectedListener(navigationViewListener)
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).addToBackStack(null).commit()
        supportFragmentManager.addOnBackStackChangedListener(backStackChangeListener)
    }

    private val navigationViewListener = NavigationView.OnNavigationItemSelectedListener {
        val f: Fragment
        val mid = it.itemId

        f = when {
            (mid == R.id.nav_home) -> HomeFragment()
            (mid == R.id.nav_scan) -> ScanFragment()
            (mid == R.id.nav_filters) -> FiltersFragment()
            (mid == R.id.nav_gps_log) -> GPSLogFragment()
            (mid == R.id.nav_settings) -> SettingsFragment()
            else -> HomeFragment()
        }

        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, f).addToBackStack(null).commit()
        drawer_layout.closeDrawers()

        true
    }

    private val backStackChangeListener = FragmentManager.OnBackStackChangedListener {
        try {
            val fm = supportFragmentManager
            fm.findFragmentById(fm.getBackStackEntryAt(fm.backStackEntryCount - 1).id)?.onResume()
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.printStackTrace()
            finish()
        }
    }

    private fun startServices(){
        serviceIntents = ArrayList()
        if(canUseGPS){
            locationServiceIntent = Intent(this, LocationService::class.java)
            serviceIntents.add(locationServiceIntent)
        }

        if(canUseBT){
            scanServiceIntent = Intent(this, ScannerService::class.java)
            serviceIntents.add(scanServiceIntent)
        }

        ouiServiceIntent = Intent(this, OUIService::class.java)
        serviceIntents.add(ouiServiceIntent)

        for (i in serviceIntents) startService(i)
    }

    private fun stopServices() {
        for(i in serviceIntents) stopService(i)
    }

    private fun checkPermission(p: String){
        // if(ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) requestPermissions()
    }

    private fun checkPermissions(){
        // permissionsBt.forEach {  }

        requestPermissions(permissionsBt, permissionsBtCode)
        requestPermissions(permissionsGPS, permissionsGPSCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServices()
    }
}