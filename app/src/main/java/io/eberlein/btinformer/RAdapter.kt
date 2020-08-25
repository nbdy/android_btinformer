package io.eberlein.btinformer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class RAdapter<VH: RAdapter.RVH<T>, T>(private val onClickListener: (T) -> Unit) : RecyclerView.Adapter<VH>() {
    protected val items = ArrayList<T>()

    abstract class RVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun set(item: T)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val i = items[position]
        holder.set(i)
        holder.itemView.setOnClickListener { onClickListener(i) }
    }

    open fun add(item: T){
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

    fun clear(){
        items.clear()
        notifyDataSetChanged()
    }

    companion object {
        fun inflate(c: Context, lid: Int, p: ViewGroup): View {
            return LayoutInflater.from(c).inflate(lid, p, false)
        }
    }
}