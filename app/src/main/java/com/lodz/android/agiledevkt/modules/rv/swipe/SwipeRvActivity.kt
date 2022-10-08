package com.lodz.android.agiledevkt.modules.rv.swipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.pandora.base.activity.BaseActivity

import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySwipeRvBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.likeBtn.setOnClickListener { toastShort(R.string.rv_swipe_left_like) }

        mBinding.shareBtn.setOnClickListener { toastShort(R.string.rv_swipe_left_share) }

        mBinding.dislikeBtn.setOnClickListener { toastShort(R.string.rv_swipe_left_dislike) }

        mBinding.followBtn.setOnClickListener { toastShort(R.string.rv_swipe_right_follow) }

        mBinding.rewardBtn.setOnClickListener { toastShort(R.string.rv_swipe_right_reward) }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}