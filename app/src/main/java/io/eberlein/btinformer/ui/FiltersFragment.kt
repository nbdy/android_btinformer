package io.eberlein.btinformer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.*
import io.eberlein.btinformer.adapters.FilterAdapter
import io.eberlein.btinformer.dialogs.FilterDialog
import io.eberlein.btinformer.objects.Filter
import kotlinx.android.synthetic.main.fragment_filters.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// todo allow deletion of filters
class FiltersFragment : Fragment() {
    private lateinit var adapter: FilterAdapter
    private lateinit var dialog: FilterDialog

    class EventFilterSelected(val filter: Filter)
    class EventFilterSaved(val filter: Filter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)
        adapter = FilterAdapter()
        dialog = FilterDialog(requireContext())
        view.rv_filters.adapter = adapter
        view.rv_filters.layoutManager = LinearLayoutManager(context)
        view.rv_filters.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.add(Filter.all())
        view.fab_add.setOnClickListener {
            dialog.set(Filter.getOrCreate("new"))
            dialog.show()
        }
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventFilterSelected(e: EventFilterSelected){
        dialog.set(e.filter)
        dialog.show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventFilterSaved(e: EventFilterSaved){
        adapter.add(e.filter)
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