package com.lodz.android.agiledevkt.modules.mvvm.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.base.response.PageBean
import com.lodz.android.agiledevkt.databinding.ActivityMvvmLoadmoreBinding
import com.lodz.android.agiledevkt.databinding.RvItemMainBinding
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.mvvm.base.activity.BaseRefreshVmActivity
import com.lodz.android.pandora.mvvm.vm.BaseRefreshViewModel
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.utils.viewmodel.bindViewModel
import com.lodz.android.pandora.widget.rv.anko.*
import com.lodz.android.pandora.widget.rv.recycler.loadmore.vb.SimpleLoadMoreVbRvAdapter

/**
 * MVVM加载更多测试类
 * @author zhouL
 * @date 2019/12/5
 */
class MvvmTestLoadMoreActivity : BaseRefreshVmActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, MvvmTestLoadMoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mViewModel by bindViewModel { MvvmTestLoadMoreViewModel() }

    override fun getViewModel(): BaseRefreshViewModel = mViewModel

    private val mBinding: ActivityMvvmLoadmoreBinding by bindingLayout(ActivityMvvmLoadmoreBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    /** 适配器 */
    private lateinit var mAdapter: SimpleLoadMoreVbRvAdapter<String>

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.mvvm_demo_loadmore_title)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView.linear()
            .loadMoreVB<String, RvItemMainBinding>(RvItemMainBinding::inflate) { vb, holder, position ->
                val str = getItem(position)
                vb.name.text = str
            }
            .loadMoreListener(
                onLoadMore = { currentPage, nextPage, size, position ->
                    mViewModel.requestDataList(nextPage)
                },
                onClickLoadFail = { reloadPage, size ->
                    mViewModel.requestDataList(reloadPage)
                }
            )
    }

    override fun onDataRefresh() {
        mViewModel.requestDataList(PageBean.DEFAULT_START_PAGE_NUM)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            mViewModel.requestDetail(getContext(), item)
        }
    }

    override fun setViewModelObserves() {
        super.setViewModelObserves()
        mViewModel.mDataList.observe(getLifecycleOwner()) {
            val isFirst = it.first
            val pageBean = it.second
            val list = pageBean.data
            if (isFirst){
                setSwipeRefreshFinish()
                if (list == null){
                    showStatusNoData()
                    return@observe
                }
                mAdapter.loadMoreStart(list, pageBean.total, pageBean.pageSize, true, 1)
                showStatusCompleted()
                return@observe
            }
            if (list == null){
                mAdapter.loadComplete()
                return@observe
            }
            val datas = mAdapter.getData()?.toMutableList() ?: ArrayList()
            datas.addAll(list)
            mAdapter.loadMoreSuccess(datas)
        }

        mViewModel.mDetailInfo.observe(getLifecycleOwner()){
            toastShort(it)
        }

    }

    override fun initData() {
        super.initData()
        showStatusLoading()
        mViewModel.requestDataList(PageBean.DEFAULT_START_PAGE_NUM)
    }
}