package com.lodz.android.pandora.widget.watermark

import android.app.Activity
import android.app.Dialog
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.lodz.android.pandora.base.application.BaseApplication
import com.lodz.android.pandora.widget.dialogfragment.BaseDialogFragment

/**
 * 水印工具类
 * @author zhouL
 * @date 2023/6/9
 */

/** 在Activity上添加水印 */
fun Activity.addWatermark(text: String = ""): Activity {
    val content = text.ifEmpty { BaseApplication.get()?.getWatermarkContent() ?: "" }
    if (content.isEmpty()) {
        return this
    }

    val root = window?.decorView ?: return this
    if (root is ViewGroup) {
        val view = WatermarkView(applicationContext)
        view.setText(content)
        root.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }
    return this
}

/** 在Dialog上添加水印 */
fun Dialog.addWatermark(text: String = ""): Dialog {
    setOnShowListener {
        val content = text.ifEmpty { BaseApplication.get()?.getWatermarkContent() ?: "" }
        if (content.isEmpty()) {
            return@setOnShowListener
        }

        val view = WatermarkView(context)
        view.setText(content)

        val root = window?.decorView as? ViewGroup ?: return@setOnShowListener
        val contentView = root.findViewById<ViewGroup>(android.R.id.content) ?: return@setOnShowListener
        contentView.addView(view, contentView.width, contentView.height)
    }
    return this
}

/** 在DialogFragment上添加水印 */
fun BaseDialogFragment.addWatermark(text: String = ""): BaseDialogFragment {
    setOnDialogCreatedListener {
        it.addWatermark(text)
    }
    return this
}

/** 在DialogFragment上添加水印 */
fun PopupWindow.addWatermark(text: String = ""): PopupWindow {
    this.contentView.viewTreeObserver.addOnGlobalLayoutListener {
        val content = text.ifEmpty { BaseApplication.get()?.getWatermarkContent() ?: "" }
        if (content.isEmpty()) {
            return@addOnGlobalLayoutListener
        }
        val root = contentView as? FrameLayout ?: return@addOnGlobalLayoutListener
        if (root.tag == root.javaClass.name) {//已经添加过了
            return@addOnGlobalLayoutListener
        }
        val view = WatermarkView(root.context)
        view.setText(content)
        root.addView(view, root.width, root.height)
        root.tag = root.javaClass.name
    }
    return this
}

