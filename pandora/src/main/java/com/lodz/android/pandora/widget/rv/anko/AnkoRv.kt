package com.lodz.android.pandora.widget.rv.anko

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.widget.rv.recycler.base.AbsRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.base.BaseDataRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.base.BaseVbRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.loadmore.AbsLoadMoreRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.loadmore.data.SimpleLoadMoreDataRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.loadmore.vb.SimpleLoadMoreVbRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder
import com.lodz.android.pandora.widget.rv.recycler.vh.DataViewHolder

/**
 * RecyclerView工具类
 * @author zhouL
 * @date 2022/6/21
 */

/** 设置RV布局 */
fun RecyclerView.layoutM(
    layout: RecyclerView.LayoutManager,
    hasFixedSize: Boolean = true
): RecyclerView {
    layoutManager = layout
    setHasFixedSize(hasFixedSize)
    return this
}

/** 设置RV线性布局 */
fun RecyclerView.linear(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    hasFixedSize: Boolean = true
): RecyclerView {
    layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    setHasFixedSize(hasFixedSize)
    return this
}

/** 设置RV网格布局 */
fun RecyclerView.grid(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    hasFixedSize: Boolean = true
): RecyclerView {
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
    setHasFixedSize(hasFixedSize)
    return this
}

/** 设置RV瀑布流布局 */
fun RecyclerView.staggeredGrid(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    hasFixedSize: Boolean = true
): RecyclerView {
    layoutManager = StaggeredGridLayoutManager(spanCount, orientation).apply {
        this.reverseLayout = reverseLayout
    }
    setHasFixedSize(hasFixedSize)
    return this
}

/** 设置RV适配器[absAdapter] */
fun <T, AD : AbsRvAdapter<T, *>> RecyclerView.setup(absAdapter: AD): AD {
    absAdapter.onAttachedToRecyclerView(this)
    this.adapter = absAdapter
    return absAdapter
}

/** 设置RV的布局[layoutId]和DataViewHolder绑定逻辑[bind] */
fun <T> RecyclerView.setupData(
    @LayoutRes layoutId: Int,
    bind: BaseDataRvAdapter<T>.(context: Context, holder: DataViewHolder, position: Int) -> Unit
): BaseDataRvAdapter<T> {
    val absAdapter = object : BaseDataRvAdapter<T>(context) {
        override fun onBind(holder: DataViewHolder, position: Int) {
            bind(context, holder, position)
        }

        override fun getLayoutId(): Int = layoutId
    }
    absAdapter.onAttachedToRecyclerView(this)
    this.adapter = absAdapter
    return absAdapter
}

