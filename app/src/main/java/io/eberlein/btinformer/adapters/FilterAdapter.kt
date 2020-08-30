package io.eberlein.btinformer.adapters

import android.view.View
import android.view.ViewGroup
import io.eberlein.btinformer.objects.Filter
import io.eberlein.btinformer.R
import io.eberlein.btinformer.ui.FiltersFragment
import kotlinx.android.synthetic.main.vh_filter.view.*
import org.greenrobot.eventbus.EventBus

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
        EventBus.getDefault().post(FiltersFragment.EventFilterSelected(item))
    }
}