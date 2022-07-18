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

    /** 数据变动监听器 */
    private var mPdrOnTreeChangedListener: ((data: List<T>) -> Unit)? = null

    override fun getItemViewType(position: Int): Int = getItem(position)?.getCls().hashCode()

    override fun setItemClick(holder: DataVBViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = getItem(position) ?: return@setOnClickListener
            if (item is RvTreeGroup && item.expandEnable() && item.getTreeItems().isNotEmpty()){
                val list = getData()?.toArrayList() ?: return@setOnClickListener
                val isExpand = item.isExpandItem()
                if (isExpand) { //收回已经展开的子项（包含子项内的子项）
                    list.removeAll(collapsedItemsChildren(item).toSet())
                } else {//展开子项
                    list.addAll(position + 1, item.getTreeItems())
                }
                item.onExpandChanged(!isExpand)
                invokeTreeChangedListener(list)
                setData(list)
                notifyDataSetChanged()
                return@setOnClickListener
            }
            mPdrOnItemClickListener?.invoke(holder, item, position)
        }
    }

    /** 折叠子项数据 */
    private fun collapsedItemsChildren(item: RvTreeGroup): List<RvTreeItem> {
        val allData = arrayListOf<RvTreeItem>()
        val list = item.getTreeItems().toMutableList()
        list.forEach {
            if (it is RvTreeGroup && it.isExpandItem() && it.getTreeItems().isNotEmpty()) {
                it.onExpandChanged(false)
                allData.addAll(collapsedItemsChildren(it))
            }
        }
        allData.addAll(list)
        return allData
    }

    /** 设置树结构数据[data] */
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

    /** 设置树状节点变动监听器[listener] */
    fun setOnTreeChangedListener(listener: ((data: List<T>) -> Unit)?) {
        mPdrOnTreeChangedListener = listener
    }

    @Suppress("UNCHECKED_CAST")
    private fun invokeTreeChangedListener(list: List<RvTreeItem>) {
        val data = ArrayList<T>()
        list.forEach {
            val t = it as T
            data.add(t)
        }
        mPdrOnTreeChangedListener?.invoke(data)
    }
}