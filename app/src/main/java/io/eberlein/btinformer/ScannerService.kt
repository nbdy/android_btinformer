package io.eberlein.btinformer

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.properties.Delegates

class ScannerService : Service() {
    private var TAG = "ScannerService"

    class EventReadyChanged(val ready: Boolean)
    class EventScanningChanged(val scanning: Boolean)
    class EventDevicesFound(val devices: ArrayList<ScanResult>)
    class EventChangeScan()

    private var DURATION = 10000L
    private var btWasEnabled = false

    private lateinit var btManager: BluetoothManager
    private lateinit var btAdapter: BluetoothAdapter
    private lateinit var leScanner: BluetoothLeScanner

    private var foundDevices = ArrayList<ScanResult>()

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null) {
                Log.d(TAG, "found device: ${result.device.address} with name: ${result.device.name}")
                foundDevices.add(result)
            }
        }
    }

    private val timeoutRunnable: Runnable = Runnable {
        stopScan()
    }

    private val handler = Handler()

    private var scanning = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        EventBus.getDefault().register(this)
        Log.d(TAG, "getting Bluetooth stuff ready")
        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
        if(!btAdapter.isEnabled) btAdapter.enable()
        else btWasEnabled = true
        leScanner = btAdapter.bluetoothLeScanner
        Log.d(TAG, "informing UI that we are ready")
        EventBus.getDefault().post(EventReadyChanged(true))
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        EventBus.getDefault().unregister(this)
        if(!btWasEnabled) btAdapter.disable()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventStartScan(e: ScannerService.EventChangeScan){
        Log.d(TAG, "onEventStartScan")
        if(!scanning) startScan()
        else {
            Log.d(TAG, "removing timeoutRunnable")
            handler.removeCallbacks(timeoutRunnable)
            stopScan()
        }
    }

    private fun stopScan(){
        Log.d(TAG, "stopScan")
        if(scanning){
            Log.d(TAG, "actually stopping the scan")
            scanning = false
            leScanner.stopScan(scanCallback)
            Log.d(TAG, "informing UI about the current scan status and the found devices")
            EventBus.getDefault().post(EventScanningChanged(scanning))
            EventBus.getDefault().post(EventDevicesFound(foundDevices))
        }
    }

    private fun startScan(){
        Log.d(TAG, "startScan")
        if(!scanning){
            Log.d(TAG, "starting a scan")
            handler.postDelayed(timeoutRunnable, DURATION)
            scanning = true
            Log.d(TAG, "informing UI that we started scanning")
            EventBus.getDefault().post(EventScanningChanged(scanning))
            leScanner.startScan(scanCallback)
        }
    }
}