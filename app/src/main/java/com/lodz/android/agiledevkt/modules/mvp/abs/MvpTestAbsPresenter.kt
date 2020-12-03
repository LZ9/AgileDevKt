package com.lodz.android.agiledevkt.modules.mvp.abs

import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.mvp.presenter.BasePresenter
import com.lodz.android.pandora.rx.subscribe.observer.ProgressObserver
import com.lodz.android.pandora.rx.utils.RxUtils

/**
 * 测试Presenter
 * @author zhouL
 * @date 2020/12/2
 */
class MvpTestAbsPresenter : BasePresenter<MvpTestAbsViewContract>() {

    fun getResult(isSuccess: Boolean) {
        val context = getContext() ?: return
        ApiModuleRx.requestResult(isSuccess)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilDetachEvent())
            .subscribe(
                ProgressObserver.action(
                    context = context,
                    msg = context.getString(R.string.mvc_demo_loading),
                    next = {
                        getViewContract()?.showResult()
                        getViewContract()?.setResult(it)
                    },
                    error = { e, isNetwork ->
                        getViewContract()?.showResult()
                        getViewContract()?.setResult(RxUtils.getExceptionTips(e, isNetwork, "fail"))
                    }
                )
            )
    }
}