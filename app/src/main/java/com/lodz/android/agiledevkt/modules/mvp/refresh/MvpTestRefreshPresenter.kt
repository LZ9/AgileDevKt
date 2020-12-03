package com.lodz.android.agiledevkt.modules.mvp.refresh

import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.mvc.ApiModuleRx
import com.lodz.android.pandora.mvp.presenter.BasePresenter
import com.lodz.android.pandora.rx.subscribe.observer.RxObserver
import com.lodz.android.pandora.rx.utils.RxUtils

/**
 * 测试Presenter
 * @author zhouL
 * @date 2020/12/2
 */
class MvpTestRefreshPresenter : BasePresenter<MvpTestRefreshViewContract>() {

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

    fun getRefreshData(isSuccess: Boolean) {
        ApiModuleRx.requestResult(isSuccess)
            .compose(RxUtils.ioToMainObservable())
            .compose(bindUntilDetachEvent())
            .subscribe(
                RxObserver.action(
                    next = {
                        getViewContract()?.setSwipeRefreshFinish()
                        getViewContract()?.showResult()
                        getViewContract()?.setResult(it)
                        getViewContract()?.showStatusCompleted()
                    },
                    error = { e, isNetwork ->
                        getViewContract()?.setSwipeRefreshFinish()
                        getViewContract()?.showShortToast(R.string.mvp_demo_refresh_data_fail)
                    })
            )
    }
}