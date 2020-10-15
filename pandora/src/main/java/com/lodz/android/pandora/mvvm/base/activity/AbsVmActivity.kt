package com.lodz.android.pandora.mvvm.base.activity

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
abstract class AbsVmActivity<VM : AbsViewModel> : AbsActivity() {

    private val mPdrViewModel by lazy { ViewModelProvider(this).get(createViewModel()) }

    fun getViewModel(): VM = mPdrViewModel

    abstract fun createViewModel(): Class<VM>

    override fun setListeners() {
        super.setListeners()

        getViewModel().isPdrFinish.observe(this, Observer { value ->
            if (value) { finish() }
        })

        getViewModel().mPdrShortToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastShort(value)
        })

        getViewModel().mPdrLongToastMsg.observe(this, Observer { value ->
            if (value.isNullOrEmpty()) {
                return@Observer
            }
            toastLong(value)
        })
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

