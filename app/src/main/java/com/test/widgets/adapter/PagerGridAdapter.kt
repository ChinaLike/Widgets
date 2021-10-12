package com.test.widgets.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.test.widgets.R

/**
 *
 * @author like
 * @date 10/12/21 2:53 PM
 */
class PagerGridAdapter(private val context: Context, private val mList: MutableList<Int>) :
    RecyclerView.Adapter<PagerGridAdapter.PagerGridViewHolder>() {


    class PagerGridViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AppCompatTextView = itemView.findViewById(R.id.name)
        val icon: AppCompatImageView = itemView.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerGridViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_pager_grid, parent, false)
        return PagerGridViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerGridViewHolder, position: Int) {
        val bean = mList[position]
        holder.name.text = "$bean"
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
}