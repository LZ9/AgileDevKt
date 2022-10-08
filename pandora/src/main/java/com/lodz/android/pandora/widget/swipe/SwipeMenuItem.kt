package com.lodz.android.pandora.widget.swipe

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.*
import com.lodz.android.corekt.anko.createColorIntDrawable
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getDrawableCompat

/**
 * @author zhouL
 * @date 2022/7/25
 */
class SwipeMenuItem(private val context: Context) {

    private var mPdrBackground: Drawable? = null
    private var mPdrIcon: Drawable? = null
    private var mPdrTitle: String = ""
    private var mPdrTitleColor: ColorStateList? = null
    private var mPdrTitleSize = 0
    private var mPdrTextTypeface: Typeface? = null
    private var mPdrTextAppearance = 0
    private var mPdrWidth = -2
    private var mPdrHeight = -2
    private var mPdrWeight = 0


    fun setBackground(@DrawableRes resId: Int): SwipeMenuItem = setBackground(context.getDrawableCompat(resId))

    fun setBackground(background: Drawable?): SwipeMenuItem {
        mPdrBackground = background
        return this
    }

    fun setBackgroundColorResource(@ColorRes color: Int) : SwipeMenuItem = setBackgroundColor(context.getColorCompat(color))

    fun setBackgroundColor(@ColorInt color: Int): SwipeMenuItem {
        mPdrBackground = context.createColorIntDrawable(color)
        return this
    }

    fun getBackground(): Drawable? = mPdrBackground

    fun setImage(@DrawableRes resId: Int): SwipeMenuItem = setImage(context.getDrawableCompat(resId))

    fun setImage(icon: Drawable?): SwipeMenuItem {
        mPdrIcon = icon
        return this
    }

    fun getImage(): Drawable? = mPdrIcon

    fun setText(@StringRes resId: Int): SwipeMenuItem = setText(context.getString(resId))

    fun setText(title: String): SwipeMenuItem {
        mPdrTitle = title
        return this
    }

    fun getText(): String = mPdrTitle

    fun setTextColorResource(@ColorRes titleColor: Int): SwipeMenuItem = setTextColor(context.getColorCompat(titleColor))

    fun setTextColor(@ColorInt titleColor: Int): SwipeMenuItem {
        mPdrTitleColor = ColorStateList.valueOf(titleColor)
        return this
    }

    fun getTitleColor(): ColorStateList? = mPdrTitleColor

    fun setTextSize(@Px titleSize: Int): SwipeMenuItem {
        mPdrTitleSize = titleSize
        return this
    }

    fun getTextSize(): Int = mPdrTitleSize

    fun setTextAppearance(@StyleRes textAppearance: Int): SwipeMenuItem {
        mPdrTextAppearance = textAppearance
        return this
    }

    fun getTextAppearance(): Int = mPdrTextAppearance

    fun setTextTypeface(textTypeface: Typeface): SwipeMenuItem {
        mPdrTextTypeface = textTypeface
        return this
    }

    fun getTextTypeface(): Typeface? = mPdrTextTypeface

    fun setWidth(width: Int): SwipeMenuItem {
        mPdrWidth = width
        return this
    }

    fun getWidth(): Int = mPdrWidth

    fun setHeight(height: Int): SwipeMenuItem {
        mPdrHeight = height
        return this
    }

    fun getHeight(): Int = mPdrHeight

    fun setWeight(weight: Int): SwipeMenuItem {
        mPdrWeight = weight
        return this
    }

    fun getWeight(): Int = mPdrWeight
}