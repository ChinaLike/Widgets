package com.core.widget.pager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.core.ex.onDebouncedClick
import com.core.widget.R

/**
 * 数据适配器
 * @author like
 * @date 10/13/21 11:36 AM
 */
internal class GridAdapter(private val page:Int,private val dataList: MutableList<View>) : RecyclerView.Adapter<GridAdapter.GridViewHolder>() {

    /**
     * Item点击回调
     */
    var onItemClickCallback:OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lk_adapter_pager_grid, parent, false)
        return GridViewHolder(view)
    }


    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        holder.rootView.removeAllViews()
        holder.rootView.onDebouncedClick {
            onItemClickCallback?.onItemClick(position,page)
        }
        holder.rootView.addView(dataList[position])
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    internal class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootView: FrameLayout = view.findViewById(R.id.rootView)
    }

}