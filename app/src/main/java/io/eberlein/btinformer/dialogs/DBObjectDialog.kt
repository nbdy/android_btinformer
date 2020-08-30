package io.eberlein.btinformer.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import io.eberlein.btinformer.objects.DBObject
import kotlinx.android.synthetic.main.dialog_filter.view.*

abstract class DBObjectDialog<T: DBObject>(context: Context, contentView: Int): Dialog(context) {
    protected val view: View = LayoutInflater.from(context).inflate(contentView, null, false)
    private lateinit var item: T

    init {
        this.setContentView(view)
        if(view.btn_save != null) view.btn_save.setOnClickListener { save(); dismiss() }
        if(view.btn_cancel != null) view.btn_cancel.setOnClickListener { dismiss() }
    }

    open fun set(i: T) {
        item = i
    }

    abstract fun save()
}