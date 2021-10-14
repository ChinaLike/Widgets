package com.core.widget.pager

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.widget.R
import com.core.widget.divider.XGridDividerDecoration

/**
 * 页面适配器
 * @author like
 * @date 10/13/21 1:41 PM
 */
internal class PagerAdapter(private val context: Context,private val dataList: MutableList<MutableList<View>>) :
    RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {

    /**
     * Item点击回调
     */
    var onItemClickCallback:OnItemClickCallback? = null

    /**
     * 水平分割线宽度
     */
    var horizontalDivider: Int = 0

    /**
     * 垂直分割线宽度
     */
    var verticalDivider: Int = 0

    /**
     * 一页显示的行数
     */
    var spanSize:Int = 0

    /**
     * Called when RecyclerView needs a new [ViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lk_adapter_pager_viewpager, parent, false)
        return PagerViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ViewHolder.getBindingAdapterPosition] which
     * will have the updated adapter position.
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        if (spanSize <= 0){
            return
        }
        val bean: MutableList<View> = dataList[position]
        val adapter: GridAdapter = GridAdapter(position,bean).apply {
            onItemClickCallback = this@PagerAdapter.onItemClickCallback
        }
        if (holder.recyclerView.layoutManager == null) {
            holder.recyclerView.layoutManager = GridLayoutManager(context, spanSize)
            holder.recyclerView.addItemDecoration(XGridDividerDecoration(horizontalDivider,verticalDivider, Color.TRANSPARENT))
        }
        holder.recyclerView.adapter = adapter
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return dataList.size
    }


    internal class PagerViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
    }
}