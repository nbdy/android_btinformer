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
    fun applies(ss: Device): Boolean {
        when (type) {
            FilterType.MAC -> return ss.address == data
            FilterType.NAME -> return ss.name == data
            FilterType.UUID -> {
                for(u: ParcelUuid in ss.uuids) {
                    if(u.uuid.toString() == data) return true
                }
            }
        }
        return false
    }

    fun apply(lst: ArrayList<Device>): ArrayList<Device> {
        val r = ArrayList<Device>()
        for(ss: Device in lst){
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