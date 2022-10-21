package com.lodz.android.agiledevkt.modules.adsorb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lodz.android.pandora.base.activity.BaseActivity

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityAdsorbViewBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getNavigationBarHeight
import com.lodz.android.corekt.anko.getStatusBarHeight
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.adsorb.AdsorbView

/**
 * 吸边控件展示类
 * @author zhouL
 * @date 2022/10/18
 */
class AdsorbViewActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, AdsorbViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityAdsorbViewBinding by bindingLayout(ActivityAdsorbViewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private var mImgAv: ImageAdsorbView? = null

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.imgAv.setOnClickListener {
            toastShort("点击了")
        }

        mBinding.addBtn.setOnClickListener {
            if (mImgAv != null){
                return@setOnClickListener
            }
            mImgAv = createImageAdsorbView()
            getWindowView().addView(mImgAv)
        }

        mBinding.hideBtn.setOnClickListener {
            if (mImgAv == null){
                return@setOnClickListener
            }
            getWindowView().removeView(mImgAv)
            mImgAv = null
        }

        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.horizontal_rb -> mImgAv?.setAdsorbType(AdsorbView.HORIZONTAL)
                R.id.vertical_rb -> mImgAv?.setAdsorbType(AdsorbView.VERTICAL)
            }
        }
    }

    /** 创建吸边控件 */
    private fun createImageAdsorbView(): ImageAdsorbView {
        val img = ImageAdsorbView(getContext())
        val lp = FrameLayout.LayoutParams(dp2px(80), dp2px(80))
        lp.topMargin = getStatusBarHeight() + resources.getDimension(R.dimen.pandora_title_bar_height).toInt()
        lp.bottomMargin = getNavigationBarHeight()
        lp.marginStart = dp2px(10)
        lp.marginEnd = dp2px(10)
        img.layoutParams = lp
        return img
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun getWindowView(): ViewGroup = window.decorView as ViewGroup
}