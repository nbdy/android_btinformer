package io.eberlein.btinformer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_filter.view.*

abstract class DBObjectAdapter<VH: RAdapter.RVH<T>, T: DBObject>: RAdapter<VH, T>() {
    override fun onDeleteClick(item: T) {
        item.delete()
        notifyDataSetChanged()
    }
}

abstract class RAdapter<VH: RAdapter.RVH<T>, T>: RecyclerView.Adapter<VH>() {
    protected val items = ArrayList<T>()

    fun showBtnDelete(vh: VH){
        val v = vh.itemView.btn_delete.visibility
        vh.itemView.btn_delete.visibility = if(v == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }

    abstract class RVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun set(item: T)
    }

    open fun onItemLongClick(vh: VH) {
        showBtnDelete(vh)
    }

    abstract fun onItemClick(item: T)

    abstract fun onDeleteClick(item: T)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val i = items[position]
        holder.set(i)
        holder.itemView.setOnClickListener { onItemClick(i) }
        holder.itemView.setOnLongClickListener { onItemLongClick(holder); false }
        holder.itemView.btn_delete.setOnClickListener { onDeleteClick(i) }
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