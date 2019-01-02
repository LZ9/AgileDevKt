package com.lodz.android.pandora.base.activity

import android.content.Context
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.base.fragment.IFragmentBackPressed
import com.lodz.android.pandora.base.fragment.LazyFragment
import com.lodz.android.pandora.event.ActivityFinishEvent
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 底层抽象Activity（已经订阅了EventBus）
 * Created by zhouL on 2018/6/20.
 */
abstract class AbsActivity : RxAppCompatActivity() {

    /** 是否使用Anko Layout */
    private var isUseAnko = false

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUseAnko = injectAnko()
        EventBus.getDefault().register(this)
        startCreate()
        if (!isUseAnko) {//不使用AnkoLayout再加载布局
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

    protected open fun findViews(savedInstanceState: Bundle?) {}

    protected open fun setListeners() {}

    protected open fun initData() {}

    protected open fun endCreate() {}

    protected open fun getContext(): Context = this

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
        if (fragment.childFragmentManager.fragments.size > 0) {
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
            if (fragment is LazyFragment) {
                val lazyFragment: LazyFragment = fragment
                val c: Class<*>? = ReflectUtils.getClassForName("com.lodz.android.component.base.fragment.LazyFragment")
                if (c != null) {
                    val obj: Any? = ReflectUtils.getFieldValue(c, lazyFragment, "isAlreadyPause")// 通过反射取到该fragment是否已经进入暂停状态
                    if (obj != null && obj is Boolean) {
                        val isAlreadyPause: Boolean = obj
                        if (isAlreadyPause) {
                            return false
                        }
                    }
                }
            }
            val itf = fragment as IFragmentBackPressed
            return itf.onPressBack()// fragment是否消耗返回按钮事件
        }
        return false;
    }

    /** 点击返回按钮 */
    protected open fun onPressBack(): Boolean = false

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onActivityFinishEvent(event: ActivityFinishEvent) {
        if (!isFinishing) {
            finish()
        }
    }

    /** 当APP处于后台被系统回收时回调 */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val bundle = getSaveBundle()
        val app = BaseApplication.get()
        if (app != null && bundle != null) {
            app.putSaveInstanceState(javaClass.simpleName, bundle)
        }
    }

    /** 获取回收前需要被保存的数据 */
    protected open fun getSaveBundle(): Bundle? = null

    /** 被回收后从后台回到前台调用 */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val app = BaseApplication.get()
        if (app == null) {
            return
        }

        val bundle = app.getSaveInstanceState(javaClass.simpleName)
        if (bundle != null) {
            onRestore(bundle)
        }
    }

    /** 被回收后从后台回到前台调用，返回之前保存的数据[bundle] */
    protected open fun onRestore(bundle: Bundle) {}

    /** 添加[fragment]，传入父控件id[containerViewId]和fragment的[tag]标签 */
    protected fun addFragment(@IdRes containerViewId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().add(containerViewId, fragment, tag).commit()
    }

    /** 替换[fragment]，传入父控件id[containerViewId]和fragment的[tag]标签 */
    protected fun replaceFragment(@IdRes containerViewId: Int, fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction().replace(containerViewId, fragment, tag).commit()
    }

    /** 显示[fragment] */
    protected fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }

    /** 隐藏[fragment] */
    protected fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(fragment).commit()
    }

    /** 获取所有的fragment */
    protected fun getFragments(): List<Fragment> = supportFragmentManager.fragments


    /** 根据[tag]标签找到对应的fragment */
    protected fun findFragmentByTag(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    /** 根据[id]找到对应的fragment */
    protected fun findFragmentById(@IdRes id: Int): Fragment? = supportFragmentManager.findFragmentById(id)

    /** 添加到回退堆栈，回退堆栈的名称[name]可为null */
    protected fun addToBackStack(name: String?) {
        supportFragmentManager.beginTransaction().addToBackStack(name).commit()
    }

    /** 是否使用AnkoLayout */
    protected fun isUseAnkoLayout(): Boolean = isUseAnko

    /** 解析AnkoLayout注解 */
    private fun injectAnko(): Boolean {
        try {
            if (!javaClass.isAnnotationPresent(UseAnkoLayout::class.java)) {
                return false
            }
            // 进行了UseAnkoLayout注解
            val inject = javaClass.getAnnotation(UseAnkoLayout::class.java)
            if (inject == null) {
                return false
            }
            return inject.value
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}