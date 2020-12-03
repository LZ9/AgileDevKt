package com.lodz.android.pandora.mvp.base.fragment

import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.mvp.contract.abs.PresenterContract
import com.lodz.android.pandora.mvp.contract.abs.ViewContract
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.android.FragmentEvent

/**
 * MVP懒加载的fragment
 * @author zhouL
 * @date 2020/12/1
 */
abstract class MvpLazyFragment<PC : PresenterContract<VC>, VC : ViewContract> : LazyFragment(),
    ViewContract {

    /** Presenter接口 */
    private var mPresenterContract: PC? = null

    @Suppress("UNCHECKED_CAST")
    override fun startCreate() {
        super.startCreate()
        mPresenterContract = createMainPresenter()
        mPresenterContract?.attach(context, this as VC)
    }

    protected abstract fun createMainPresenter(): PC

    protected fun getPresenterContract(): PC? = mPresenterContract

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenterContract?.detach()
    }

    final override fun <T> bindUntilActivityEvent(event: ActivityEvent): LifecycleTransformer<T> {
        throw IllegalArgumentException("you bind fragment but call activity event")
    }

    final override fun <T> bindUntilFragmentEvent(event: FragmentEvent): LifecycleTransformer<T> =
        bindUntilEvent(FragmentEvent.DESTROY_VIEW)

    final override fun <T> bindUntilDetachEvent(): LifecycleTransformer<T> =
        bindUntilEvent(FragmentEvent.DESTROY_VIEW)


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

    final override fun finish() {}

}