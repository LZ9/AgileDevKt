package com.lodz.android.agiledevkt.modules.badge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle

import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityBadgeTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseRefreshActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import kotlin.random.Random

/**
 * 角标测试类
 * @author zhouL
 * @date 2022/10/17
 */
class BadgeTestActivity : BaseRefreshActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BadgeTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityBadgeTestBinding by bindingLayout(ActivityBadgeTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private lateinit var mTvBadgeDrawable: BadgeDrawable

    private lateinit var mImgBadgeDrawable: BadgeDrawable

    private lateinit var mBtnBadgeDrawable: BadgeDrawable

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initTabLayout()
        initBadgeText()
        initBadgeImg()
        initBadgeButton()
    }

    /** Badge 数字 & 红点 */
    private fun initTabLayout() {
        // 数字
        mBinding.tabLayout.getTabAt(1)?.let { tab ->
            tab.orCreateBadge.apply {
                backgroundColor = Color.RED
                maxCharacterCount = 3
                number = 99999
                badgeTextColor = Color.WHITE
            }
        }

        // 红点
        mBinding.tabLayout.getTabAt(2)?.let { tab ->
            tab.orCreateBadge.backgroundColor = ContextCompat.getColor(this, R.color.red)
        }

        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.badge?.isVisible = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initBadgeText() {
        // 在视图树变化
        mBinding.badgeTv.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mTvBadgeDrawable = BadgeDrawable.create(getContext()).apply {
                    // 设置基于目标view的位置
                    badgeGravity = BadgeDrawable.TOP_END
                    number = 1024
                    maxCharacterCount = 3
                    backgroundColor = Color.RED
                    isVisible = true
                    horizontalOffset = -10
                }
                BadgeUtils.attachBadgeDrawable(mTvBadgeDrawable, mBinding.badgeTv)
                mBinding.badgeTv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initBadgeImg() {
        // 在视图树变化
        mBinding.badgeImg.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mImgBadgeDrawable = BadgeDrawable.create(getContext()).apply {
                    // 设置基于目标view的位置
                    badgeGravity = BadgeDrawable.TOP_START
                    backgroundColor = Color.RED
                    verticalOffset = 5
                    horizontalOffset = 5
                }
                BadgeUtils.attachBadgeDrawable(
                    mImgBadgeDrawable,
                    mBinding.badgeImg,
                    mBinding.imgLayout
                )
                mBinding.badgeImg.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun initBadgeButton() {
        // 在视图树变化
        mBinding.badgeBtn.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBtnBadgeDrawable = BadgeDrawable.create(getContext()).apply {
                    // 设置基于目标view的位置
                    badgeGravity = BadgeDrawable.TOP_END
                    backgroundColor = Color.RED
                    number = 12
                    maxCharacterCount = 3
                    verticalOffset = 15
                    horizontalOffset = 10
                }
                BadgeUtils.attachBadgeDrawable(mBtnBadgeDrawable, mBinding.badgeBtn)
                mBinding.badgeBtn.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onDataRefresh() {
        for (i in 0 until mBinding.tabLayout.tabCount) {
            mBinding.tabLayout.getTabAt(i)?.badge?.isVisible = true
        }
        mTvBadgeDrawable.isVisible = true
        mImgBadgeDrawable.isVisible = true
        mBtnBadgeDrawable.number = Random.nextInt(150) + 1
        setSwipeRefreshFinish()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.badgeTv.setOnClickListener {
            if (mTvBadgeDrawable.isVisible) {
                toastShort(mTvBadgeDrawable.number.toString())
            }
            mTvBadgeDrawable.isVisible = false
        }

        mBinding.badgeImg.setOnClickListener {
            mImgBadgeDrawable.isVisible = false
        }

        mBinding.badgeBtn.setOnClickListener {
            contentResolver.call(
                Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                "change_badge",
                null,
                bundleOf(
                    "package" to getContext().packageName,
                    "class" to "com.lodz.android.agiledevkt.modules.splash.SplashActivity",
                    "badgenumber" to mBtnBadgeDrawable.number
                )
            )
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}