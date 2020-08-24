package io.eberlein.btinformer

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class RAdapter<VH: RAdapter.RVH<T>, T> : RecyclerView.Adapter<VH>() {
    private val items = ArrayList<T>()

    abstract class RVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun set(item: T)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.set(items[position])
    }

    fun add(item: T){
        if(!items.contains(item)) {
            items.add(item)
            notifyItemInserted(itemCount)
        } else {
            val i = items.indexOf(item)
            items[i] = item
            notifyItemChanged(i)
        }
    }

    fun add(items: ArrayList<T>){
        items.forEach { add(it) }
    }
}