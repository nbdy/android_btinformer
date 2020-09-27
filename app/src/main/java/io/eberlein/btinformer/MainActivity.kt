package io.eberlein.btinformer

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import io.eberlein.btinformer.services.LocationService
import io.eberlein.btinformer.services.OUIService
import io.eberlein.btinformer.services.ScannerService
import io.eberlein.btinformer.ui.*
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {
    private var canUseGPS = false

    private lateinit var scanServiceIntent: Intent
    private lateinit var ouiServiceIntent: Intent
    private lateinit var locationServiceIntent: Intent

    private lateinit var serviceIntents: ArrayList<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUi()
        Paper.init(this)
        NotificationHelper.setup(this)
        checkPermissions()
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

    private fun checkPermissions(){
        checkLocationPermission()
    }

    private fun checkLocationPermission(){
        if(PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            canUseGPS = true
            startServices()
        }
        else {
            PermissionUtils.permission(PermissionConstants.LOCATION)
                .callback(object : PermissionUtils.FullCallback {
                    override fun onGranted(granted: MutableList<String>) {
                        if (granted.size == 1) {
                            canUseGPS = true
                            startServices()
                        }
                    }

                    override fun onDenied(
                        deniedForever: MutableList<String>,
                        denied: MutableList<String>
                    ) {
                        if (denied.size > 0) ToastUtils.showLong("GPS and Bluetooth functionality disabled.")
                    }
                }).request()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServices()
    }
}