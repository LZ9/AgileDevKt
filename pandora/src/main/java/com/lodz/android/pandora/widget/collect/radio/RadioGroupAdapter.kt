package com.lodz.android.pandora.widget.collect.radio

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.R
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter

/**
 * 单选组适配器
 * @author zhouL
 * @date 2019/5/23
 */
internal class RadioGroupAdapter(context: Context) : BaseRecyclerViewAdapter<RadioableWrapper>(context) {

    /** 单选按钮图片 */
    private var mBtnSrc: Drawable? = null
    /** 单选按钮图片资源id */
    private var mBtnSrcResId: Int = 0
    /** 单选文字颜色 */
    private var mRadioTextColor: ColorStateList? = null
    /** 单选文字大小 */
    private var mRadioTextSizeSp: Float = 0f
    /** 文字距离图标的左侧间距 */
    private var mMarginStartDp = 0
    /** 布局方向 */
    private var mRadioGravity = 0
    /** 单选项图片宽高 */
    private var mRadioBtnSquareDp = 0

    fun setBtnSrc(src: Drawable?) {
        mBtnSrc = src
    }

    fun setBtnSrcResId(resId: Int) {
        mBtnSrcResId = resId
    }

    fun setRadioTextColor(color: ColorStateList?) {
        mRadioTextColor = color
    }

    fun setRadioTextSize(sp: Float) {
        mRadioTextSizeSp = sp
    }

    fun setRadioMarginStart(dp: Int) {
        mMarginStartDp = dp
    }

    fun setRadioGravity(gravity: Int) {
        mRadioGravity = gravity
    }

    fun setRadioBtnSquare(dp: Int) {
        mRadioBtnSquareDp = dp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.pandora_item_clt_radio))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data == null || !(holder is DataViewHolder)) {
            return
        }
        showItem(holder, data)
    }

    private fun showItem(holder: DataViewHolder, data: RadioableWrapper) {
        holder.radioTv.text = data.getNameText()
        holder.radioImg.isSelected = data.isSelected
    }

    private inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /** 布局 */
        val radioLayout by bindView<LinearLayout>(R.id.radio_layout)
        /** 图片 */
        val radioImg by bindView<ImageView>(R.id.radio_img)
        /** 文字 */
        val radioTv by bindView<TextView>(R.id.radio_tv)

        init {
            if (mBtnSrc != null) {
                radioImg.setImageDrawable(mBtnSrc)
            }
            if (mBtnSrcResId != 0) {
                radioImg.setImageResource(mBtnSrcResId)
            }
            if (mRadioTextColor != null) {
                radioTv.setTextColor(mRadioTextColor)
            }
            if (mRadioTextSizeSp > 0f) {
                radioTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mRadioTextSizeSp)
            }
            if (mMarginStartDp > 0) {
                val lp = radioTv.layoutParams as LinearLayout.LayoutParams
                lp.marginStart = context.dp2px(mMarginStartDp)
                radioTv.layoutParams = lp
            }
            if (mRadioBtnSquareDp > 0) {
                val lp = radioImg.layoutParams
                lp.width = context.dp2px(mRadioBtnSquareDp)
                lp.height = context.dp2px(mRadioBtnSquareDp)
            }
            if (mRadioGravity != 0) {
                radioLayout.gravity = mRadioGravity
            }
        }
    }
}