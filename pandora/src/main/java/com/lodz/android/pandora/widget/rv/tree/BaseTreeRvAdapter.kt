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

    /** 折叠所有子项 */
    fun collapsedAll() {
        val data = getData()?.toMutableList() ?: return
        val rootList = ArrayList<RvTreeItem>()
        data.forEach {
            if (it.isRootItem()) {//得到根节点数据
                rootList.add(it)
            }
        }
        setData(setChildrenExpandChanged(rootList, false).toMutableList())//修改折叠标志位
        invokeTreeChangedListener(rootList)
        notifyDataSetChanged()
    }

    /** 设置子项的折叠标志位 */
    private fun setChildrenExpandChanged(list: List<RvTreeItem>, isExpand: Boolean) :List<RvTreeItem>{
        list.forEach {
            if (it is RvTreeGroup && it.expandEnable() && it.getTreeItems().isNotEmpty()) {
                it.onExpandChanged(isExpand)
                setChildrenExpandChanged(it.getTreeItems(), isExpand)
            }
        }
        return list
    }

    /** 展开所有子项 */
    fun expandAll() {
        val data = getData()?.toMutableList() ?: return
        val rootList = ArrayList<RvTreeItem>()
        data.forEach {
            if (it.isRootItem()){//得到根节点数据
                rootList.add(it)
            }
        }
        val list = ArrayList<RvTreeItem>()
        rootList.forEach {//按顺序逐个添加子项数据
            list.addAll(getAllChildren(it))
        }
        setData(list)
        invokeTreeChangedListener(list)
        notifyDataSetChanged()
    }

    /** 获取所有的子项数据列表 */
    private fun getAllChildren(item: RvTreeItem): List<RvTreeItem> {
        val list = ArrayList<RvTreeItem>()
        list.add(item)
        if (item is RvTreeGroup && item.expandEnable() && item.getTreeItems().isNotEmpty()) {
            item.onExpandChanged(true)
            item.getTreeItems().forEach {
                list.addAll(getAllChildren(it))
            }
        }
        return list
    }
}