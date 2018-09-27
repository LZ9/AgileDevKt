package com.lodz.android.agiledevkt.modules.toast

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
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.corekt.utils.toastLong
import com.lodz.android.corekt.utils.toastShort
import io.reactivex.Observable

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
    @BindView(R.id.short_duration_btn)
    lateinit var mShortDurationBtn: Button
    /** 长时间 */
    @BindView(R.id.long_duration_btn)
    lateinit var mLongDurationBtn: Button
    /** 自定义位置（中间） */
    @BindView(R.id.custom_position_btn)
    lateinit var mCustomPositionBtn: Button
    /** 带顶部图片 */
    @BindView(R.id.top_img_btn)
    lateinit var mTopImgBtn: Button
    /** 完全自定义 */
    @BindView(R.id.custom_view_btn)
    lateinit var mCustomViewBtn: Button
    /** 异步线程 */
    @BindView(R.id.io_thread_btn)
    lateinit var mIoThreadBtn: Button

    override fun getLayoutId() = R.layout.activity_toast_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 短时间
        mShortDurationBtn.setOnClickListener {
            toastShort(R.string.toast_short_time)
        }

        // 长时间
        mLongDurationBtn.setOnClickListener {
            toastLong(R.string.toast_long_time)
        }

        // 自定义位置（中间）
        mCustomPositionBtn.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_gravity_center).setShort().setGravity(Gravity.CENTER).show()
        }

        // 带顶部图片
        mTopImgBtn.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_top_drawable).setShort().setTopImg(R.drawable.ic_launcher).show()
        }

        // 完全自定义
        mCustomViewBtn.setOnClickListener {
            val layout = layoutInflater.inflate(R.layout.toast_custom, null)
            val title = layout.findViewById<TextView>(R.id.title)
            title.setText(R.string.toast_custom)

            val img = layout.findViewById<ImageView>(R.id.img)
            img.setImageResource(R.drawable.ic_collect)

            val content = layout.findViewById<TextView>(R.id.content)
            content.setText(R.string.toast_custom_content)

            ToastUtils.create(getContext()).setView(layout).setShort().setGravity(Gravity.TOP, 100, 500).show()
        }

        // 异步线程
        mIoThreadBtn.setOnClickListener {
            Observable.just("")
                    .map {str ->
                        val threadName = Thread.currentThread().name
                        toastShort(threadName)
                        str
                    }
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

}