package com.lodz.android.agiledevkt.modules.statusbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity

/**
 * 带ImageView的状态栏颜色测试
 * Created by zhouL on 2018/8/31.
 */
class StatusBarImgTestActivity :AbsActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, StatusBarImgTestActivity::class.java)
            context.startActivity(intent)
        }
    }


    /** 标题布局 */
    @BindView(R.id.title_layout)
    lateinit var mTitleLayout: ViewGroup
    /** 返回按钮 */
    @BindView(R.id.back_btn)
    lateinit var mBackBtn: ImageView
    /** 描述按钮 */
    @BindView(R.id.desc_btn)
    lateinit var mDescBtn: ImageView
    /** 状态栏设置类型单选组 */
    @BindView(R.id.type_rg)
    lateinit var mTypeRadioGroup: RadioGroup

    override fun getAbsLayoutId() = R.layout.activity_statusbar_img_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
    }
}