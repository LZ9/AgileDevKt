package com.lodz.android.agiledevkt.modules.mvp.base

import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.mvp.presenter.BasePresenter
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils

/**
 * 测试Presenter
 * @author zhouL
 * @date 2020/12/2
 */
class MvpTestBasePresenter : BasePresenter<MvpTestBaseViewContract>() {

    fun getResult(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilDetachEvent())
            .subscribe(
                RxObserver.action(
                    next = {
                        getViewContract()?.showResult()
                        getViewContract()?.setResult(it)
                        getViewContract()?.showStatusCompleted()
                    },
                    error = { e, isNetwork ->
                        getViewContract()?.showStatusError(e)
                    })
            )
    }
}