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

    override fun getItemViewType(position: Int): Int = getItem(position)?.fetchCls().hashCode()

    override fun setItemClick(holder: DataVBViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val item = getItem(position) ?: return@setOnClickListener
            if (item is RvTreeGroup && item.expandEnable() && item.fetchTreeItems().isNotEmpty()){
                val list = getData()?.toArrayList() ?: return@setOnClickListener
                val isExpand = item.isExpandItem()
                if (isExpand) { //收回已经展开的子项（包含子项内的子项）
                    list.removeAll(collapsedItemsChildren(item).toSet())
                } else {//展开子项
                    list.addAll(position + 1, item.fetchTreeItems())
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
        val list = item.fetchTreeItems().toMutableList()
        list.forEach {
            if (it is RvTreeGroup && it.isExpandItem() && it.fetchTreeItems().isNotEmpty()) {
                it.onExpandChanged(false)
                allData.addAll(collapsedItemsChildren(it))
            }
        }
        allData.addAll(list)
        return allData
    }

    /** 设置树结构数据[data]，会根据isExpandItem()和expandEnable()自动组装数据 */
    fun setTreeData(data: MutableList<T>?) {
        val list = ArrayList<RvTreeItem>()
        if (data == null) {
            super.setData(list)
            return
        }
        data.forEach {
            list.add(it)
        }
        setTreeDataObj(list)
    }

    /** 设置树结构数据（使用接口对象RvTreeItem） */
    fun setTreeDataObj(data: List<RvTreeItem>) {
        val results = assembleTreeData(data)
        super.setData(results)
        invokeTreeChangedListener(results)
        notifyDataSetChanged()
    }

    /** 组装树结构数据 */
    private fun assembleTreeData(list: List<RvTreeItem>): ArrayList<RvTreeItem> {
        val rootList = ArrayList<RvTreeItem>()
        list.forEach {
            if (it.isRootItem()){//得到根节点数据
                rootList.add(it)
            }
        }
        val data = ArrayList<RvTreeItem>()
        rootList.forEach { //按顺序和规则逐个添加子项数据
            data.addAll(getItemChildren(it))
        }
        return data
    }

    /** 获取所有的子项数据列表 */
    private fun getItemChildren(item: RvTreeItem): List<RvTreeItem> {
        val list = ArrayList<RvTreeItem>()
        list.add(item)
        if (item is RvTreeGroup && item.expandEnable() && item.fetchTreeItems().isNotEmpty() && item.isExpandItem()) {
            item.fetchTreeItems().forEach {
                list.addAll(getItemChildren(it))
            }
        }
        return list
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
        setTreeDataObj(setChildrenExpandChanged(data, false))
    }

    /** 展开所有子项 */
    fun expandAll() {
        val data = getData()?.toMutableList() ?: return
        setTreeDataObj(setChildrenExpandChanged(data, true))
    }

    /** 设置所有子项的折叠标志位 */
    private fun setChildrenExpandChanged(list: List<RvTreeItem>, isExpand: Boolean) :List<RvTreeItem>{
        list.forEach {
            if (it is RvTreeGroup && it.expandEnable() && it.fetchTreeItems().isNotEmpty()) {
                it.onExpandChanged(isExpand)
                setChildrenExpandChanged(it.fetchTreeItems(), isExpand)
            }
        }
        return list
    }

}