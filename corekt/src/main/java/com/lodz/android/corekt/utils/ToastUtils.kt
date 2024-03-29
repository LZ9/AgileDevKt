package com.lodz.android.corekt.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lodz.android.corekt.anko.runOnMain

/**
 * Toast帮助类
 * Created by zhouL on 2018/6/26.
 */
class ToastUtils private constructor(private val mContext: Context) {

    /** 提示语 */
    private var mText: String? = null
    /** 时长 */
    private var mDuration = Toast.LENGTH_SHORT
    /** 位置 */
    private var mGravity = Gravity.NO_GRAVITY
    /** 位置的X轴偏移量 */
    private var mGravityOffsetX = 0
    /** 位置的Y轴偏移量 */
    private var mGravityOffsetY = 0
    /** 图片资源id */
    @DrawableRes
    private var mImgResId = 0
    /** 自定义界面 */
    private var mCustomView: View? = null

    companion object {

        /** 通过上下文[context]创建ToastUtils对象 */
        @JvmStatic
        fun create(context: Context): ToastUtils {
            return ToastUtils(context)
        }

        /** 通过上下文[context]显示文字为[text]的短时间的Toast */
        @JvmStatic
        fun showShort(context: Context, text: String) {
            create(context).setText(text).setShort().show()
        }

        /** 通过上下文[context]显示文字资源为[strResId]的短时间的Toast */
        @JvmStatic
        fun showShort(context: Context, @StringRes strResId: Int) {
            create(context).setText(strResId).setShort().show()
        }

        /** 通过上下文[context]显示文字为[text]的长时间的Toast */
        @JvmStatic
        fun showLong(context: Context, text: String) {
            create(context).setText(text).setLong().show()
        }

        /** 通过上下文[context]显示文字资源为[strResId]的长时间的Toast */
        @JvmStatic
        fun showLong(context: Context, @StringRes strResId: Int) {
            create(context).setText(strResId).setLong().show()
        }

    }

    /** 设置提示文字[text] */
    fun setText(text: String): ToastUtils {
        this.mText = text
        return this
    }

    /** 设置提示文字资源[textResId] */
    fun setText(@StringRes textResId: Int): ToastUtils {
        this.mText = mContext.getString(textResId)
        return this
    }

    /** 设置短时间 */
    fun setShort(): ToastUtils {
        mDuration = Toast.LENGTH_SHORT
        return this
    }

    /** 设置长时间 */
    fun setLong(): ToastUtils {
        mDuration = Toast.LENGTH_LONG
        return this
    }

    /** 设置显示位置[dravity] */

    @Deprecated(
        message = "在Android Build.VERSION_CODES.R 及以上版本，此方法不会生效",
        level = DeprecationLevel.WARNING
    )
    fun setGravity(dravity: Int): ToastUtils {
        mGravity = dravity
        return this
    }

    /** 设置显示位置[dravity]以及X轴偏移量[xOffset]和Y轴偏移量[yOffset] */
    @Deprecated(
        message = "在Android Build.VERSION_CODES.R 及以上版本，此方法不会生效",
        level = DeprecationLevel.WARNING
    )
    fun setGravity(dravity: Int, xOffset: Int, yOffset: Int): ToastUtils {
        mGravity = dravity
        mGravityOffsetX = xOffset
        mGravityOffsetY = yOffset
        return this
    }

    /** 设置toast顶部图片资源[imgResId] */
    @Deprecated(
        message = "在Android Build.VERSION_CODES.R 及以上版本，此方法不会生效",
        level = DeprecationLevel.WARNING
    )
    fun setTopImg(@DrawableRes imgResId: Int): ToastUtils {
        mImgResId = imgResId
        return this
    }

    /** 设置自定义界面[view] */
    fun setView(view: View): ToastUtils {
        mCustomView = view
        return this
    }

    /** 获取Toast对象 */
    fun getToast(): Toast? {
        var toast: Toast? = null
        if (!mText.isNullOrEmpty()) {
            toast = getDefaultToast()
        } else if (mCustomView != null) {
            toast = getCustomViewToast()
        }

        if (toast == null) {
            return null
        }

        if (mGravity != Gravity.NO_GRAVITY) {
            toast.setGravity(mGravity, mGravityOffsetX, mGravityOffsetY)
        }

        if (mImgResId != 0 && mCustomView == null && toast.view != null) {//没有自定义view的情况下才设置
            val toastView: LinearLayout = toast.view as LinearLayout
            val imageCodeProject = ImageView(mContext)
            imageCodeProject.setImageResource(mImgResId)
            toastView.addView(imageCodeProject, 0)
        }
        return toast
    }

    /** 显示Toast */
    fun show() {
        if (AppUtils.isMainThread()) {//主线程直接显示
            getToast()?.show()
            return
        }
        runOnMain {
            getToast()?.show()//非主线程post到主线程显示
        }
    }

    /** 获取默认的toast */
    private fun getDefaultToast(): Toast = Toast.makeText(mContext, mText, mDuration)

    /** 获取自定义view的toast */
    private fun getCustomViewToast(): Toast {
        val toast = Toast(mContext)
        toast.view = mCustomView
        return toast
    }
}
