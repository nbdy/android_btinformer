package io.eberlein.btinformer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.eberlein.btinformer.*
import io.eberlein.btinformer.dialogs.FilterDialog
import io.eberlein.btinformer.services.OUIService
import io.eberlein.oui.OUI
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeFragment: Fragment(){
    private lateinit var oui: OUI
    private var valuesSet = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().post(OUIService.EventRequestOUI())
    }

    private fun setValues(){
        tv_mac.text = oui.randomMAC()
        tv_mac.setOnLongClickListener {FilterDialog.forMAC(requireContext(), tv_mac.text as String).show(); false}
        tv_uuid.text = UUID.randomUUID().toString()
        tv_uuid.setOnLongClickListener {FilterDialog.forUUID(requireContext(), tv_uuid.text as String).show(); false}
        valuesSet = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOUI(e: OUIService.EventOUI) {
        oui = e.oui
        if(!valuesSet) setValues()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}