package com.lodz.android.pandora.widget.dialogfragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.lodz.android.pandora.R
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.android.FragmentEvent
import com.trello.rxlifecycle3.components.support.RxDialogFragment

/**
 * DialogFragment基类
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseDialogFragment : RxDialogFragment() {

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCreate()
        findViews(view, savedInstanceState)
        setListeners(view)
        initData(view)
        endCreate()
    }

    protected open fun startCreate() {}

    protected open fun findViews(view: View, savedInstanceState: Bundle?) {}

    protected open fun setListeners(view: View) {}

    protected open fun initData(view: View) {}

    protected open fun endCreate() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(context, R.style.BaseDialog)
    }

    override fun onStart() {
        super.onStart()
        val wd = dialog.window
        if (wd != null) {
            setWindowAnimations(wd)
            configDialogWindow(wd)
        }
    }

    /** 设置弹窗动画 */
    private fun setWindowAnimations(window: Window) {
        val animations = configAnimations()
        if (animations != -1) {
            window.setWindowAnimations(animations)//设置窗口弹出动画
        }
    }

    /** 配置弹窗动画 */
    @StyleRes
    protected open fun configAnimations(): Int = -1

    /** 配置Dialog的Window参数 */
    protected open fun configDialogWindow(window: Window) {}

    override fun getContext(): Context {
        val ctx = super.getContext()
        if (ctx != null) {
            return ctx
        }
        return requireContext()
    }

    /** 绑定Fragment的DestroyView生命周期 */
    protected fun <T> bindDestroyViewEvent(): LifecycleTransformer<T> = bindUntilEvent(FragmentEvent.DESTROY_VIEW)
    protected fun bindAnyDestroyViewEvent(): LifecycleTransformer<Any> = bindUntilEvent(FragmentEvent.DESTROY_VIEW)
}