package io.eberlein.btinformer.objects

import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import io.eberlein.oui.OUI
import io.eberlein.oui.OUIEntry
import io.paperdb.Book
import io.paperdb.Paper
import kotlin.collections.ArrayList

class Device(
    val address: String,
    var manufacturer: String,
    var name: String,
    val rssi: Int,
    val connectable: Boolean,
    val txPower: Int,
    var uuids: ArrayList<ParcelUuid>
): DBObject() {
    constructor(ss: ScanResult): this(ss.device.address, "", "", ss.rssi, ss.isConnectable, ss.txPower, ArrayList()){
        if(ss.device.name != null) name = ss.device.name
        if(ss.device.uuids != null) uuids = ss.device.uuids.toCollection(ArrayList())
    }

    constructor(ss: ScanResult, oui: OUI): this(ss) {
        val r = oui.lookupByMac(address)
        if(r != null) manufacturer = r.orgName
    }

    companion object {
        private const val bookName = "device"

        fun book(): Book {
            return Paper.book(bookName)
        }
    }
}