package com.lodz.android.pandora.widget.rv.recycler

/**
 * RecyclerView加载更多帮助类
 * Created by zhouL on 2018/11/21.
 */
class RecyclerLoadMoreHelper<T>(val mAdapter: BaseLoadMoreRVAdapter<T>) {

    /** 监听器 */
    private var mListener: Listener? = null

    /** 配置加载更多适配器（请在获得数据后调用该方法配置），数据[list]，总条数[sumSize]，每页条数[size]，是否显示底部提示界面[isShowBottomLayout]，预加载偏移量，滑动到倒数第[index]个item时就回调加载接口（默认值为0） */
    fun config(list: MutableList<T>, sumSize: Int, size: Int, isShowBottomLayout: Boolean, index: Int = 0) {
        if (mAdapter.isOpenItemAnim()) {
            mAdapter.resetItemAnimPosition()
        }
        mAdapter.setLoadMoreParam(sumSize, size, isShowBottomLayout)
        mAdapter.setLoadIndex(index)
        mAdapter.setIsLoadMore(true)
        mAdapter.setIsShowLoadFail(false)
        mAdapter.setData(list)
        mAdapter.notifyDataSetChanged()
    }

    /** 加载更多数据获取成功，传入总数据[list] */
    fun loadMoreSuccess(list: MutableList<T>) {
        mAdapter.setIsLoadMore(true)
        mAdapter.setData(list)
        mAdapter.notifyDataSetChanged()
    }

    /** 手动设置加载完成 */
    fun loadComplete() {
        mAdapter.setIsLoadMore(true)
        mAdapter.setLoadCompleted()
        mAdapter.notifyDataSetChanged()
    }

    /** 加载更多数据获取失败 */
    fun loadMoreFail() {
        mAdapter.setIsLoadMore(true)
        mAdapter.setIsShowLoadFail(true)
        mAdapter.notifyDataSetChanged()
    }

    /** 隐藏某个位置[position]的Item */
    fun hideItem(position: Int) {
        mAdapter.hideItem(position)
        mAdapter.notifyDataSetChanged()
    }

    /** 设置监听器[listener] */
    fun setListener(listener: Listener) {
        mListener = listener
        mAdapter.setOnLoadMoreListener { currentPage, nextPage, size, position ->
            mAdapter.setIsLoadMore(false)
            if (mListener != null) {
                mListener!!.onLoadMore(currentPage, nextPage, size, position)
            }
        }

        mAdapter.setOnLoadFailClickListener { reloadPage, size ->
            mAdapter.setIsShowLoadFail(false)
            mAdapter.setIsLoadMore(false)
            mAdapter.notifyDataSetChanged()
            if (mListener != null) {
                mListener!!.onClickLoadFail(reloadPage, size)
            }
        }
    }

    interface Listener {

        /** 加载更多，当前页码[currentPage]，需要加载的页码[nextPage]，每页大小[size]，回调位置[position] */
        fun onLoadMore(currentPage: Int, nextPage: Int, size: Int, position: Int)

        /** 点击加载失败，需要重载的页码[reloadPage]，每页大小[size] */
        fun onClickLoadFail(reloadPage: Int, size: Int)
    }
}