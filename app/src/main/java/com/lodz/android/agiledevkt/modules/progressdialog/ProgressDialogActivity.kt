package com.lodz.android.agiledevkt.modules.progressdialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.exception.RxException
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 加载框测试类
 * Created by zhouL on 2019/1/7.
 */
class ProgressDialogActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProgressDialogActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 无提示语 */
    private val mNoTipsBtn by bindView<MaterialButton>(R.id.no_tips_btn)
    /** 有提示语 */
    private val mTipsBtn by bindView<MaterialButton>(R.id.tips_btn)
    /** 自定义图标 */
    private val mCustomIconBtn by bindView<MaterialButton>(R.id.custom_icon_btn)
    /** 点击空白不可消失 */
    private val mCanceledOnTouchOutsideBtn by bindView<MaterialButton>(R.id.canceled_on_touch_outside_btn)
    /** 点击空白和返回不可消失 */
    private val mCancelableBtn by bindView<MaterialButton>(R.id.cancelable_btn)

    override fun getLayoutId(): Int = R.layout.activity_progress_dialog

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 无提示语
        mNoTipsBtn.setOnClickListener {
            ProgressDialogHelper.get()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .create(getContext())
                    .show()
        }

        // 有提示语
        mTipsBtn.setOnClickListener {
            ProgressDialogHelper.get()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setMsg(getString(R.string.pd_loading))
                    .create(getContext())
                    .show()
        }

        // 自定义图标
        mCustomIconBtn.setOnClickListener {
            ProgressDialogHelper.get()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .setMsg(getString(R.string.pd_loading))
                    .setIndeterminateDrawable(R.drawable.anims_custom_progress)
                    .create(getContext())
                    .show()
        }

        // 点击空白不可消失
        mCanceledOnTouchOutsideBtn.setOnClickListener {
            ProgressDialogHelper.get()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(false)
                    .setMsg(getString(R.string.pd_loading))
                    .setOnCancelListener(DialogInterface.OnCancelListener { dialog ->
                        toastShort(R.string.pd_dismiss)
                    })
                    .create(getContext())
                    .show()
        }

        // 点击空白和返回不可消失
        mCancelableBtn.setOnClickListener {
            var dialog: AlertDialog? = null
            Observable.interval(0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { i ->
                        if (i == 0L) {
                            dialog = ProgressDialogHelper.get()
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(true)
                                    .setMsg(getString(R.string.pd_loading))
                                    .create(getContext())
                            dialog?.show()
                        }
                        if (i == 5L) {
                            dialog?.dismiss()
                            throw RxException("dismiss")
                        }
                    }
                    .compose(bindDestroyEvent())
                    .subscribe(BaseObserver.empty())
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}