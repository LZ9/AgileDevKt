package com.lodz.android.agiledevkt.modules.mvc.abs

import android.content.Context
import android.content.Intent
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.trello.rxlifecycle4.android.ActivityEvent

/**
 * MVC基础Activity
 * Created by zhouL on 2018/11/19.
 */
class MvcTestAbsActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MvcTestAbsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityMvcTestBinding by bindingLayout(ActivityMvcTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()

        // 获取成功数据按钮
        mBinding.getSuccessReusltBtn.setOnClickListener {
            getResult(true)
        }

        // 获取失败数据按钮
        mBinding.getFailReusltBtn.setOnClickListener {
            getResult(false)
        }
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : ProgressObserver<String>() {
                    override fun onPgNext(any: String) {
                        mBinding.resultTv.text = any
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        if (e is DataException) {
                            mBinding.resultTv.text = e.message
                        }
                    }
                }.create(getContext(), R.string.mvc_demo_loading, true))
    }

}