package com.lodz.android.agiledevkt.modules.mvc.abs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMvcTestBinding
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.rx.exception.DataException
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * MVC测试Fragment
 * Created by zhouL on 2018/11/19.
 */
class MvcTestLazyFragment : LazyFragment() {

    companion object {
        fun newInstance(): MvcTestLazyFragment = MvcTestLazyFragment()
    }

    private val mBinding: ActivityMvcTestBinding by bindingLayout(ActivityMvcTestBinding::inflate)

    override fun getAbsViewBindingLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = mBinding.root

    override fun setListeners(view: View) {
        super.setListeners(view)
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
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(object : ProgressObserver<String>() {
                    override fun onPgNext(any: String) {
                        mBinding.resultTv.text = any
                    }

                    override fun onPgError(e: Throwable, isNetwork: Boolean) {
                        if (e is DataException) {
                            mBinding.resultTv.text = e.message
                        }
                    }
                }.create(context, R.string.mvc_demo_loading, true))
    }
}