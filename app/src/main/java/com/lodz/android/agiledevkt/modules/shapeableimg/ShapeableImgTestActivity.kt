package com.lodz.android.agiledevkt.modules.shapeableimg

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import com.lodz.android.pandora.base.activity.BaseActivity

import android.view.View
import com.google.android.material.shape.*
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityShapeableImgTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.corekt.anko.*

/**
 * ShapeableImageView测试类
 * @author zhouL
 * @date 2022/10/14
 */
class ShapeableImgTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ActivityShapeableImgTestBinding::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityShapeableImgTestBinding by bindingLayout(ActivityShapeableImgTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        setCustomImg()
        setCustomTv()
        setBubbleTv()
    }

    // 代码设置圆角、切角
    private fun setCustomImg() {
        mBinding.customSimg.shapeAppearanceModel = ShapeAppearanceModel.builder().apply {
            setTopLeftCorner(RoundedCornerTreatment())
            setTopLeftCornerSize(dp2pxRF(20))
            setBottomRightCorner(RoundedCornerTreatment())
            setBottomRightCornerSize(dp2pxRF(20))
            setTopRightCorner(CutCornerTreatment())
            setTopRightCornerSize(dp2pxRF(20))
            setBottomLeftCorner(CutCornerTreatment())
            setBottomLeftCornerSize(dp2pxRF(20))
        }.build()
    }

    // 代码设置 角和边
    private fun setCustomTv() {
        val model = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(dp2pxRF(15))
            setAllEdges(TriangleEdgeTreatment(dp2pxRF(15), false))
        }.build()
        val drawable = MaterialShapeDrawable(model).apply {
            setTint(getContext().getColorCompat(R.color.color_00a0e9))
            paintStyle = Paint.Style.FILL_AND_STROKE
            strokeWidth = dp2pxRF(15)
            strokeColor = getContext().getColorStateListCompat(R.color.red)
        }
        mBinding.customTv.setTextColor(Color.WHITE)
        mBinding.customTv.background = drawable
    }

    // 代码设置 聊天框效果
    private fun setBubbleTv() {
        val model = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(dp2pxRF(8))
            setRightEdge(TriangleEdgeTreatment(dp2pxRF(8), false))
        }.build()
        val drawable = MaterialShapeDrawable(model).apply {
            setTint(getContext().getColorCompat(R.color.color_00a0e9))
            paintStyle = Paint.Style.FILL
        }

        mBinding.bubbleTv.setTextColor(Color.WHITE)
        mBinding.bubbleTv.background = drawable
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}