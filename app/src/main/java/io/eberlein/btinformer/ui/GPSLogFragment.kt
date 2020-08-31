package io.eberlein.btinformer.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.objects.GPSLogEntry
import io.eberlein.btinformer.R
import io.eberlein.btinformer.adapters.GPSLogAdapter
import io.eberlein.btinformer.dialogs.GPSLogEntryDialog
import kotlinx.android.synthetic.main.fragment_gps_log.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class GPSLogFragment : Fragment() {
    private lateinit var dialog: Dialog
    private lateinit var adapter: GPSLogAdapter

    class EventGPSLogEntrySelected(val logEntry: GPSLogEntry)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_gps_log, container, false)
        dialog = GPSLogEntryDialog(requireContext())
        adapter = GPSLogAdapter()
        v.rv_gps_log.adapter = adapter
        v.rv_gps_log.layoutManager = LinearLayoutManager(context)
        v.rv_gps_log.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.add(GPSLogEntry.getAll())
        return v
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventGPSLogEntrySelected(e: EventGPSLogEntrySelected){

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