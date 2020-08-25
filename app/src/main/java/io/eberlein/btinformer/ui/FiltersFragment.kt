package io.eberlein.btinformer.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.eberlein.btinformer.Filter
import io.eberlein.btinformer.FilterType
import io.eberlein.btinformer.R
import io.eberlein.btinformer.RAdapter
import io.paperdb.Paper
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.view.*
import kotlinx.android.synthetic.main.fragment_filters.view.*
import kotlinx.android.synthetic.main.vh_filter.view.*
import kotlin.collections.ArrayList

// todo allow deletion of filters
class FiltersFragment : Fragment() {
    private lateinit var adapter: FilterAdapter
    private lateinit var dialog: ViewFilterDialog

    // todo save filter if dialog closed
    class ViewFilterDialog(c: Context, private val view: View) : Dialog(c) {
        private var typeAdapter: ArrayAdapter<String>
        private lateinit var filter: Filter

        init {
            val al = ArrayList<String>()
            enumValues<FilterType>().iterator().forEach { ft -> al.add(ft.name) }
            typeAdapter = ArrayAdapter(c, android.R.layout.simple_spinner_item, al)
            setContentView(R.layout.dialog_filter)
            btn_save.setOnClickListener {save()}
        }

        fun save(){
            filter.name = et_name.text.toString()
            filter.data = et_data.text.toString()
            filter.type = enumValues<FilterType>()[sp_data_type.selectedItemPosition]
            filter.save()
        }

        fun set(f: Filter): Dialog {
            filter = f
            setTitle(f.name)
            view.et_name.setText(f.name)
            view.et_data.setText(f.data)
            view.sp_data_type.setSelection(typeAdapter.getPosition(f.type.name))
            return this
        }
    }

    class FilterHolder(itemView: View) : RAdapter.RVH<Filter>(itemView) {
        override fun set(item: Filter) {
            itemView.tv_name.text = item.name
        }
    }

    class FilterAdapter(onClickListener: (Filter) -> Unit) : RAdapter<FilterHolder, Filter>(onClickListener) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterHolder {
            return FilterHolder(inflate(parent.context, R.layout.vh_filter, parent))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)
        dialog = context?.let { ViewFilterDialog(it, inflater.inflate(R.layout.dialog_filter, null, false)) }!!
        adapter = FilterAdapter { filter -> dialog.set(filter).show() }
        view.rv_filters.adapter = adapter
        view.rv_filters.layoutManager = LinearLayoutManager(context)
        view.rv_filters.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        adapter.add(Filter.getAll())
        view.fab_add.setOnClickListener {
            dialog.set(Filter("", "", FilterType.MAC))
            dialog.show()
        }
        return view
    }
}