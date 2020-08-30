package io.eberlein.btinformer.dialogs

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.objects.Device
import io.eberlein.btinformer.R
import io.eberlein.btinformer.adapters.UUIDAdapter
import kotlinx.android.synthetic.main.dialog_device.view.*


class DeviceDialog(context: Context): DBObjectDialog<Device>(context, R.layout.dialog_device){
    private val uuidAdapter = UUIDAdapter()

    init {
        view.rv_uuids.adapter = uuidAdapter
        view.rv_uuids.layoutManager = LinearLayoutManager(context)
        view.rv_uuids.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun set(i: Device) {
        super.set(i)
        view.tv_name.text = i.name
        view.tv_mac.text = i.address
        uuidAdapter.clear()
        uuidAdapter.add(i.uuids)
    }

    override fun save() {
        TODO("Not yet implemented")
    }

}