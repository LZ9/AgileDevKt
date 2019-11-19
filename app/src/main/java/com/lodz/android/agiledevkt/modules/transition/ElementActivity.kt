package com.lodz.android.agiledevkt.modules.transition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.util.Pair
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.transition.TransitionHelper
import org.jetbrains.anko.imageResource

/**
 * 共享元素详情
 * Created by zhouL on 2018/11/20.
 */
class ElementActivity : BaseActivity() {

    companion object {
        private const val EXTRA_IMG_RES_ID = "extra_img_res_id"
        private const val EXTRA_TITLE = "extra_title"

        /** 启动[activity]，详情图片资源[resId]，详情标题[title]，共享元素列表[sharedElements] */
        fun start(activity: Activity, @DrawableRes resId: Int, title: String, sharedElements: List<Pair<View, String>>) {
            val intent = Intent(activity, ElementActivity::class.java)
            intent.putExtra(EXTRA_IMG_RES_ID, resId)
            intent.putExtra(EXTRA_TITLE, title)
            TransitionHelper.jumpTransition(activity, intent, sharedElements)
        }
    }

    /** 标题 */
    private val mTitleTv by bindView<TextView>(R.id.title_name_tv)
    /** 图标 */
    private val mIconImg by bindView<ImageView>(R.id.icon_img)

    /** 标题 */
    private var mTitle = ""
    /** 图标资源id */
    @DrawableRes
    private var mImgResId = 0

    override fun startCreate() {
        super.startCreate()
        mTitle = intent.getStringExtra(EXTRA_TITLE)
        mImgResId = intent.getIntExtra(EXTRA_IMG_RES_ID, R.drawable.ic_launcher)
    }

    override fun getLayoutId(): Int = R.layout.activity_element

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(R.string.share_element_title)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        TransitionHelper.finish(this)
    }

    override fun initData() {
        super.initData()
        mTitleTv.text = mTitle
        mIconImg.imageResource = mImgResId
        TransitionHelper.setTransition(mTitleTv, TransitionActivity.TITLE)
        TransitionHelper.setTransition(mIconImg, TransitionActivity.IMG)
        TransitionHelper.setEnterTransitionDuration(this, 500)
        showStatusCompleted()
    }

    override fun onPressBack(): Boolean {
        TransitionHelper.finish(this)
        return true
    }
}
