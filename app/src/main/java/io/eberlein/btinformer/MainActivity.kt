package io.eberlein.btinformer

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.coroutineScope
import io.eberlein.btinformer.services.LocationService
import io.eberlein.btinformer.services.OUIService
import io.eberlein.btinformer.services.ScannerService
import io.paperdb.Paper
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import splitties.experimental.ExperimentalSplittiesApi
import splitties.permissions.ensurePermission

// todo fix <- button showing upon selecting fragment
// do some stuff in backstackchangelistener or smthn
class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_BT = arrayOf(
        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED
    )
    private val PERMISSIONS_GPS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var scanServiceIntent: Intent
    private lateinit var ouiServiceIntent: Intent
    private lateinit var locationServiceIntent: Intent

    private lateinit var serviceIntents: Array<Intent>

    private lateinit var appBarConfiguration: AppBarConfiguration

    @ExperimentalSplittiesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        setupUi()
        lifecycle.coroutineScope.launch {checkPermissions()}
        Paper.init(this)
        NotificationHelper.setup(this)
        startServices()
    }

    private fun setupUi(){
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_filters, R.id.nav_settings), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun startServices(){
        locationServiceIntent = Intent(this, LocationService::class.java)
        scanServiceIntent = Intent(this, ScannerService::class.java)
        ouiServiceIntent = Intent(this, OUIService::class.java)

        serviceIntents = arrayOf(locationServiceIntent, scanServiceIntent, ouiServiceIntent)

        for (i in serviceIntents) startService(i)
    }

    private fun stopServices() {
        for(i in serviceIntents) stopService(i)
    }

    @ExperimentalSplittiesApi
    private suspend fun ensurePermissionOrExit(p: String) = ensurePermission(
        permission = p,
        askOpenSettingsOrReturn = { false },
        showRationaleAndContinueOrReturn = { true }
    ) {
        return
    }

    @ExperimentalSplittiesApi
    private suspend fun checkPermissions(){
        PERMISSIONS_BT.forEach { ensurePermissionOrExit(it) }
        PERMISSIONS_GPS.forEach { ensurePermissionOrExit(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopServices()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventReadyChanged(e: ScannerService.EventReadyChanged){
        // todo
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}