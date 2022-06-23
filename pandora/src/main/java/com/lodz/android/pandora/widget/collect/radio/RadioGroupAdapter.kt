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
import com.lodz.android.pandora.widget.rv.recycler.base.BaseRvAdapter

/**
 * 单选组适配器
 * @author zhouL
 * @date 2019/5/23
 */
internal class RadioGroupAdapter(context: Context) : BaseRvAdapter<RadioableWrapper>(context) {

    /** 单选按钮图片 */
    private var mPdrBtnSrc: Drawable? = null
    /** 单选按钮图片资源id */
    private var mPdrBtnSrcResId: Int = 0
    /** 单选文字颜色 */
    private var mPdrRadioTextColor: ColorStateList? = null
    /** 单选文字大小 */
    private var mPdrRadioTextSizeSp: Float = 0f
    /** 文字距离图标的左侧间距 */
    private var mPdrMarginStartDp = 0
    /** 布局方向 */
    private var mPdrRadioGravity = 0
    /** 单选项图片宽高 */
    private var mPdrRadioBtnSquareDp = 0

    fun setBtnSrc(src: Drawable?) {
        mPdrBtnSrc = src
    }

    fun setBtnSrcResId(resId: Int) {
        mPdrBtnSrcResId = resId
    }

    fun setRadioTextColor(color: ColorStateList?) {
        mPdrRadioTextColor = color
    }

    fun setRadioTextSize(sp: Float) {
        mPdrRadioTextSizeSp = sp
    }

    fun setRadioMarginStart(dp: Int) {
        mPdrMarginStartDp = dp
    }

    fun setRadioGravity(gravity: Int) {
        mPdrRadioGravity = gravity
    }

    fun setRadioBtnSquare(dp: Int) {
        mPdrRadioBtnSquareDp = dp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            DataViewHolder(getLayoutView(parent, R.layout.pandora_item_clt_radio))

    override fun onBind(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data == null || holder !is DataViewHolder) {
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
        val radioLayout by bindView<LinearLayout>(R.id.pdr_radio_layout)
        /** 图片 */
        val radioImg by bindView<ImageView>(R.id.pdr_radio_img)
        /** 文字 */
        val radioTv by bindView<TextView>(R.id.pdr_radio_tv)

        init {
            if (mPdrBtnSrc != null) {
                radioImg.setImageDrawable(mPdrBtnSrc)
            }
            if (mPdrBtnSrcResId != 0) {
                radioImg.setImageResource(mPdrBtnSrcResId)
            }
            if (mPdrRadioTextColor != null) {
                radioTv.setTextColor(mPdrRadioTextColor)
            }
            if (mPdrRadioTextSizeSp > 0f) {
                radioTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mPdrRadioTextSizeSp)
            }
            if (mPdrMarginStartDp > 0) {
                val lp = radioTv.layoutParams as LinearLayout.LayoutParams
                lp.marginStart = context.dp2px(mPdrMarginStartDp)
                radioTv.layoutParams = lp
            }
            if (mPdrRadioBtnSquareDp > 0) {
                val lp = radioImg.layoutParams
                lp.width = context.dp2px(mPdrRadioBtnSquareDp)
                lp.height = context.dp2px(mPdrRadioBtnSquareDp)
            }
            if (mPdrRadioGravity != 0) {
                radioLayout.gravity = mPdrRadioGravity
            }
        }
    }
}