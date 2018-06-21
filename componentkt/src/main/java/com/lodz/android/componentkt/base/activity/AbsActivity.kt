package com.lodz.android.componentkt.base.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import com.lodz.android.componentkt.base.fragment.IFragmentBackPressed
import com.lodz.android.componentkt.base.fragment.LazyFragment
import com.lodz.android.componentkt.event.ActivityFinishEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 底层抽象Activity（已经订阅了EventBus）
 * Created by zhouL on 2018/6/20.
 */
abstract class AbsActivity : RxAppCompatActivity() {

    /** 使用Anko Layouts */
    protected val USE_ANKO_LAYOUTS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        startCreate()
        if (getAbsLayoutId() != USE_ANKO_LAYOUTS) {
            setContentView(getAbsLayoutId())
        }
        afterSetContentView()
        findViews(savedInstanceState)
        setListeners()
        initData()
        endCreate()
    }

    protected open fun startCreate() {}

    @LayoutRes
    protected abstract fun getAbsLayoutId(): Int

    protected open fun afterSetContentView() {}

    protected abstract fun findViews(savedInstanceState: Bundle?)

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected open fun getContext() = this

    override fun finish() {
        EventBus.getDefault().unregister(this)
        super.finish()
    }

    final override fun onBackPressed() {
        if (supportFragmentManager != null) {
            val list: List<Fragment>? = supportFragmentManager.fragments// 获取activity下的fragment
            if (list != null && list.size > 0) {
                for (fragment in list) {
                    if (isFragmentConsumeBackPressed(fragment)) {
                        return
                    }
                }
            }
        }
        if (!onPressBack()) {
            super.onBackPressed()
        }
    }

    private fun isFragmentConsumeBackPressed(fragment: Fragment): Boolean {
        // fragment底下还有子fragment
        if (fragment.childFragmentManager.fragments != null && fragment.childFragmentManager.fragments.size > 0) {
            val list = fragment.childFragmentManager.fragments
            for (f in list) {
                if (isFragmentConsumeBackPressed(f)) {
                    return true
                }
            }
        }

        // fragment底下没有子fragment或子fragment没有消费事件 则判断自己
        if (fragment.userVisibleHint && fragment.isVisible && fragment is IFragmentBackPressed) {
            val parentFragment: Fragment? = fragment.parentFragment
            if (parentFragment != null) {
                // 如果子fragment的父fragment没有显示，则不询问该fragment的返回事件（避免受预先初始化却没有展示到前端的fragment的影响）
                if (!parentFragment.userVisibleHint) {
                    return false
                }
            }
            if (fragment is LazyFragment){
                val lazyFragment : LazyFragment = fragment
                TODO("需要使用反射")
            }
            val itf = fragment as IFragmentBackPressed
            return itf.onPressBack()// fragment是否消耗返回按钮事件
        }
        return false;
    }

    protected open fun onPressBack() = false

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMessageEvent(event: ActivityFinishEvent) {
        if (!isFinishing) {
            finish()
        }
    }

}