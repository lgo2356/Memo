package com.example.memo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memo.R
import com.example.memo.dto.Memo
import kotlinx.android.synthetic.main.layout_memo_card.view.*

class MemoRecyclerAdapter : RecyclerView.Adapter<MemoRecyclerAdapter.ViewHolder>() {

    var items: List<Memo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var checkedMemoPositions: MutableSet<Int> = mutableSetOf()
    private var clickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.layout_memo_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.title.text = item.title
        holder.content.text = item.content

        holder.chkBox.setOnCheckedChangeListener { _, _ ->
            if (holder.chkBox.isChecked) {
                checkedMemoPositions.add(position)
            }
        }
        holder.itemView.setOnClickListener { clickListener?.onItemClick(it, position) }
    }

    // Return checked memo positions
    fun getCheckedMemoPositions(): MutableSet<Int> {
        return checkedMemoPositions
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.txtView_title
        val content: TextView = itemView.txtView_content
        val chkBox: CheckBox = itemView.chkBox_memo_card
    }
}
