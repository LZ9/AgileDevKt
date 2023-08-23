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
            setTopLeftCornerSize(dp2px(20f))
            setBottomRightCorner(RoundedCornerTreatment())
            setBottomRightCornerSize(dp2px(20f))
            setTopRightCorner(CutCornerTreatment())
            setTopRightCornerSize(dp2px(20f))
            setBottomLeftCorner(CutCornerTreatment())
            setBottomLeftCornerSize(dp2px(20f))
        }.build()
    }

    // 代码设置 角和边
    private fun setCustomTv() {
        val model = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(dp2px(15f))
            setAllEdges(TriangleEdgeTreatment(dp2px(15f), false))
        }.build()
        val drawable = MaterialShapeDrawable(model).apply {
            setTint(getContext().getColorCompat(R.color.color_00a0e9))
            paintStyle = Paint.Style.FILL_AND_STROKE
            strokeWidth = dp2px(15f)
            strokeColor = getContext().getColorStateListCompat(R.color.red)
        }
        mBinding.customTv.setTextColor(Color.WHITE)
        mBinding.customTv.background = drawable
    }

    // 代码设置 聊天框效果
    private fun setBubbleTv() {
        val model = ShapeAppearanceModel.builder().apply {
            setAllCorners(RoundedCornerTreatment())
            setAllCornerSizes(dp2px(8f))
            setRightEdge(TriangleEdgeTreatment(dp2px(8f), false))
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