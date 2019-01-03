package com.lodz.android.pandora.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.pandora.base.activity.UseAnkoLayout
import com.trello.rxlifecycle3.components.support.RxFragment

/**
 * 懒加载的fragment
 * Created by zhouL on 2018/6/20.
 */
abstract class LazyFragment : RxFragment(), IFragmentBackPressed {

    /** 是否使用Anko Layout */
    private var isUseAnko = false

    /** 父控件布局  */
    private var mParentView: View? = null
    /** 是否使用懒加载  */
    private var isLazyLoad = true
    /** 是否完成加载  */
    private var isLoadComplete = false
    /** 是否首次启动  */
    private var isFirstCreate = true
    /** 是否首次启动Resume  */
    private var isFirstResume = true
    /** 当前的fragment是否已经暂停  */
    private var isAlreadyPause = false
    /** 是否从OnPause离开  */
    private var isOnPauseOut = false

    /** 是否使用AnkoLayout */
    protected fun isUseAnkoLayout(): Boolean = isUseAnko

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isUseAnko = injectAnko()
        if (!isUseAnko) {//不使用AnkoLayout再加载布局
            return inflater.inflate(getAbsLayoutId(), container, false)
        }
        val view = getAnkoLayoutView()
        if (view != null) {
            return view
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @LayoutRes
    protected abstract fun getAbsLayoutId(): Int

    protected open fun getAnkoLayoutView(): View? = null

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isLazyLoad = configIsLazyLoad()
        var isInit = false
        if (isVisibleToUser && isLazyLoad && !isFirstCreate && !isLoadComplete) {// fragment可见 && 启用懒加载 && 不是第一次启动 && 未加载完成
            init(mParentView!!, null)
            isLoadComplete = true
            isInit = true
        }
        if (isVisibleToUser && isLoadComplete) {
            onFragmentResume()
            if (!isInit) {
                notifyChildResume(this)
            }
        }
        if (!isVisibleToUser && isLoadComplete) {
            onFragmentPause()
            notifyChildPause(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoadComplete = false// fragment被回收时重置加载状态
        mParentView = null
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mParentView = view
        if (!isLazyLoad || userVisibleHint) {// 不使用懒加载 || fragment可见
            init(view, savedInstanceState)
            isLoadComplete = true
            onFragmentResume()
        }
        isFirstCreate = false
    }


    private fun init(view: View, savedInstanceState: Bundle?) {
        startCreate()
        beforeFindViews(view)
        findViews(view, savedInstanceState)
        setListeners(view)
        initData(view)
        endCreate()
    }

    protected open fun startCreate() {}

    protected open fun beforeFindViews(view: View) {}

    protected open fun findViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setListeners(view: View) {}

    protected open fun initData(view: View) {}

    protected open fun endCreate() {}

    /** 配置是否使用懒加载（默认使用，可重写） */
    protected open fun configIsLazyLoad(): Boolean = true

    override fun onResume() {
        super.onResume()
        if (isFirstResume) {
            isFirstResume = false
            return
        }
        if (userVisibleHint && isLoadComplete) {//自己显示 && 已经加载
            val parent = parentFragment
            if (parent != null && !parent.userVisibleHint) {// 父类不显示
                return
            }
            if (isOnPauseOut) {//自己是从OnPause回来的
                onFragmentResume()
                isOnPauseOut = false
            }
        }
    }

    /** FragmentResume时调用，与activity生命周期保持一致 */
    protected open fun onFragmentResume() {
        isAlreadyPause = false
    }

    /** 通知内部显示着的fragment显示 */
    private fun notifyChildResume(fragment: Fragment) {
        val fragments = fragment.childFragmentManager.fragments//获取fragment底下的其他fragment
        if (fragments.getSize() == 0) {
            return
        }
        for (f in fragments) {
            val parent = f.parentFragment
            if (parent != null && !parent.userVisibleHint) {//它的父类没有显示
                continue
            }
            if (f.userVisibleHint && f is LazyFragment) {// 父类显示自己也显示
                f.onFragmentResume()
            }
            notifyChildResume(f)
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint && isLoadComplete && !isAlreadyPause) {// 自己显示 && 已加载完成 && 未调用过pause方法
            val parent = parentFragment
            if (parent != null && !parent.userVisibleHint) {//父类没有显示
                return
            }
            if (parent != null && parent is LazyFragment) {
                if (parent.isAlreadyPause) {// 父类已经暂停了
                    return
                }
            }
            isOnPauseOut = true
            onFragmentPause()
        }
    }

    /** FragmentPause时调用，与activity生命周期保持一致 */
    protected open fun onFragmentPause() {
        isAlreadyPause = true
    }

    private fun notifyChildPause(fragment: Fragment) {
        val fragments = fragment.childFragmentManager.fragments//获取fragment底下的其他fragment
        if (fragments.getSize() == 0) {
            return
        }
        for (f in fragments) {
            if (f.userVisibleHint && f is LazyFragment) {
                if (!f.isAlreadyPause) {//子类的fragment没有暂停过
                    f.onFragmentPause()
                }
            }
            notifyChildPause(f)
        }
    }

    override fun onPressBack(): Boolean = false

    override fun getContext(): Context {
        val ctx = super.getContext()
        if (ctx != null) {
            return ctx
        }
        return requireContext()
    }

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