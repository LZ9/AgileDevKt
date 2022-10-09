package com.lodz.android.agiledevkt.modules.rv.swipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.pandora.base.activity.BaseActivity

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySwipeRvBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.drag.DragSpeedCallback
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.security.MD5
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.drag.RecyclerViewDragHelper
import com.lodz.android.pandora.widget.rv.swipe.vb.SwipeVbViewHolder

/**
 * RV侧滑菜单测试
 * @author zhouL
 * @date 2022/10/8
 */
class SwipeRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SwipeRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivitySwipeRvBinding by bindingLayout(ActivitySwipeRvBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    /** 适配器 */
    private lateinit var mAdapter: SwipeTestVbAdapter
    /** 拖拽帮助类 */
    private lateinit var mRecyclerViewDragHelper: RecyclerViewDragHelper<String, SwipeVbViewHolder>

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.swipeMenuRv
            .apply {
                linear()
                addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            }
            .setup(SwipeTestVbAdapter(getContext()))
        mRecyclerViewDragHelper = RecyclerViewDragHelper(getContext())
        mRecyclerViewDragHelper.setUseDrag(true)// 设置是否允许拖拽
            .setLongPressDragEnabled(true)// 是否启用长按拖拽效果
            .setSwipeEnabled(false)// 设置是否允许滑动
            .setVibrateEnabled(true)// 启用震动效果
        val callback = DragSpeedCallback<String, SwipeVbViewHolder>()
        callback.isLimit = true
        mRecyclerViewDragHelper.build(mBinding.swipeMenuRv, mAdapter, callback)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.likeBtn.setOnClickListener { toastShort(R.string.rv_swipe_like) }

        mBinding.shareBtn.setOnClickListener { toastShort(R.string.rv_swipe_share) }

        mBinding.dislikeBtn.setOnClickListener { toastShort(R.string.rv_swipe_dislike) }

        mBinding.followBtn.setOnClickListener { toastShort(R.string.rv_swipe_follow) }

        mBinding.rewardBtn.setOnClickListener { toastShort(R.string.rv_swipe_reward) }

        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(position.toString().append(".").append(item))
        }
    }

    override fun initData() {
        super.initData()
        val list = initTestDatas()
        mAdapter.setData(list)
        mRecyclerViewDragHelper.setList(list)
        showStatusCompleted()
    }

    private fun initTestDatas(): ArrayList<String> {
        val list = ArrayList<String>()
        val time = MD5.md(System.currentTimeMillis().toString()) ?: ""
        for (char in time) {
            list.add(MD5.md(char.toString()) ?: "")
        }
        return list
    }
}