/** 设置RV的ViewBinding方法[inflate]和DataVBViewHolder绑定逻辑 */
fun <T, VB : ViewBinding> RecyclerView.setupVB(
    inflate: (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> VB,
    bind: BaseVbRvAdapter<T>.(context: Context, vb: VB, holder: DataVBViewHolder, position: Int) -> Unit
): BaseVbRvAdapter<T> {
    val absAdapter = object : BaseVbRvAdapter<T>(context) {
        override fun onBind(holder: DataVBViewHolder, position: Int) {
            holder.getVB<VB>().apply { bind(context, this, holder, position) }
        }

        override fun getVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding = inflate
    }
    absAdapter.onAttachedToRecyclerView(this)
    this.adapter = absAdapter
    return absAdapter

}

/** 设置RV加载更多适配器[absAdapter] */
fun <T, AD : AbsLoadMoreRvAdapter<T, *>> RecyclerView.loadMore(absAdapter: AD) = setup(absAdapter)

/** 设置RV的布局[layoutId]和DataViewHolder绑定逻辑[bind] */
fun <T> RecyclerView.loadMoreData(
    @LayoutRes layoutId: Int,
    bind: SimpleLoadMoreDataRvAdapter<T>.(context: Context, holder: DataViewHolder, position: Int) -> Unit
): SimpleLoadMoreDataRvAdapter<T> {
    val absAdapter = object : SimpleLoadMoreDataRvAdapter<T>(context){
        override fun onBind(holder: DataViewHolder, position: Int) {
            bind(context, holder, position)
        }

        override fun getItemLayoutId(): Int = layoutId
    }
    absAdapter.onAttachedToRecyclerView(this)
    this.adapter = absAdapter
    return absAdapter
}

/** 设置RV的ViewBinding方法[inflate]和DataVBViewHolder绑定逻辑 */
fun <T, VB : ViewBinding> RecyclerView.loadMoreVB(
    inflate: (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> VB,
    bind: SimpleLoadMoreVbRvAdapter<T>.(context: Context, vb: VB, holder: DataVBViewHolder, position: Int) -> Unit
): SimpleLoadMoreVbRvAdapter<T> {
    val absAdapter = object : SimpleLoadMoreVbRvAdapter<T>(context) {
        override fun onBind(holder: DataVBViewHolder, position: Int) {
            holder.getVB<VB>().apply { bind(context, this, holder, position) }
        }

        override fun getVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding = inflate
    }
    absAdapter.onAttachedToRecyclerView(this)
    this.adapter = absAdapter
    return absAdapter
}

/**
 * 设置监听器，
 * 加载更多[onLoadMore]，当前页码[currentPage]，需要加载的页码[nextPage]，每页大小[size]，回调位置[position]
 * 点击加载失败[onClickLoadFail]，需要重载的页码[reloadPage]，每页大小[size]
 */
@SuppressLint("NotifyDataSetChanged")
fun <T, AD : AbsLoadMoreRvAdapter<T, *>> AD.loadMoreListener(
    onLoadMore: (currentPage: Int, nextPage: Int, size: Int, position: Int) -> Unit,
    onClickLoadFail: (reloadPage: Int, size: Int) -> Unit
): AD {
    this.setOnLoadMoreListener { currentPage, nextPage, size, position ->
        this.setIsLoadMore(false)
        onLoadMore(currentPage, nextPage, size, position)
    }
    this.setOnLoadFailClickListener { reloadPage, size ->
        this.setIsShowLoadFail(false)
        this.setIsLoadMore(false)
        this.notifyDataSetChanged()
        onClickLoadFail(reloadPage, size)
    }
    return this
}

/** 配置加载更多请在获得数据后调用该方法配置），数据[list]，总条数[sumSize]，每页条数[size]，是否显示底部提示界面[isShowBottomLayout]，预加载偏移量，滑动到倒数第[index]个item时就回调加载接口（默认值为0） */
@SuppressLint("NotifyDataSetChanged")
fun <T> AbsLoadMoreRvAdapter<T, *>.loadMoreStart(list: MutableList<T>, sumSize: Int, size: Int, isShowBottomLayout: Boolean, index: Int = 0) {
    if (this.isOpenItemAnim()) {
        this.resetItemAnimPosition()
    }
    this.setLoadMoreParam(sumSize, size, isShowBottomLayout)
    this.setLoadIndex(index)
    this.setIsLoadMore(true)
    this.setIsShowLoadFail(false)
    this.setData(list)
    this.notifyDataSetChanged()
}

/** 加载更多数据获取成功，传入总数据[list] */
@SuppressLint("NotifyDataSetChanged")
fun <T> AbsLoadMoreRvAdapter<T, *>.loadMoreSuccess(list: MutableList<T>){
    this.setIsLoadMore(true)
    this.setData(list)
    this.notifyDataSetChanged()
}

/** 手动设置加载完成 */
@SuppressLint("NotifyDataSetChanged")
fun <T> AbsLoadMoreRvAdapter<T, *>.loadComplete(){
    this.setIsLoadMore(true)
    this.setLoadCompleted()
    this.notifyDataSetChanged()
}

/** 加载更多数据获取失败 */
@SuppressLint("NotifyDataSetChanged")
fun <T> AbsLoadMoreRvAdapter<T, *>.loadMoreFail(){
    this.setIsLoadMore(true)
    this.setIsShowLoadFail(true)
    this.notifyDataSetChanged()
}

/** 隐藏某个位置[position]的Item */
fun <T> AbsLoadMoreRvAdapter<T, *>.hideItemNotify(position: Int){
    this.hideItem(position)
    this.notifyDataSetChanged()
}