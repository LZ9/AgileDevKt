package com.lodz.android.pandora.mvvm.base.activity

import com.lodz.android.corekt.anko.toastLong
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.mvvm.vm.AbsViewModel
import com.trello.rxlifecycle4.android.ActivityEvent

/**
 * ViewModel基础Activity
 * @author zhouL
 * @date 2019/11/29
 */
abstract class AbsVmActivity : AbsActivity() {

    abstract fun getViewModel(): AbsViewModel

    override fun setListeners() {
        super.setListeners()
        setViewModelObserves()
    }

    protected open fun setViewModelObserves() {
        getViewModel().isPdrFinish.observe(getLifecycleOwner()) {
            if (it) {
                finish()
            }
        }

        getViewModel().mPdrShortToastMsg.observe(getLifecycleOwner()) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastShort(it)
        }

        getViewModel().mPdrLongToastMsg.observe(getLifecycleOwner()) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            toastLong(it)
        }
    }

    override fun startCreate() {
        super.startCreate()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.CREATE)
    }

    override fun onStart() {
        super.onStart()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onPause() {
        super.onPause()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        getViewModel().mPdrLifecycleSubject.onNext(ActivityEvent.DESTROY)
    }
}

