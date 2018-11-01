package com.lodz.android.corekt.utils

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.lodz.android.corekt.R

/**
 * Snackbar帮助类
 * Created by zhouL on 2018/9/7.
 */
class SnackbarUtils private constructor() {

    private lateinit var mSnackbar: Snackbar

    companion object {

        /** 创建一个短时Snackbar，控件容器[view]，内容文字[text] */
        fun createShort(view: View, text: String): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
            return utils
        }

        /** 创建一个短时Snackbar，控件容器[view]，内容文字[textRes] */
        fun createShort(view: View, @StringRes textRes: Int): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, textRes, Snackbar.LENGTH_SHORT)
            return utils
        }

        /** 创建一个长时Snackbar，控件容器[view]，内容文字[text] */
        fun createLong(view: View, text: String): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            return utils
        }

        /** 创建一个长时Snackbar，控件容器[view]，内容文字[textRes] */
        fun createLong(view: View, @StringRes textRes: Int): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, textRes, Snackbar.LENGTH_LONG)
            return utils
        }

        /** 创建一个自定义时长Snackbar，控件容器[view]，内容文字[text]，时长[duration]以毫秒为单位 */
        fun createCustom(view: View, text: String, duration: Int): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE).setDuration(duration)
            return utils
        }

        /** 创建一个自定义时长Snackbar，控件容器[view]，内容文字[textRes]，时长[duration]以毫秒为单位 */
        fun createCustom(view: View, @StringRes textRes: Int, duration: Int): SnackbarUtils {
            val utils = SnackbarUtils()
            utils.mSnackbar = Snackbar.make(view, textRes, Snackbar.LENGTH_INDEFINITE).setDuration(duration)
            return utils
        }
    }

    /** 设置文字颜色[textColor] */
    fun setTextColor(@ColorInt textColor: Int): SnackbarUtils {
        mSnackbar.view.findViewById<TextView>(R.id.snackbar_text).setTextColor(textColor)
        return this
    }

    /** 设置文字大小[sizeSp]（单位SP） */
    fun setTextSize(sizeSp: Int): SnackbarUtils {
        mSnackbar.view.findViewById<TextView>(R.id.snackbar_text).setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp.toFloat())
        return this
    }

    /** 添加文字左侧图片[resId]，图文间距[drawablePadding]（单位px，默认0） */
    fun addLeftImage(@DrawableRes resId: Int, drawablePadding: Int = 0): SnackbarUtils {
        val textView = mSnackbar.view.findViewById<TextView>(R.id.snackbar_text)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0)
        textView.compoundDrawablePadding = drawablePadding
        return this
    }

    /** 添加文字右侧图片[resId]，图文间距[drawablePadding]（单位px，默认0） */
    fun addRightImage(@DrawableRes resId: Int, drawablePadding: Int = 0): SnackbarUtils {
        val textView = mSnackbar.view.findViewById<TextView>(R.id.snackbar_text)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, resId, 0)
        textView.compoundDrawablePadding = drawablePadding
        return this
    }

    /** 设置背景色[backgroundColor] */
    fun setBackgroundColor(@ColorInt backgroundColor: Int): SnackbarUtils {
        mSnackbar.view.setBackgroundColor(backgroundColor)
        return this
    }

    /** 替换内容布局[layoutId]，布局宽度[width]（默认LinearLayout.LayoutParams.MATCH_PARENT），布局高度[height]（默认LinearLayout.LayoutParams.MATCH_PARENT） */
    fun replaceLayoutView(@LayoutRes layoutId: Int, width: Int = LinearLayout.LayoutParams.MATCH_PARENT, height: Int = LinearLayout.LayoutParams.MATCH_PARENT): SnackbarUtils {
        val snackbarLayout = mSnackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(LayoutInflater.from(snackbarLayout.context).inflate(layoutId, null), 0, layoutParams)
        return this
    }

    /** 替换内容布局[view]，布局宽度[width]（默认LinearLayout.LayoutParams.MATCH_PARENT），布局高度[height]（默认LinearLayout.LayoutParams.MATCH_PARENT） */
    fun replaceLayoutView(view: View, width: Int = LinearLayout.LayoutParams.MATCH_PARENT, height: Int = LinearLayout.LayoutParams.MATCH_PARENT): SnackbarUtils {
        val snackbarLayout = mSnackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.removeAllViews()
        val layoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(view, 0, layoutParams)
        return this
    }

    /** 得到Snackbar */
    fun getSnackbar(): Snackbar = mSnackbar

    /** 显示Snackbar */
    fun show() {
        mSnackbar.show()
    }
}