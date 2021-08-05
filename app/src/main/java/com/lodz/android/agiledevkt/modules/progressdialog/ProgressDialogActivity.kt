package com.lodz.android.agiledevkt.modules.progressdialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityProgressDialogBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.rx.subscribe.observer.BaseObserver
import com.lodz.android.pandora.utils.progress.ProgressDialogHelper
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
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

    private val mBinding: ActivityProgressDialogBinding by bindingLayout(ActivityProgressDialogBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

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

        // 无提示语
        mBinding.noTipsBtn.setOnClickListener {
            ProgressDialogHelper.get()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .create(getContext())
                .show()
        }

        // 有提示语
        mBinding.tipsBtn.setOnClickListener {
            ProgressDialogHelper.get()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setMsg(getString(R.string.pd_loading))
                .create(getContext())
                .show()
        }

        // 自定义图标
        mBinding.customIconBtn.setOnClickListener {
            ProgressDialogHelper.get()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setMsg(getString(R.string.pd_loading))
                .setIndeterminateDrawable(R.drawable.anims_custom_progress)
                .create(getContext())
                .show()
        }

        // 点击空白不可消失
        mBinding.canceledOnTouchOutsideBtn.setOnClickListener {
            ProgressDialogHelper.get()
                .setCancelable(true)
                .setCanceledOnTouchOutside(false)
                .setMsg(getString(R.string.pd_loading))
                .setOnCancelListener {
                    toastShort(R.string.pd_dismiss)
                }
                .create(getContext())
                .show()
        }

        // 点击空白和返回不可消失
        mBinding.cancelableBtn.setOnClickListener {
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
                        throw RuntimeException("dismiss")
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