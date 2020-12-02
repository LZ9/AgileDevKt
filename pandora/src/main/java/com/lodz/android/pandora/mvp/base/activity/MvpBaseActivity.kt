package com.lodz.android.pandora.mvp.base.activity

import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.mvp.contract.abs.PresenterContract
import com.lodz.android.pandora.mvp.contract.base.BaseViewContract
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * MVP基类Activity（带基础状态控件）
 * @author zhouL
 * @date 2020/12/1
 */
abstract class MvpBaseActivity<PC : PresenterContract<VC>, VC : BaseViewContract> : BaseActivity(), BaseViewContract {

    /** Presenter接口 */
    private var mPresenterContract: PC? = null

    override fun startCreate() {
        super.startCreate()
        mPresenterContract = createMainPresenter()
        mPresenterContract?.attach(this, this as VC)
    }

    protected abstract fun createMainPresenter(): PC

    protected fun getPresenterContract(): PC? = mPresenterContract

    override fun onDestroy() {
        super.onDestroy()
        mPresenterContract?.detach()
    }

    override fun showStatusNoData() {
        super.showStatusNoData()
    }

    override fun showStatusError(t: Throwable?) {
        super.showStatusError(t)
    }

    override fun showStatusError() {
        super.showStatusError(null)
    }

    override fun showStatusLoading() {
        super.showStatusLoading()
    }

    override fun showStatusCompleted() {
        super.showStatusCompleted()
    }

    override fun goneTitleBar() {
        super.goneTitleBar()
    }

    override fun showTitleBar() {
        super.showTitleBar()
    }

    final override fun <T> bindUntilActivityEvent(event: ActivityEvent): LifecycleTransformer<T>  = bindUntilEvent(event)

    final override fun <T> bindUntilFragmentEvent(event: FragmentEvent): LifecycleTransformer<T> {
        throw IllegalArgumentException("you bind activity but call fragment event")
    }

    final override fun <T> bindUntilDetachEvent(): LifecycleTransformer<T> = bindUntilEvent(ActivityEvent.DESTROY)

    final override fun showShortToast(resId: Int) {
        showShortToast(getString(resId))
    }

    final override fun showShortToast(tips: String) {
        toastShort(tips)
    }

    final override fun showLongToast(resId: Int) {
        showLongToast(getString(resId))
    }

    final override fun showLongToast(tips: String) {
        toastLong(tips)
    }
}