package io.eberlein.btinformer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.eberlein.btinformer.*
import io.eberlein.btinformer.dialogs.FilterDialog
import io.eberlein.btinformer.services.OUIService
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class HomeFragment: Fragment(){
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventOUI(e: OUIService.EventOUI) {
        tv_mac.text = e.oui.randomMAC()
        tv_mac.setOnLongClickListener {FilterDialog.forMAC(requireContext(), tv_mac.text as String).show(); false}
        tv_uuid.text = UUID.randomUUID().toString()
        tv_uuid.setOnLongClickListener {FilterDialog.forUUID(requireContext(), tv_uuid.text as String).show(); false}
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