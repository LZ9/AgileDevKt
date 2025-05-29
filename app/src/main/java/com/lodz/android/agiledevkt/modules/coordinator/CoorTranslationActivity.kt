package com.lodz.android.agiledevkt.modules.coordinator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCoorTranslationBinding
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getScreenWidth
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.imageloaderkt.glide.anko.loadResId
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.contract.OnAppBarStateChangeListener
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setupData
import java.util.*

/**
 * 带CoordinatorLayout的位移测试类
 * Created by zhouL on 2018/9/11.
 */
class CoorTranslationActivity : AbsActivity() {

    companion object {
        /** 按钮右侧间距（单位dp） */
        private const val BTN_MARGIN_END_DP = 10f

        fun start(context: Context) {
            val intent = Intent(context, CoorTranslationActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCoorTranslationBinding by bindingLayout(ActivityCoorTranslationBinding::inflate)

    /** 标题栏中心Y坐标 */
    private var mToolbarCenterY: Float = 0f
    /** 头像中心Y坐标 */
    private var mHeadImgCenterY: Float = 0f
    /** 用户名中心Y坐标 */
    private var mUserNameCenterY: Float = 0f
    /** 订阅按钮中心Y坐标 */
    private var mSubscribeBtnCenterY: Float = 0f
    /** 订阅按钮中心X坐标 */
    private var mSubscribeBtnCenterX: Float = 0f

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.recyclerView
            .linear()
            .setupData<String>(R.layout.rv_item_coordinator) { context, holder, position ->
                val data = getItem(position)
                holder.withView<TextView>(R.id.data_tv).text = data

            }
            .setData(getListData())
    }

    override fun setListeners() {
        super.setListeners()

        // 返回按钮
        mBinding.backBtn.setOnClickListener {
            finish()
        }

        // 订阅按钮
        mBinding.subscribeBtn.setOnClickListener {
            toastShort(R.string.coordinator_subscribe_tips)
        }

        mBinding.appBarLayout.addOnOffsetChangedListener(object : OnAppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: Int, delta: Double) {
                if (mToolbarCenterY == 0f){
                    return
                }

                // 图片缩放和位移
                mBinding.headImg.scaleX = delta.toFloat()
                mBinding.headImg.scaleY = delta.toFloat()
                mBinding.headImg.translationY = (mHeadImgCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 用户名位移
                mBinding.userNameTv.translationY = (mUserNameCenterY - mToolbarCenterY) * (delta.toFloat() - 1)

                // 订阅按钮位移
                mBinding.subscribeBtn.translationY = (mSubscribeBtnCenterY - mToolbarCenterY) * (delta.toFloat() - 1)
                mBinding.subscribeBtn.translationX = (getScreenWidth() - mBinding.subscribeBtn.width / 2 - dp2px(BTN_MARGIN_END_DP) - mSubscribeBtnCenterX) * (1 - delta.toFloat())
            }
        })
    }

    override fun initData() {
        super.initData()
        showHeadImg(mBinding.headImg)
        mBinding.userNameTv.setText(R.string.coordinator_user_name)
    }

    private fun getListData(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..50) {
            list.add(i.toString())
        }
        return list
    }

    /** 显示头像 */
    private fun showHeadImg(imageView: ImageView) {
        imageView.loadResId(R.drawable.bg_pokemon){
            useCircle()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            mToolbarCenterY = mBinding.toolbar.height / 2.0f
            mHeadImgCenterY = mBinding.headImg.top + mBinding.headImg.height / 2.0f
            mUserNameCenterY = mBinding.userNameTv.top + mBinding.userNameTv.height / 2.0f
            mSubscribeBtnCenterY = mBinding.subscribeBtn.top + mBinding.subscribeBtn.height / 2.0f
            mSubscribeBtnCenterX = mBinding.subscribeBtn.left + mBinding.subscribeBtn.width / 2.0f
        }
    }
}