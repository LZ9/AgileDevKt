package com.lodz.android.agiledevkt.modules.toast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityToastTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.core.Observable

/**
 * Toast测试类
 * Created by zhouL on 2018/7/16.
 */
class ToastTestActivity : BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, ToastTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** Toast对象 */
    private var mToast: Toast? = null

    private val mBinding: ActivityToastTestBinding by bindingLayout(ActivityToastTestBinding::inflate)

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

        // 短时间
        mBinding.shortDurationBtn.setOnClickListener {
            toastShort(R.string.toast_short_time)
        }

        // 长时间
        mBinding.longDurationBtn.setOnClickListener {
            toastLong(R.string.toast_long_time)
        }

        // 自定义位置（中间）
        mBinding.customPositionBtn.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_gravity_center).setShort().setGravity(Gravity.CENTER).show()
        }

        // 带顶部图片
        mBinding.topImgBtn.setOnClickListener {
            ToastUtils.create(getContext()).setText(R.string.toast_top_drawable).setShort().setTopImg(R.drawable.ic_launcher).show()
        }

        // 完全自定义
        mBinding.customViewBtn.setOnClickListener {
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
        mBinding.ioThreadBtn.setOnClickListener {
            Observable.just("")
                    .map {str ->
                        val threadName = Thread.currentThread().name
                        toastShort(threadName)
                        str
                    }
                    .compose(RxUtils.ioToMainObservable())
                    .subscribe(BaseObserver.empty())
        }

        // 可覆盖前次（android8.0及以上不能连续覆盖弹出）
        mBinding.replaceForwardBtn.setOnClickListener {
            mToast?.setText(DateUtils.getCurrentFormatString(DateUtils.TYPE_10))
            mToast?.show()
        }
    }

    override fun initData() {
        super.initData()
        mToast = ToastUtils.create(getContext()).setText(R.string.toast_replace_forward).setShort().getToast()
        showStatusCompleted()
    }

}