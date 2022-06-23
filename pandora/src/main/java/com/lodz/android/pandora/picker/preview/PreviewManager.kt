package com.lodz.android.pandora.picker.preview

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.utils.ToastUtils
import com.lodz.android.pandora.R

/**
 * 图片预览管理类
 * Created by zhouL on 2018/12/13.
 */
class PreviewManager<T : Any, VH : RecyclerView.ViewHolder> internal constructor(private val previewBean: PreviewBean<T, VH>) {
    companion object {
        @JvmStatic
        fun <T : Any, VH : RecyclerView.ViewHolder> create(): PreviewBuilder<T, VH> = PreviewBuilder()
    }

    /** 打开预览器，上下文[context]，Intent启动标记[flags]  */
    @JvmOverloads
    fun open(context: Context, flags: List<Int>? = null) {
        if (previewBean.adapter == null) {// 校验图片加载器
            ToastUtils.showShort(context, R.string.pandora_preview_adapter_unset)
            return
        }
        if (previewBean.sourceList.isNullOrEmpty()) {// 校验数据列表
            ToastUtils.showShort(context, R.string.pandora_preview_source_list_empty)
            return
        }
        if (previewBean.sourceList.getSize() == 1) {// 只有一张图片不显示页码
            previewBean.isShowPagerText = false
        }
        if ((previewBean.showPosition + 1) > previewBean.sourceList.getSize()) {// 校验默认位置参数
            previewBean.showPosition = 0
        }

        PicturePreviewActivity.start(context, previewBean, flags)
    }

}