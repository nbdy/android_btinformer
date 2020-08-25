package io.eberlein.btinformer

import android.app.Dialog
import android.content.Context

abstract class GDialog<T>(c: Context, cid: Int) : Dialog(c) {
    init {
        setContentView(cid)
    }

    abstract fun set(item: T)
}