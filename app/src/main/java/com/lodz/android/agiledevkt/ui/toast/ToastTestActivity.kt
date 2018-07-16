package com.lodz.android.agiledevkt.ui.toast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.ui.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.corekt.utils.toastLong
import com.lodz.android.corekt.utils.toastShort

/**
 * Toast测试类
 * Created by zhouL on 2018/7/16.
 */
class ToastTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, ToastTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 短时间 */
    @BindView(R.id.short_duration)
    lateinit var mShortDuration: Button
    /** 长时间 */
    @BindView(R.id.long_duration)
    lateinit var mLongDuration: Button
    /** 自定义位置（中间） */
    @BindView(R.id.custom_position)
    lateinit var mCustomPosition: Button
    /** 带顶部图片 */
    @BindView(R.id.top_img)
    lateinit var mTopImg: Button
    /** 完全自定义 */
    @BindView(R.id.custom_view)
    lateinit var mCustomView: Button

    override fun getLayoutId() = R.layout.activity_toast_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 短时间
        mShortDuration.setOnClickListener {
            toastShort(R.string.toast_short_time)
        }

        // 长时间
        mLongDuration.setOnClickListener {
            toastLong(R.string.toast_long_time)
        }

        // 自定义位置（中间）
        mCustomPosition.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_gravity_center).setShort().setGravity(Gravity.CENTER).show()
        }

        // 带顶部图片
        mTopImg.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_top_drawable).setShort().setTopImg(R.drawable.ic_launcher).show()
        }

        // 完全自定义
        mCustomView.setOnClickListener {
            val layout = layoutInflater.inflate(R.layout.toast_custom, null)
            val title = layout.findViewById<TextView>(R.id.title)
            title.setText(R.string.toast_custom)

            val img = layout.findViewById<ImageView>(R.id.img)
            img.setImageResource(R.drawable.ic_collect)

            val content = layout.findViewById<TextView>(R.id.content)
            content.setText(R.string.toast_custom_content)

            ToastUtils.create(getContext()).setView(layout).setShort().setGravity(Gravity.TOP, 100, 500).show()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}