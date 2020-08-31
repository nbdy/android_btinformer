package io.eberlein.btinformer.dialogs

import android.content.Context
import android.widget.Toast
import io.eberlein.btinformer.R
import io.eberlein.btinformer.objects.GPSLogEntry
import kotlinx.android.synthetic.main.dialog_gps_log_entry.view.*

class GPSLogEntryDialog(c: Context) : DBObjectDialog<GPSLogEntry>(c, R.layout.dialog_gps_log_entry){
    override fun save() {}

    override fun set(i: GPSLogEntry) {
        super.set(i)
        view.tv_lat.text = i.location.latitude.toString()
        view.tv_lng.text = i.location.longitude.toString()
        view.btn_view_location.setOnClickListener {
            Toast.makeText(context, "this still has to be implemented", Toast.LENGTH_LONG).show()
        }

        view.btn_view_device.setOnClickListener {
            val dd = DeviceDialog(context)
            dd.set(i.device)
            dd.show()
        }

        view.btn_view_filter.setOnClickListener {
            val fd = FilterDialog(context)
            fd.set(i.filter)
            fd.show()
        }
    }
}