package com.lodz.android.agiledevkt.modules.viewbinding

import android.content.Context
import android.view.View
import com.lodz.android.agiledevkt.databinding.PopupViewBindingBinding
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.popup.BasePopupWindow

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/23
 */
class ViewBindingPopupWindow(context: Context) : BasePopupWindow(context) {

    private val mBinding: PopupViewBindingBinding by context.bindingLayout(PopupViewBindingBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()
        mBinding.closeBtn.setOnClickListener {
            getPopup().dismiss()
        }
    }

}