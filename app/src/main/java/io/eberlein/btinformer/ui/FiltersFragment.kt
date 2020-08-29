package io.eberlein.btinformer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.*
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.view.*
import kotlinx.android.synthetic.main.fragment_filters.view.*
import kotlinx.android.synthetic.main.vh_filter.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.ArrayList

// todo allow deletion of filters
class FiltersFragment : Fragment() {
    private lateinit var adapter: FilterAdapter
    private lateinit var dialog: ViewFilterDialog

    class EventFilterSelected(val filter: Filter)
    class EventFilterSaved(val filter: Filter)

    class ViewFilterDialog(c: Context) : DBObjectDialog<Filter>(c, R.layout.dialog_filter) {
        private lateinit var filter: Filter
        private var options: ArrayList<String> = ArrayList()
        private var selectedOption: String

        init {
            FilterType.values().forEach { options.add(it.name) }
            selectedOption = options[0]
            view.sp_data_type.adapter = ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, options)
        }

        override fun save(){
            filter.name = et_name.text.toString()
            filter.data = et_data.text.toString()
            filter.type = selectedOption
            filter.save()
            EventBus.getDefault().post(EventFilterSaved(filter))
        }

        override fun set(i: Filter) {
            filter = i
            setTitle(i.name)
            view.et_name.setText(i.name)
            view.et_data.setText(i.data)
            view.sp_data_type.setSelection(options.indexOf(i.type))
        }
    }

    class FilterHolder(itemView: View) : RAdapter.RVH<Filter>(itemView) {
        override fun set(item: Filter) {
            itemView.tv_name.text = item.name
        }
    }

    class FilterAdapter: DBObjectAdapter<FilterHolder, Filter>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterHolder {
            return FilterHolder(inflate(parent.context, R.layout.vh_filter, parent))
        }

        override fun onItemClick(item: Filter) {
            EventBus.getDefault().post(EventFilterSelected(item))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)
        adapter = FilterAdapter()
        dialog = ViewFilterDialog(requireContext())
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