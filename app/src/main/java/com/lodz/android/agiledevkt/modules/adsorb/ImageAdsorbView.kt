package com.lodz.android.agiledevkt.modules.adsorb

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.RoundedCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.lodz.android.agiledevkt.R
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.widget.adsorb.AdsorbView

/**
 * 吸边控件
 * @author zhouL
 * @date 2022/10/18
 */
class ImageAdsorbView : AdsorbView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun getContentView(): View {
        val imageView = ShapeableImageView(context)
        imageView.setImageResource(R.drawable.ic_regret)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.shapeAppearanceModel = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(dp2px(15f))
        }.build()
        return imageView
    }
}