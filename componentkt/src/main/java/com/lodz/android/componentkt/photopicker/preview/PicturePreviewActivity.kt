package com.lodz.android.componentkt.photopicker.preview

import android.content.Context
import android.content.Intent
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.photopicker.contract.preview.PreviewController

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
class PicturePreviewActivity : AbsActivity() {

    companion object {

        private var sPreviewBean: PreviewBean<*>? = null

        fun <T> start(context: Context, previewBean: PreviewBean<T>) {
            synchronized(this) {
                if (sPreviewBean != null) {
                    return
                }
                sPreviewBean = previewBean
                val intent = Intent(context, PicturePreviewActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getAbsLayoutId(): Int = R.layout.componentkt_activity_preview

    override fun initData() {
        super.initData()
        val a = sPreviewBean!!.sourceList!!.get(0)
//        sPreviewBean!!.clickListener!!.onClick(getContext(), a, 0, mPreviewController)
    }

    private val mPreviewController = object : PreviewController {
        override fun close() {
            finish()
        }
    }

    override fun finish() {
        sPreviewBean?.clear()
        sPreviewBean = null
        super.finish()
    }
}