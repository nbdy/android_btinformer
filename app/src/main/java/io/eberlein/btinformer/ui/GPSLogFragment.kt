package io.eberlein.btinformer.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.GPSLogEntry
import io.eberlein.btinformer.R
import io.eberlein.btinformer.RAdapter
import kotlinx.android.synthetic.main.fragment_gps_log.view.*
import kotlinx.android.synthetic.main.vh_gps_log_entry.view.*

class GPSLogFragment : Fragment() {
    private lateinit var dialog: Dialog
    private lateinit var adapter: GPSLogAdapter

    class GPSLogEntryDialog(c: Context) : Dialog(c){

    }

    class GPSLogViewHolder(view: View) : RAdapter.RVH<GPSLogEntry>(view){
        override fun set(item: GPSLogEntry) {
            itemView.tv_bt_mac.text = item.device.address
            itemView.tv_bt_name.text = item.device.name
            itemView.tv_timestamp.text = item.timestamp.toString()
        }
    }

    class GPSLogAdapter(onClickListener: (GPSLogEntry) -> Unit) : RAdapter<GPSLogViewHolder, GPSLogEntry>(onClickListener) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GPSLogViewHolder {
            return GPSLogViewHolder(inflate(parent.context, R.layout.vh_gps_log_entry, parent))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_gps_log, container, false)
        dialog = context?.let { GPSLogEntryDialog(it) }!!
        adapter = GPSLogAdapter {  }
        v.rv_gps_log.adapter = adapter // todo
        v.rv_gps_log.layoutManager = LinearLayoutManager(context)
        v.rv_gps_log.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.add(GPSLogEntry.getAll())
        return v
    }
}