package io.eberlein.btinformer.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.eberlein.oui.AndroidOUI
import io.eberlein.oui.OUI
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class OUIService: Service() {
    private lateinit var oui: OUI

    class EventOUI(val oui: OUI)
    class EventRequestOUI()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventRequestOUI(e: EventRequestOUI){
        EventBus.getDefault().post(EventOUI(oui))
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread {
            oui = AndroidOUI(applicationContext)
            EventBus.getDefault().post(EventOUI(oui))
        }.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}