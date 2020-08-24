package io.eberlein.btinformer

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

abstract class RAdapter<VH: RAdapter.RVH<T>, T>() : RecyclerView.Adapter<VH>() {
    protected val items = ArrayList<T>()

    abstract class RVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun set(item: T)
    }

    open fun onClick(position: Int){

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.set(items[position])
        holder.itemView.setOnClickListener { onClick(position) }
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