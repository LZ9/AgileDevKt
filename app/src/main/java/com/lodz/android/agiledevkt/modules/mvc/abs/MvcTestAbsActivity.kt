package com.lodz.android.agiledevkt.modules.mvc.abs

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModule
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.rx.exception.DataException
import com.lodz.android.componentkt.rx.subscribe.observer.ProgressObserver
import com.lodz.android.componentkt.rx.utils.RxUtils
import com.lodz.android.corekt.anko.bindView
import com.trello.rxlifecycle3.android.ActivityEvent

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

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun getAbsLayoutId(): Int = R.layout.activity_mvc_test

    override fun setListeners() {
        super.setListeners()
        mGetSuccessResultBtn.setOnClickListener {
            getResult(true)
        }

        mGetFailResultBtn.setOnClickListener {
            getResult(false)
        }
    }

    private fun getResult(isSuccess: Boolean) {
        ApiModule.requestResult(isSuccess)
                .compose(RxUtils.ioToMainObservable())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(object : ProgressObserver<String>() {
                    override fun onPgNext(any: String) {
                        mResult.text = any
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        if (e is DataException) {
                            mResult.text = e.getErrorMsg()
                        }
                    }
                }.create(getContext(), R.string.mvc_demo_loading, true))
    }

}