package com.lodz.android.agiledevkt.modules.selector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivitySelectorTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.utils.SelectorUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy

/**
 * 背景选择器测试类
 * Created by zhouL on 2018/7/19.
 */
class SelectorTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SelectorTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 正常背景 */
    private val BG_NORMAL = R.color.color_00a0e9
    /** 按压背景 */
    private val BG_PRESSED = R.color.color_00796b
    /** 不可用背景 */
    private val BG_UNABLE = R.color.color_d9d9d9
    /** 选中背景 */
    private val BG_SELECTED = R.color.color_ea5e5e
    /** 焦点背景 */
    private val BG_FOCUSED = R.color.color_ff00ff

    /** 文字正常颜色 */
    private val TEXT_NORMAL = R.color.white
    /** 文字按压颜色 */
    private val TEXT_PRESSED = R.color.color_b3e5fc
    /** 文字不可用颜色 */
    private val TEXT_UNABLE = R.color.color_9a9a9a
    /** 文字选中颜色 */
    private val TEXT_SELECTED = R.color.color_191919
    /** 文字焦点颜色 */
    private val TEXT_FOCUSED = R.color.color_ffa630

    private val mBinding: ActivitySelectorTestBinding by bindingLayoutLazy(ActivitySelectorTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 禁用开关
        mBinding.unableSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            setAllBtnEnabled(!isChecked)
        }

        // 选中开关
        mBinding.selectSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            setAllBtnSelected(isChecked)
        }

        // 焦点开关
        mBinding.focusedSwitchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            setAllBtnFocusable(isChecked)
        }
    }

    /** 设置所有按钮是否启用[isEnabled] */
    private fun setAllBtnEnabled(isEnabled: Boolean) {
        mBinding.pressedBtn.isEnabled = isEnabled
        mBinding.selectedBtn.isEnabled = isEnabled
        mBinding.unableBtn.isEnabled = isEnabled
        mBinding.focusedBtn.isEnabled = isEnabled
        mBinding.pressedUnableBtn.isEnabled = isEnabled
        mBinding.pressedSelectedBtn.isEnabled = isEnabled
        mBinding.pressedFocusedBtn.isEnabled = isEnabled
        mBinding.unableSelectedBtn.isEnabled = isEnabled
        mBinding.unableFocusedBtn.isEnabled = isEnabled
        mBinding.selectedFocusedBtn.isEnabled = isEnabled
        mBinding.pressedUnableSelectedBtn.isEnabled = isEnabled
        mBinding.pressedUnableFocusedBtn.isEnabled = isEnabled
        mBinding.unableSelectedFocusedBtn.isEnabled = isEnabled
        mBinding.pressedSelectedFocusedBtn.isEnabled = isEnabled
        mBinding.pressedUnableSelectedFocusedBtn.isEnabled = isEnabled
    }

    /** 设置所有按钮是否选中[isSelected] */
    private fun setAllBtnSelected(isSelected: Boolean) {
        mBinding.pressedBtn.isSelected = isSelected
        mBinding.selectedBtn.isSelected = isSelected
        mBinding.unableBtn.isSelected = isSelected
        mBinding.focusedBtn.isSelected = isSelected
        mBinding.pressedUnableBtn.isSelected = isSelected
        mBinding.pressedSelectedBtn.isSelected = isSelected
        mBinding.pressedFocusedBtn.isSelected = isSelected
        mBinding.unableSelectedBtn.isSelected = isSelected
        mBinding.unableFocusedBtn.isSelected = isSelected
        mBinding.selectedFocusedBtn.isSelected = isSelected
        mBinding.pressedUnableSelectedBtn.isSelected = isSelected
        mBinding.pressedUnableFocusedBtn.isSelected = isSelected
        mBinding.unableSelectedFocusedBtn.isSelected = isSelected
        mBinding.pressedSelectedFocusedBtn.isSelected = isSelected
        mBinding.pressedUnableSelectedFocusedBtn.isSelected = isSelected
    }

    /** 设置所有按钮是否拥有焦点[isFocusable] */
    private fun setAllBtnFocusable(isFocusable: Boolean) {

        // 焦点和选中一起打开时会优先展示选中的样式
        mBinding.focusedBtn.isFocusable = isFocusable
        mBinding.focusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.pressedFocusedBtn.isFocusable = isFocusable
        mBinding.pressedFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.unableFocusedBtn.isFocusable = isFocusable
        mBinding.unableFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.selectedFocusedBtn.isFocusable = isFocusable
        mBinding.selectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.pressedUnableFocusedBtn.isFocusable = isFocusable
        mBinding.pressedUnableFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.unableSelectedFocusedBtn.isFocusable = isFocusable
        mBinding.unableSelectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.pressedSelectedFocusedBtn.isFocusable = isFocusable
        mBinding.pressedSelectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mBinding.pressedUnableSelectedFocusedBtn.isFocusable = isFocusable
        mBinding.pressedUnableSelectedFocusedBtn.isFocusableInTouchMode = isFocusable
    }

    override fun initData() {
        super.initData()

        // 正常+按压
        mBinding.pressedBtn.background = SelectorUtils.createBgPressedColor(getContext(), BG_NORMAL, BG_PRESSED)
        mBinding.pressedBtn.setTextColor(SelectorUtils.createTxPressedColor(getContext(), TEXT_NORMAL, TEXT_PRESSED))

        // 正常+选中
        mBinding.selectedBtn.background = SelectorUtils.createBgSelectedColor(getContext(), BG_NORMAL, BG_SELECTED)
        mBinding.selectedBtn.setTextColor(SelectorUtils.createTxSelectedColor(getContext(), TEXT_NORMAL, TEXT_SELECTED))

        // 正常+不可用
        mBinding.unableBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE)
        mBinding.unableBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE))

        // 正常+焦点
        mBinding.focusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, focused = BG_FOCUSED)
        mBinding.focusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, focused = TEXT_FOCUSED))

        // 正常+按压+不可用
        mBinding.pressedUnableBtn.background = SelectorUtils.createBgPressedUnableColor(getContext(), BG_NORMAL, BG_PRESSED, BG_UNABLE)
        mBinding.pressedUnableBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(getContext(), TEXT_NORMAL, TEXT_PRESSED, TEXT_UNABLE))

        // 正常+按压+选中
        mBinding.pressedSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, selected = BG_SELECTED)
        mBinding.pressedSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, selected = TEXT_SELECTED))

        // 正常+按压+焦点
        mBinding.pressedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, focused = BG_FOCUSED)
        mBinding.pressedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, focused = TEXT_FOCUSED))

        // 正常+不可用+选中
        mBinding.unableSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, selected = BG_SELECTED)
        mBinding.unableSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, selected = TEXT_SELECTED))

        // 正常+不可用+焦点
        mBinding.unableFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, focused = BG_FOCUSED)
        mBinding.unableFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, focused = TEXT_FOCUSED))

        // 正常+选中+焦点
        mBinding.selectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, selected = BG_SELECTED, focused = BG_FOCUSED)
        mBinding.selectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+不可用+选中
        mBinding.pressedUnableSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, unable = BG_UNABLE, selected = BG_SELECTED)
        mBinding.pressedUnableSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, unable = TEXT_UNABLE, selected = TEXT_SELECTED))

        // 正常+按压+不可用+焦点
        mBinding.pressedUnableFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, unable = BG_UNABLE, focused = BG_FOCUSED)
        mBinding.pressedUnableFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, unable = TEXT_UNABLE, focused = TEXT_FOCUSED))

        // 正常+不可用+选中+焦点
        mBinding.unableSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, selected = BG_SELECTED, focused = BG_FOCUSED)
        mBinding.unableSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+选中+焦点
        mBinding.pressedSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, selected = BG_SELECTED, focused = BG_FOCUSED)
        mBinding.pressedSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+不可用+选中+焦点
        mBinding.pressedUnableSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), BG_NORMAL, BG_PRESSED, BG_UNABLE, BG_SELECTED, BG_FOCUSED)
        mBinding.pressedUnableSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), TEXT_NORMAL, TEXT_PRESSED, TEXT_UNABLE, TEXT_SELECTED, TEXT_FOCUSED))

        showStatusCompleted()
    }
}