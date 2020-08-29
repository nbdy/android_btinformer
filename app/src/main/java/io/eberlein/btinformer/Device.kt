package io.eberlein.btinformer

import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import io.paperdb.Book
import io.paperdb.Paper
import java.util.*
import kotlin.collections.ArrayList

class Device(
    val address: String,
    var name: String,
    val rssi: Int,
    val connectable: Boolean,
    val txPower: Int,
    var uuids: ArrayList<ParcelUuid>
): DBObject() {
    constructor(ss: ScanResult): this(ss.device.address, "", ss.rssi, ss.isConnectable, ss.txPower, ArrayList()){
        if(ss.device.name != null) name = ss.device.name
        if(ss.device.uuids != null) uuids = ss.device.uuids.toCollection(ArrayList())
    }

    companion object {
        private const val bookName = "device"

        fun book(): Book {
            return Paper.book(bookName)
        }
    }
}