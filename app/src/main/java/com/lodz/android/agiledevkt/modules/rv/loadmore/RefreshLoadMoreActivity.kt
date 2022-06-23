package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityRefreshLoadMoreBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.*
import com.trello.rxlifecycle4.android.ActivityEvent

/**
 * RV刷新/加载更多测试
 * Created by zhouL on 2018/11/23.
 */
class RefreshLoadMoreActivity : BaseRefreshActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RefreshLoadMoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 每页数量 */
    private val PAGE_SIZE = 20
    /** 最大数量 */
    private val MAX_SIZE = 120
    /** 预加载偏移量 */
    private val LOAD_MORE_INDEX = 1

    private val mBinding: ActivityRefreshLoadMoreBinding by bindingLayout(ActivityRefreshLoadMoreBinding::inflate)

    /** 适配器 */
    private lateinit var mAdapter: LoadMoreRvAdapter
    /** 数据列表 */
    private var mList: MutableList<String> = ArrayList()
    /** 是否加载失败 */
    private var isLoadFail: Boolean = false

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView
            .layoutM(getLayoutManager())
            .loadMore(LoadMoreRvAdapter(getContext()))
            .apply {
                setLayoutManagerType(mLayoutManagerType)
            }.loadMoreListener(
                onLoadMore = {currentPage, nextPage, size, position ->
                    DataModule.get().requestData(nextPage)
                        .compose(RxUtils.ioToMainObservable())
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(BaseObserver.action({
                            if (isLoadFail) {
                                mAdapter.loadMoreFail()
                                return@action
                            }
                            mList.addAll(it)
                            mAdapter.loadMoreSuccess(mList)
                        }))
                },
                onClickLoadFail = {reloadPage, size ->
                    DataModule.get().requestData(reloadPage)
                        .compose(RxUtils.ioToMainObservable())
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(BaseObserver.action({
                            if (isLoadFail) {
                                mAdapter.loadMoreFail()
                                return@action
                            }
                            mList.addAll(it)
                            mAdapter.loadMoreSuccess(mList)
                        }))
                }
            )
    }

    /** 获取RV布局 */
    private fun getLayoutManager(): RecyclerView.LayoutManager {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            val layoutManager = GridLayoutManager(getContext(), 3)
            layoutManager.orientation = RecyclerView.VERTICAL
            return layoutManager
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            return StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        }
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        return layoutManager
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onDataRefresh() {
        DataModule.get().requestData(1)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(object : BaseObserver<List<String>>() {
                override fun onBaseNext(any: List<String>) {
                    setSwipeRefreshFinish()
                    if (isLoadFail) {
                        toastShort(R.string.rvrefresh_refresh_fail)
                        return
                    }
                    mList.clear()
                    mList.addAll(any)
                    mAdapter.loadMoreStart(mList, MAX_SIZE, PAGE_SIZE, true, LOAD_MORE_INDEX)
                    showStatusCompleted()
                }

                override fun onBaseError(e: Throwable) {
                    setSwipeRefreshFinish()
                }
            })
    }

    override fun setListeners() {
        super.setListeners()

        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item)
        }

        mAdapter.setOnItemLongClickListener { viewHolder, item, position ->
            toastShort(R.string.rvrefresh_long_click_tips)
        }

        mAdapter.setOnClickDeleteListener { position ->
            mAdapter.hideItemNotify(position)
        }

        mAdapter.setOnAllItemHideListener {
            showStatusNoData()
        }

        // 加载失败开关
        mBinding.loadFailSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            isLoadFail = isChecked
        }

        // 布局按钮
        mBinding.layoutManagerBtn.setOnClickListener { view ->
            showLayoutManagerPopupWindow(view)
        }
    }

    override fun initData() {
        super.initData()
        requestFirstData()
    }

    private fun requestFirstData() {
        DataModule.get().requestData(1)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(object : BaseObserver<List<String>>() {
                override fun onBaseNext(any: List<String>) {
                    mList.clear()
                    mList.addAll(any)
                    mAdapter.loadMoreStart(mList, MAX_SIZE, PAGE_SIZE, true, LOAD_MORE_INDEX)
                    showStatusCompleted()
                }

                override fun onBaseError(e: Throwable) {

                }
            })
    }

    /** 显示布局的PopupWindow */
    private fun showLayoutManagerPopupWindow(view: View) {
        val popupWindow = LayoutManagerPopupWindow(getContext())
        popupWindow.create()
        popupWindow.setLayoutManagerType(mLayoutManagerType)
        popupWindow.getPopup().showAsDropDown(view, -45, 20)
        popupWindow.setOnClickListener { popup, type ->
            mLayoutManagerType = type
            mBinding.recyclerView.layoutManager = getLayoutManager()
            mAdapter.setLayoutManagerType(mLayoutManagerType)
            mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)
            mAdapter.notifyDataSetChanged()
            popup.dismiss()
        }
    }

}