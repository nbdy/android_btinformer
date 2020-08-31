package io.eberlein.btinformer.adapters

import android.view.View
import android.view.ViewGroup
import io.eberlein.btinformer.objects.Device
import io.eberlein.btinformer.R
import io.eberlein.btinformer.ui.HomeFragment
import io.eberlein.btinformer.ui.ScanFragment
import kotlinx.android.synthetic.main.vh_device.view.*
import org.greenrobot.eventbus.EventBus


class DeviceHolder(itemView: View) : RAdapter.RVH<Device>(itemView) {
    override fun set(item: Device){
        itemView.tv_name.text = item.name
        itemView.tv_mac.text = item.address
        itemView.tv_rssi.text = item.rssi.toString()
    }
}

class DeviceAdapter: DBObjectAdapter<DeviceHolder, Device>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        return DeviceHolder(inflate(parent.context, R.layout.vh_device, parent))
    }

    override fun add(item: Device){
        var r = -1
        for(i in items.indices){
            if(items[i].address == item.address) {
                r = i
            }
        }
        if(r != -1) {
            items[r] = item
            notifyItemChanged(r)
        } else {
            items.add(item)
            notifyItemInserted(items.size)
        }
    }

    override fun onItemClick(item: Device) {
        EventBus.getDefault().post(ScanFragment.EventDeviceSelected(item))
    }
}