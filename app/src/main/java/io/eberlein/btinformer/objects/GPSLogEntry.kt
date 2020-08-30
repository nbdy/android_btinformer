package io.eberlein.btinformer.objects

import android.location.Location
import java.sql.Timestamp

class GPSLogEntry(
    val timestamp: Timestamp,
    val device: Device,
    val location: Location
): DBObject() {
    companion object {
        fun getAll(): ArrayList<GPSLogEntry>{
            return ArrayList() // todo
        }
    }
}