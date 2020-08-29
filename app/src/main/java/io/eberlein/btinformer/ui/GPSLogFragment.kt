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
import io.eberlein.btinformer.DBObjectAdapter
import io.eberlein.btinformer.GPSLogEntry
import io.eberlein.btinformer.R
import io.eberlein.btinformer.RAdapter
import kotlinx.android.synthetic.main.fragment_gps_log.view.*
import kotlinx.android.synthetic.main.vh_gps_log_entry.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GPSLogFragment : Fragment() {
    private lateinit var dialog: Dialog
    private lateinit var adapter: GPSLogAdapter

    class EventGPSLogEntrySelected(val logEntry: GPSLogEntry)

    class GPSLogEntryDialog(c: Context) : Dialog(c){

    }

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
            EventBus.getDefault().post(EventGPSLogEntrySelected(item))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_gps_log, container, false)
        dialog = GPSLogEntryDialog(requireContext())
        adapter = GPSLogAdapter()
        v.rv_gps_log.adapter = adapter // todo
        v.rv_gps_log.layoutManager = LinearLayoutManager(context)
        v.rv_gps_log.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.add(GPSLogEntry.getAll())
        return v
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventGPSLogEntrySelected(e: EventGPSLogEntrySelected){
        // todo
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}