package com.lodz.android.agiledevkt.modules.mvc.abs

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModule
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.trello.rxlifecycle3.android.FragmentEvent

/**
 * MVC测试Fragment
 * Created by zhouL on 2018/11/19.
 */
class MvcTestLazyFragment : LazyFragment() {

    companion object {
        fun newInstance(): MvcTestLazyFragment = MvcTestLazyFragment()
    }

    /** 结果 */
    private val mResult by bindView<TextView>(R.id.result)
    /** 获取成功数据按钮 */
    private val mGetSuccessResultBtn by bindView<Button>(R.id.get_success_reuslt_btn)
    /** 获取失败数据按钮 */
    private val mGetFailResultBtn by bindView<Button>(R.id.get_fail_reuslt_btn)

    override fun getAbsLayoutId(): Int = R.layout.activity_mvc_test

    override fun setListeners(view: View) {
        super.setListeners(view)
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(object : ProgressObserver<String>() {
                    override fun onPgNext(any: String) {
                        mResult.text = any
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        if (e is DataException) {
                            mResult.text = e.getErrorMsg()
                        }
                    }
                }.create(context, R.string.mvc_demo_loading, true))
    }
}