package io.eberlein.btinformer.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LocationService: Service() {
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LL

    class EventGetCurrentLocation
    class EventCurrentLocation(val location: Location)

    class LL: LocationListener {
        lateinit var currentLocation: Location

        override fun onLocationChanged(location: Location?) {
            if(location != null) {
                currentLocation = location
                EventBus.getDefault().post(EventCurrentLocation(location))
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventGetCurrentLocation(e: EventGetCurrentLocation){
        EventBus.getDefault().post(EventCurrentLocation(locationListener.currentLocation))
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LL()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 420, 4.2f, locationListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}