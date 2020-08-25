package io.eberlein.btinformer

import android.location.Location
import java.sql.Timestamp

class GPSLogEntry(
    val timestamp: Timestamp,
    val device: Device,
    val location: Location
) {

    companion object {
        fun getAll(): ArrayList<GPSLogEntry>{
            return ArrayList() // todo
        }
    }
}