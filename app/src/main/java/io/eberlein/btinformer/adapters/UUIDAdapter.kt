package io.eberlein.btinformer.adapters

import android.os.ParcelUuid
import android.view.View
import android.view.ViewGroup
import io.eberlein.btinformer.R
import kotlinx.android.synthetic.main.vh_uuid.view.*

class UUIDHolder(itemView: View): RAdapter.RVH<ParcelUuid>(itemView){
    override fun set(item: ParcelUuid) {
        itemView.tv_uuid.text = item.toString()
    }
}

class UUIDAdapter: RAdapter<UUIDHolder, ParcelUuid>() {
    override fun onItemClick(item: ParcelUuid) {
        // todo create filter dialog
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UUIDHolder {
        return UUIDHolder(inflate(parent.context, R.layout.vh_uuid, parent))
    }
}