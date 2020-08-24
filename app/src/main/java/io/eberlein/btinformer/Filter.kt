package io.eberlein.btinformer

import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import io.paperdb.Paper
import kotlin.collections.ArrayList

enum class FilterType {
    NAME, MAC, UUID
}

class Filter(
    val name: String,
    val data: String,
    val type: FilterType
) {
    fun applies(ss: ScanResult): Boolean {
        when (type) {
            FilterType.MAC -> return ss.device.address == data
            FilterType.NAME -> return ss.device.name == data
            FilterType.UUID -> {
                for(u: ParcelUuid in ss.device.uuids) {
                    if(u.uuid.toString() == data) return true
                }
            }
        }
        return false
    }

    fun apply(lst: ArrayList<ScanResult>): ArrayList<ScanResult> {
        val r = ArrayList<ScanResult>()
        for(ss: ScanResult in lst){
            if(applies(ss)) r.add(ss)
        }
        return r
    }

    fun save(){
        Paper.book("Filters").write(name, this)
    }

    companion object {
        fun get(name: String): Filter {
            return Paper.book("Filters").read(name)
        }

        fun getAll(): ArrayList<Filter> {
            val r = ArrayList<Filter>()
            Paper.book("Filters").allKeys.forEach { r.add(get(it)) }
            return r
        }
    }
}