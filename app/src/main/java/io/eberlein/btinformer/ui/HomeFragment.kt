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
import io.eberlein.btinformer.adapters.DeviceAdapter
import io.eberlein.btinformer.dialogs.DeviceDialog
import io.eberlein.btinformer.objects.Device
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var adapter: DeviceAdapter
    private lateinit var deviceDialog: DeviceDialog

    class EventDeviceSelected(val device: Device)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        adapter = DeviceAdapter()
        deviceDialog = DeviceDialog(requireContext())
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventDeviceSelected(e: EventDeviceSelected){
        deviceDialog.set(e.device)
        deviceDialog.show()
    }
}