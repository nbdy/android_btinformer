package io.eberlein.btinformer.ui

import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.R
import io.eberlein.btinformer.RAdapter
import io.eberlein.btinformer.ScannerService
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.vh_device.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : Fragment() {
    private val adapter = DeviceAdapter()

    class DeviceHolder(itemView: View) : RAdapter.RVH<ScanResult>(itemView) {
        override fun set(item: ScanResult){
            itemView.tv_name.text = item.device.name
            itemView.tv_mac.text = item.device.address
            itemView.tv_rssi.text = item.rssi.toString()
        }
    }

    class DeviceAdapter : RAdapter<DeviceHolder, ScanResult>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
            return DeviceHolder(View.inflate(parent.context, R.layout.vh_device, parent))
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.btnSearch.setOnClickListener { EventBus.getDefault().post(ScannerService.EventChangeScan()) }
        view.rv_devices.layoutManager = LinearLayoutManager(context)
        view.rv_devices.adapter = adapter
        view.rv_devices.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventScanningChanged(e: ScannerService.EventScanningChanged){
        if(e.scanning) btnSearch.setImageResource(R.drawable.baseline_search_off_white_48)
        else btnSearch.setImageResource(R.drawable.baseline_search_white_48)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventDevicesFound(e: ScannerService.EventDevicesFound){
        adapter.add(e.devices)
    }
}