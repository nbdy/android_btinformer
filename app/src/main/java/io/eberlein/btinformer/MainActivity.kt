package io.eberlein.btinformer

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.Menu
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
import io.paperdb.Paper
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import splitties.experimental.ExperimentalSplittiesApi
import splitties.permissions.ensurePermission


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_BT = arrayOf(
        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED
    )
    private val PERMISSIONS_GPS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var serviceIntent: Intent

    private lateinit var appBarConfiguration: AppBarConfiguration

    @ExperimentalSplittiesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_filters, R.id.nav_settings), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        lifecycle.coroutineScope.launch {checkPermissions()}
        Paper.init(this)
        EventBus.getDefault().register(this)
        serviceIntent = Intent(this, ScannerService::class.java)
        startService(serviceIntent)
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
        stopService(serviceIntent)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventReadyChanged(e: ScannerService.EventReadyChanged){
        // todo
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}