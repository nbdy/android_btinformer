package io.eberlein.btinformer

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_filter.view.*

abstract class DBObjectDialog<T: DBObject>(context: Context, contentView: Int): Dialog(context) {
    protected val view: View = LayoutInflater.from(context).inflate(contentView, null, false)
    private lateinit var item: T

    init {
        this.setContentView(view)
        view.btn_save.setOnClickListener { save(); dismiss() }
        view.btn_cancel.setOnClickListener { dismiss() }
    }

    abstract fun set(i: T)

    abstract fun save()
}