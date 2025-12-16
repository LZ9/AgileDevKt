package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.databinding.ActivityRefreshLoadMoreHeaderBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.hideItemNotify
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.loadMore
import com.lodz.android.pandora.widget.rv.anko.loadMoreFail
import com.lodz.android.pandora.widget.rv.anko.loadMoreListener
import com.lodz.android.pandora.widget.rv.anko.loadMoreStart
import com.lodz.android.pandora.widget.rv.anko.loadMoreSuccess
import com.trello.rxlifecycle4.android.ActivityEvent
import kotlin.getValue

/**
 * RV刷新/加载更多测试（头布局实现嵌套）
 * @author zhouL
 * @date 2025/12/15
 */
class RefreshLoadMoreHeaderActivity : BaseRefreshActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RefreshLoadMoreHeaderActivity::class.java)
            context.startActivity(intent)
        }
    }


    /** 每页数量 */
    private val PAGE_SIZE = 20
    /** 最大数量 */
    private val MAX_SIZE = 60

    private val mBinding: ActivityRefreshLoadMoreHeaderBinding by bindingLayout(ActivityRefreshLoadMoreHeaderBinding::inflate)

    /** 适配器 */
    private lateinit var mAdapter: LoadMoreHeadRvAdapter
    /** 数据列表 */
    private var mList: MutableList<String> = ArrayList()
    /** 是否加载失败 */
    private var isLoadFail: Boolean = false

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.rvrefresh_load_header)
        initListRecyclerView()
    }

    /** 初始化列表布局 */
    private fun initListRecyclerView() {
        val adapter = LoadMoreHeadRvAdapter(getContext())
        adapter.setHeaderData(getNationList())
        mAdapter = mBinding.recyclerView
            .linear()
            .loadMore(adapter)
            .loadMoreListener(
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
                    mAdapter.loadMoreStart(mList, MAX_SIZE, PAGE_SIZE)
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
            toastShort(item.append("  ").append(getContext().getString(R.string.rvrefresh_long_click_tips)))
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
                    mAdapter.loadMoreStart(mList, MAX_SIZE, PAGE_SIZE)
                    showStatusCompleted()
                }

                override fun onBaseError(e: Throwable) {

                }
            })
    }

    /** 获取国家数据 */
    private fun getNationList(): ArrayList<NationBean> {
        /** 国旗 */
        val imgs = arrayOf(
            Constant.CHN_FLAG_URL,
            Constant.USA_FLAG_URL,
            Constant.RUS_FLAG_URL,
            Constant.JPN_FLAG_URL,
            Constant.KOR_FLAG_URL,
            Constant.AUS_FLAG_URL,
            Constant.UKR_FLAG_URL,
            Constant.PRK_FLAG_URL,
            Constant.BRA_FLAG_URL
        )
        /** 名称 */
        val names = arrayOf("中国", "美国", "俄罗斯", "日本", "韩国", "澳大利亚", "乌克兰", "朝鲜", "巴西")
        /** 缩写 */
        val codes = arrayOf("CHN", "USA", "RUS", "JPN", "KOR", "AUS", "UKR", "PRK", "BRA")

        val list = ArrayList<NationBean>()
        for (i in imgs.indices) {
            val bean = NationBean()
            bean.imgUrl = imgs[i]
            bean.name = names[i]
            bean.code = codes[i]
            list.add(bean)
        }
        return list
    }
}