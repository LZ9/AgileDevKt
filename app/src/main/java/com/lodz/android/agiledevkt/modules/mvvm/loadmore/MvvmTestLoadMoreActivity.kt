package com.lodz.android.agiledevkt.modules.mvvm.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.PageBean
import com.lodz.android.agiledevkt.databinding.ActivityMvvmLoadmoreBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.mvvm.base.activity.BaseRefreshVmActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.recycler.RecyclerLoadMoreHelper

/**
 * MVVM加载更多测试类
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestLoadMoreActivity : BaseRefreshVmActivity<MvvmTestLoadMoreViewModel>() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestLoadMoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 适配器 */
    private lateinit var mAdapter: LoadMoreListAdapter
    /** 加载更多帮助类 */
    private lateinit var mLoadMoreHelper: RecyclerLoadMoreHelper<String>

    override fun createViewModel(): Class<MvvmTestLoadMoreViewModel> = MvvmTestLoadMoreViewModel::class.java

    private val mBinding: ActivityMvvmLoadmoreBinding by bindingLayout(ActivityMvvmLoadmoreBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_loadmore_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = LoadMoreListAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mBinding.recyclerView.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)// 如果使用网格布局请设置此方法
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
        mLoadMoreHelper = RecyclerLoadMoreHelper(mAdapter)
    }

    override fun onDataRefresh() {
        getViewModel().requestDataList(PageBean.DEFAULT_START_PAGE_NUM)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mLoadMoreHelper.setListener(object : RecyclerLoadMoreHelper.Listener {
            override fun onLoadMore(currentPage: Int, nextPage: Int, size: Int, position: Int) {
                getViewModel().requestDataList(nextPage)
            }

            override fun onClickLoadFail(reloadPage: Int, size: Int) {
                getViewModel().requestDataList(reloadPage)

            }
        })

        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            getViewModel().requestDetail(getContext(), item)
        }
    }

    override fun MvvmTestLoadMoreViewModel.setViewModelObserves() {
        mDataList.observe(getLifecycleOwner()) {
            val isFirst = it.first
            val pageBean = it.second
            val list = pageBean.data
            if (isFirst){
                setSwipeRefreshFinish()
                if (list == null){
                    showStatusNoData()
                    return@observe
                }
                mLoadMoreHelper.config(list, pageBean.total, pageBean.pageSize, true, 1)
                showStatusCompleted()
                return@observe
            }
            if (list == null){
                mLoadMoreHelper.loadComplete()
                return@observe
            }
            val datas = mAdapter.getData()?.toMutableList() ?: ArrayList()
            datas.addAll(list)
            mLoadMoreHelper.loadMoreSuccess(datas)
        }

        mDetailInfo.observe(getLifecycleOwner()){
            toastShort(it)
        }
    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        getViewModel().requestDataList(PageBean.DEFAULT_START_PAGE_NUM)
    }
}