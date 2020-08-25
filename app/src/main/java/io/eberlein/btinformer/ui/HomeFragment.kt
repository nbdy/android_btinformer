package io.eberlein.btinformer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.vh_device.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var adapter: DeviceAdapter

    class DeviceHolder(itemView: View) : RAdapter.RVH<Device>(itemView) {
        override fun set(item: Device){
            itemView.tv_name.text = item.name
            itemView.tv_mac.text = item.address
            itemView.tv_rssi.text = item.rssi.toString()
        }
    }

    class DeviceAdapter(onClickListener: (Device) -> Unit) : RAdapter<DeviceHolder, Device>(onClickListener) {
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
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        adapter = DeviceAdapter {  } // todo show device specific data in a dialog
        view.btnSearch.setOnClickListener {
            adapter.clear()
            EventBus.getDefault().post(ScannerService.EventChangeScan())
        }
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
        Log.d(TAG, "onEventScanningChanged")
        if(e.scanning) btnSearch.setImageResource(R.drawable.baseline_search_off_white_48)
        else btnSearch.setImageResource(R.drawable.baseline_search_white_48)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventDeviceFound(e: ScannerService.EventFoundDevice){
        adapter.add(e.device)
    }
}