package io.eberlein.btinformer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import io.eberlein.btinformer.objects.Device
import io.eberlein.btinformer.objects.Filter

class NotificationHelper {
    companion object {
        private val cId: String = "BTInformer"

        fun setup(context: Context){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val c = NotificationChannel(cId, "BTInformer", NotificationManager.IMPORTANCE_DEFAULT).apply { description = "Device matched filter" }
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(c)
            }
        }

        fun create(context: Context, device: Device, filter: Filter){
            var b = NotificationCompat.Builder(context, cId)
                .setContentTitle("Device matched filter")
                .setContentText("Filter: ${filter.name} | ${filter.type} -> ${filter.data}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
    }
}