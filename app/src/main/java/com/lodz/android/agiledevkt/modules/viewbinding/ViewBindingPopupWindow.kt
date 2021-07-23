package com.lodz.android.agiledevkt.modules.viewbinding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.lodz.android.agiledevkt.databinding.PopupViewBindingBinding
import com.lodz.android.pandora.widget.popup.BasePopupWindow

/**
 * ViewBinding测试类
 * @author zhouL
 * @date 2021/7/23
 */
class ViewBindingPopupWindow(context: Context) : BasePopupWindow(context) {

    private lateinit var mBinding: PopupViewBindingBinding

    override fun getLayoutView(): View {
        mBinding = PopupViewBindingBinding.inflate(LayoutInflater.from(getContext()))
        return  mBinding.root
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.closeBtn.setOnClickListener {
            getPopup().dismiss()
        }
    }

}