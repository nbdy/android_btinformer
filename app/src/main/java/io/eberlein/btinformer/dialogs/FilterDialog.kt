package io.eberlein.btinformer.dialogs

import android.content.Context
import android.widget.ArrayAdapter
import io.eberlein.btinformer.objects.Filter
import io.eberlein.btinformer.objects.FilterType
import io.eberlein.btinformer.R
import io.eberlein.btinformer.ui.FiltersFragment
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.view.*
import org.greenrobot.eventbus.EventBus

class FilterDialog(c: Context) : DBObjectDialog<Filter>(c, R.layout.dialog_filter) {
    private lateinit var filter: Filter
    private var options: ArrayList<String> = ArrayList()
    private var selectedOption: String

    init {
        FilterType.values().forEach { options.add(it.name) }
        selectedOption = options[0]
        view.sp_data_type.adapter = ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, options)
    }

    companion object {
        fun forName(c: Context, name: String): FilterDialog {
            val d = FilterDialog(c)
            d.set(Filter.fromName(name))
            return d
        }

        fun forMAC(c: Context, mac: String): FilterDialog {
            val d = FilterDialog(c)
            d.set(Filter.fromMAC(mac))
            return d
        }

        fun forUUID(c: Context, uuid: String): FilterDialog {
            val d = FilterDialog(c)
            d.set(Filter.fromUUID(uuid))
            return d
        }
    }

    override fun save(){
        filter.name = et_name.text.toString()
        filter.data = et_data.text.toString()
        filter.type = selectedOption
        filter.save()
        EventBus.getDefault().post(FiltersFragment.EventFilterSaved(filter))
    }

    override fun set(i: Filter) {
        filter = i
        setTitle(i.name)
        view.et_name.setText(i.name)
        view.et_data.setText(i.data)
        view.sp_data_type.setSelection(options.indexOf(i.type))
    }
}