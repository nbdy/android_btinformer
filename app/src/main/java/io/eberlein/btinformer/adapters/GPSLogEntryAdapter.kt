package io.eberlein.btinformer.adapters

import android.view.View
import android.view.ViewGroup
import io.eberlein.btinformer.objects.GPSLogEntry
import io.eberlein.btinformer.R
import io.eberlein.btinformer.ui.GPSLogFragment
import kotlinx.android.synthetic.main.vh_gps_log_entry.view.*
import org.greenrobot.eventbus.EventBus

class GPSLogViewHolder(view: View) : RAdapter.RVH<GPSLogEntry>(view){
    override fun set(item: GPSLogEntry) {
        itemView.tv_bt_mac.text = item.device.address
        itemView.tv_bt_name.text = item.device.name
        itemView.tv_timestamp.text = item.timestamp.toString()
    }
}

class GPSLogAdapter : DBObjectAdapter<GPSLogViewHolder, GPSLogEntry>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GPSLogViewHolder {
        return GPSLogViewHolder(inflate(parent.context, R.layout.vh_gps_log_entry, parent))
    }

    override fun onItemClick(item: GPSLogEntry) {
        EventBus.getDefault().post(GPSLogFragment.EventGPSLogEntrySelected(item))
    }

}