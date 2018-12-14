package com.lodz.android.componentkt.photopicker.preview

import android.content.Context
import android.content.Intent
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity

/**
 * 图片预览页面
 * Created by zhouL on 2018/12/13.
 */
class PicturePreviewActivity : AbsActivity() {

    companion object {
        private const val EXTRA_PREVIEW_BEAN = "extra_preview_bean"

        fun <T> start(context: Context, previewBean: PreviewBean<T>) {
            val intent = Intent(context, PicturePreviewActivity::class.java)
            intent.putExtra(EXTRA_PREVIEW_BEAN, previewBean)
            context.startActivity(intent)
        }
    }

    private var mPreviewBean: PreviewBean<*>? = null

    override fun startCreate() {
        super.startCreate()
        val data = intent.getSerializableExtra(EXTRA_PREVIEW_BEAN)
        if (data is PreviewBean<*>){
            mPreviewBean = data
        }
    }

    override fun getAbsLayoutId(): Int = R.layout.componentkt_activity_preview
}