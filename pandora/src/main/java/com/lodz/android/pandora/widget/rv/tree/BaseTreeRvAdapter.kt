package com.lodz.android.pandora.widget.rv.tree

import android.content.Context
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * RV树结构基类
 * @author zhouL
 * @date 2022/7/15
 */
abstract class BaseTreeRvAdapter<T : RvTreeItem>(context: Context) : AbsRvAdapter<RvTreeItem, DataVBViewHolder>(context) {

    override fun getItemViewType(position: Int): Int = getItem(position)?.getCls().hashCode()

    override fun setItemClick(holder: DataVBViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = getItem(position) ?: return@setOnClickListener
            if (item is RvTreeGroup && item.expandEnable() && item.getTreeItems().isNotEmpty()){
                val list = getData()?.toArrayList() ?: return@setOnClickListener
                val isExpand = item.isExpandItem()
                if (isExpand) {//已经展开要收回
                    list.removeAll(item.getTreeItems().toSet())
                } else {
                    list.addAll(position + 1, item.getTreeItems())
                }
                item.onExpandChanged(!isExpand)
                setData(list)
                notifyDataSetChanged()
                return@setOnClickListener
            }
            mPdrOnItemClickListener?.invoke(holder, item, position)
        }
    }

    /** 设置树结构数据 */
    fun setTreeData(data: MutableList<T>?) {
        val list = ArrayList<RvTreeItem>()
        if (data == null) {
            super.setData(list)
            return
        }
        data.forEach {
            list.add(it)
        }
        super.setData(list)
    }
}