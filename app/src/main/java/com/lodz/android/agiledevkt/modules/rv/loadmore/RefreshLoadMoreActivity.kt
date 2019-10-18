package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.widget.rv.recycler.RecyclerLoadMoreHelper
import com.trello.rxlifecycle3.android.ActivityEvent

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

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 加载失败开关 */
    private val mLoadFailSwitch by bindView<Switch>(R.id.load_fail_switch)
    /** 布局按钮 */
    private val mLayoutManagerBtn by bindView<TextView>(R.id.layout_manager_btn)

    /** 适配器 */
    private lateinit var mAdapter: LoadMoreRvAdapter
    /** 加载更多帮助类 */
    private lateinit var mLoadMoreHelper: RecyclerLoadMoreHelper<String>
    /** 数据列表 */
    private var mList: MutableList<String> = ArrayList()
    /** 是否加载失败 */
    private var isLoadFail: Boolean = false

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR

    override fun getLayoutId(): Int = R.layout.activity_refresh_load_more

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = LoadMoreRvAdapter(getContext())
        mRecyclerView.layoutManager = getLayoutManager()
        mAdapter.onAttachedToRecyclerView(mRecyclerView)// 如果使用网格布局请设置此方法
        mAdapter.setLayoutManagerType(mLayoutManagerType)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
        mLoadMoreHelper = RecyclerLoadMoreHelper(mAdapter)
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
                        mLoadMoreHelper.config(mList, MAX_SIZE, PAGE_SIZE, true, LOAD_MORE_INDEX)
                        showStatusCompleted()
                    }

                    override fun onBaseError(e: Throwable) {
                        setSwipeRefreshFinish()
                    }
                })
    }

    override fun setListeners() {
        super.setListeners()
        mLoadMoreHelper.setListener(object : RecyclerLoadMoreHelper.Listener {
            override fun onLoadMore(currentPage: Int, nextPage: Int, size: Int, position: Int) {
                DataModule.get().requestData(nextPage)
                        .compose(RxUtils.ioToMainObservable())
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(object : BaseObserver<List<String>>() {
                            override fun onBaseNext(any: List<String>) {
                                if (isLoadFail) {
                                    mLoadMoreHelper.loadMoreFail()
                                    return
                                }
                                mList.addAll(any)
                                mLoadMoreHelper.loadMoreSuccess(mList)
                            }

                            override fun onBaseError(e: Throwable) {
                            }
                        })
            }

            override fun onClickLoadFail(reloadPage: Int, size: Int) {
                DataModule.get().requestData(reloadPage)
                        .compose(RxUtils.ioToMainObservable())
                        .compose(bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(object : BaseObserver<List<String>>() {
                            override fun onBaseNext(any: List<String>) {
                                if (isLoadFail) {
                                    mLoadMoreHelper.loadMoreFail()
                                    return
                                }
                                mList.addAll(any)
                                mLoadMoreHelper.loadMoreSuccess(mList)
                            }

                            override fun onBaseError(e: Throwable) {
                            }
                        })
            }
        })

        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item)
        }

        mAdapter.setOnItemLongClickListener { viewHolder, item, position ->
            toastShort(R.string.rvrefresh_long_click_tips)
        }

        mAdapter.setOnClickDeleteListener { position ->
            mLoadMoreHelper.hideItem(position)
        }

        mAdapter.setOnAllItemHideListener {
            showStatusNoData()
        }

        mLoadFailSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            isLoadFail = isChecked
        }

        mLayoutManagerBtn.setOnClickListener { view ->
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
                        mLoadMoreHelper.config(mList, MAX_SIZE, PAGE_SIZE, true, LOAD_MORE_INDEX)
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
            mRecyclerView.layoutManager = getLayoutManager()
            mAdapter.setLayoutManagerType(mLayoutManagerType)
            mAdapter.onAttachedToRecyclerView(mRecyclerView)
            mAdapter.notifyDataSetChanged()
            popup.dismiss()
        }
    }

}