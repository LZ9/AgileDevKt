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
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.FragmentEvent
import com.trello.rxlifecycle4.components.support.RxDialogFragment

/**
 * DialogFragment基类
 * Created by zhouL on 2018/12/13.
 */
abstract class BaseDialogFragment : RxDialogFragment() {

    /** Dialog创建回调 */
    private var mOnDialogCreatedListener: ((dialog: Dialog) -> Unit)? = null

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layoutId = getLayoutId()
        val view = if (layoutId != 0) {
            inflater.inflate(layoutId, container, false)
        } else {
            getViewBindingLayout(inflater, container, savedInstanceState)
        }
        if (view == null){
            throw NullPointerException("please override getAbsLayoutId() or getAbsViewBindingLayout() to set layout")
        }
        return view
    }

    @LayoutRes
    protected open fun getLayoutId(): Int = 0

    protected open fun getViewBindingLayout(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = null

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
        val d = Dialog(context, R.style.BaseDialog)
        mOnDialogCreatedListener?.invoke(d)
        return d
    }

    override fun onStart() {
        super.onStart()
        val wd = dialog?.window
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

    /** 设置Dialog创建回调[listener] */
    fun setOnDialogCreatedListener(listener: ((dialog: Dialog) -> Unit)?) {
        mOnDialogCreatedListener = listener
    }

    /** 绑定Fragment的DestroyView生命周期 */
    protected fun <T> bindDestroyViewEvent(): LifecycleTransformer<T> = bindUntilEvent(FragmentEvent.DESTROY_VIEW)
    protected fun bindAnyDestroyViewEvent(): LifecycleTransformer<Any> = bindUntilEvent(FragmentEvent.DESTROY_VIEW)
}