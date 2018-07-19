package com.lodz.android.agiledevkt.modules.selector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.corekt.utils.SelectorUtils

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
    private val BG_PRESSED = R.color.color_2f6dc9
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

    /** 禁用开关 */
    @BindView(R.id.unable_switch_btn)
    lateinit var mUnableSwitchBtn: Switch
    /** 选中开关 */
    @BindView(R.id.select_switch_btn)
    lateinit var mSelectSwitchBtn: Switch
    /** 焦点开关 */
    @BindView(R.id.focused_switch_btn)
    lateinit var mFocusedSwitchBtn: Switch

    /** 正常+按压 */
    @BindView(R.id.pressed_btn)
    lateinit var mPressedBtn: Button
    /** 正常+选中 */
    @BindView(R.id.selected_btn)
    lateinit var mSelectedBtn: Button
    /** 正常+不可用 */
    @BindView(R.id.unable_btn)
    lateinit var mUnableBtn: Button
    /** 正常+焦点 */
    @BindView(R.id.focused_btn)
    lateinit var mFocusedBtn: Button

    /** 正常+按压+不可用 */
    @BindView(R.id.pressed_unable_btn)
    lateinit var mPressedUnableBtn: Button
    /** 正常+按压+选中 */
    @BindView(R.id.pressed_selected_btn)
    lateinit var mPressedSelectedBtn: Button
    /** 正常+按压+焦点 */
    @BindView(R.id.pressed_focused_btn)
    lateinit var mPressedFocusedBtn: Button
    /** 正常+不可用+选中 */
    @BindView(R.id.unable_selected_btn)
    lateinit var mUnableSelectedBtn: Button
    /** 正常+不可用+焦点 */
    @BindView(R.id.unable_focused_btn)
    lateinit var mUnableFocusedBtn: Button
    /** 正常+选中+焦点 */
    @BindView(R.id.selected_focused_btn)
    lateinit var mSelectedFocusedBtn: Button

    /** 正常+按压+不可用+选中 */
    @BindView(R.id.pressed_unable_selected_btn)
    lateinit var mPressedUnableSelectedBtn: Button
    /** 正常+按压+不可用+焦点 */
    @BindView(R.id.pressed_unable_focused_btn)
    lateinit var mPressedUnableFocusedBtn: Button
    /** 正常+不可用+选中+焦点 */
    @BindView(R.id.unable_selected_focused_btn)
    lateinit var mUnableSelectedFocusedBtn: Button
    /** 正常+按压+选中+焦点 */
    @BindView(R.id.pressed_selected_focused_btn)
    lateinit var mPressedSelectedFocusedBtn: Button

    /** 正常+按压+不可用+选中+焦点 */
    @BindView(R.id.pressed_unable_selected_focused_btn)
    lateinit var mPressedUnableSelectedFocusedBtn: Button


    override fun getLayoutId() = R.layout.activity_selector_test

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun clickBackBtn() {
        super.clickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 禁用开关
        mUnableSwitchBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                setAllBtnEnabled(!isChecked)
            }
        })

        // 选中开关
        mSelectSwitchBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                setAllBtnSelected(isChecked)
            }
        })

        // 焦点开关
        mFocusedSwitchBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                setAllBtnFocusable(isChecked)
            }
        })
    }

    /** 设置所有按钮是否启用[isEnabled] */
    private fun setAllBtnEnabled(isEnabled: Boolean) {
        mPressedBtn.isEnabled = isEnabled
        mSelectedBtn.isEnabled = isEnabled
        mUnableBtn.isEnabled = isEnabled
        mFocusedBtn.isEnabled = isEnabled
        mPressedUnableBtn.isEnabled = isEnabled
        mPressedSelectedBtn.isEnabled = isEnabled
        mPressedFocusedBtn.isEnabled = isEnabled
        mUnableSelectedBtn.isEnabled = isEnabled
        mUnableFocusedBtn.isEnabled = isEnabled
        mSelectedFocusedBtn.isEnabled = isEnabled
        mPressedUnableSelectedBtn.isEnabled = isEnabled
        mPressedUnableFocusedBtn.isEnabled = isEnabled
        mUnableSelectedFocusedBtn.isEnabled = isEnabled
        mPressedSelectedFocusedBtn.isEnabled = isEnabled
        mPressedUnableSelectedFocusedBtn.isEnabled = isEnabled
    }

    /** 设置所有按钮是否选中[isSelected] */
    private fun setAllBtnSelected(isSelected: Boolean) {
        mPressedBtn.isSelected = isSelected
        mSelectedBtn.isSelected = isSelected
        mUnableBtn.isSelected = isSelected
        mFocusedBtn.isSelected = isSelected
        mPressedUnableBtn.isSelected = isSelected
        mPressedSelectedBtn.isSelected = isSelected
        mPressedFocusedBtn.isSelected = isSelected
        mUnableSelectedBtn.isSelected = isSelected
        mUnableFocusedBtn.isSelected = isSelected
        mSelectedFocusedBtn.isSelected = isSelected
        mPressedUnableSelectedBtn.isSelected = isSelected
        mPressedUnableFocusedBtn.isSelected = isSelected
        mUnableSelectedFocusedBtn.isSelected = isSelected
        mPressedSelectedFocusedBtn.isSelected = isSelected
        mPressedUnableSelectedFocusedBtn.isSelected = isSelected
    }

    /** 设置所有按钮是否拥有焦点[isFocusable] */
    private fun setAllBtnFocusable(isFocusable: Boolean) {

        // 焦点和选中一起打开时会优先展示选中的样式

        mFocusedBtn.isFocusable = isFocusable
        mFocusedBtn.isFocusableInTouchMode = isFocusable

        mPressedFocusedBtn.isFocusable = isFocusable
        mPressedFocusedBtn.isFocusableInTouchMode = isFocusable

        mUnableFocusedBtn.isFocusable = isFocusable
        mUnableFocusedBtn.isFocusableInTouchMode = isFocusable

        mSelectedFocusedBtn.isFocusable = isFocusable
        mSelectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mPressedUnableFocusedBtn.isFocusable = isFocusable
        mPressedUnableFocusedBtn.isFocusableInTouchMode = isFocusable

        mUnableSelectedFocusedBtn.isFocusable = isFocusable
        mUnableSelectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mPressedSelectedFocusedBtn.isFocusable = isFocusable
        mPressedSelectedFocusedBtn.isFocusableInTouchMode = isFocusable

        mPressedUnableSelectedFocusedBtn.isFocusable = isFocusable
        mPressedUnableSelectedFocusedBtn.isFocusableInTouchMode = isFocusable
    }

    override fun initData() {
        super.initData()

        // 正常+按压
        mPressedBtn.background = SelectorUtils.createBgPressedColor(getContext(), BG_NORMAL, BG_PRESSED)
        mPressedBtn.setTextColor(SelectorUtils.createTxPressedColor(getContext(), TEXT_NORMAL, TEXT_PRESSED))

        // 正常+选中
        mSelectedBtn.background = SelectorUtils.createBgSelectedColor(getContext(), BG_NORMAL, BG_SELECTED)
        mSelectedBtn.setTextColor(SelectorUtils.createTxSelectedColor(getContext(), TEXT_NORMAL, TEXT_SELECTED))

        // 正常+不可用
        mUnableBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE)
        mUnableBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE))

        // 正常+焦点
        mFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, focused = BG_FOCUSED)
        mFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, focused = TEXT_FOCUSED))

        // 正常+按压+不可用
        mPressedUnableBtn.background = SelectorUtils.createBgPressedUnableColor(getContext(), BG_NORMAL, BG_PRESSED, BG_UNABLE)
        mPressedUnableBtn.setTextColor(SelectorUtils.createTxPressedUnableColor(getContext(), TEXT_NORMAL, TEXT_PRESSED, TEXT_UNABLE))

        // 正常+按压+选中
        mPressedSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, selected = BG_SELECTED)
        mPressedSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, selected = TEXT_SELECTED))

        // 正常+按压+焦点
        mPressedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, focused = BG_FOCUSED)
        mPressedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, focused = TEXT_FOCUSED))

        // 正常+不可用+选中
        mUnableSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, selected = BG_SELECTED)
        mUnableSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, selected = TEXT_SELECTED))

        // 正常+不可用+焦点
        mUnableFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, focused = BG_FOCUSED)
        mUnableFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, focused = TEXT_FOCUSED))

        // 正常+选中+焦点
        mSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, selected = BG_SELECTED, focused = BG_FOCUSED)
        mSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+不可用+选中
        mPressedUnableSelectedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, unable = BG_UNABLE, selected = BG_SELECTED)
        mPressedUnableSelectedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, unable = TEXT_UNABLE, selected = TEXT_SELECTED))

        // 正常+按压+不可用+焦点
        mPressedUnableFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, unable = BG_UNABLE, focused = BG_FOCUSED)
        mPressedUnableFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, unable = TEXT_UNABLE, focused = TEXT_FOCUSED))

        // 正常+不可用+选中+焦点
        mUnableSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, unable = BG_UNABLE, selected = BG_SELECTED, focused = BG_FOCUSED)
        mUnableSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, unable = TEXT_UNABLE, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+选中+焦点
        mPressedSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), normal = BG_NORMAL, pressed = BG_PRESSED, selected = BG_SELECTED, focused = BG_FOCUSED)
        mPressedSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), normal = TEXT_NORMAL, pressed = TEXT_PRESSED, selected = TEXT_SELECTED, focused = TEXT_FOCUSED))

        // 正常+按压+不可用+选中+焦点
        mPressedUnableSelectedFocusedBtn.background = SelectorUtils.createBgSelectorColor(getContext(), BG_NORMAL, BG_PRESSED, BG_UNABLE, BG_SELECTED, BG_FOCUSED)
        mPressedUnableSelectedFocusedBtn.setTextColor(SelectorUtils.createTxSelectorColor(getContext(), TEXT_NORMAL, TEXT_PRESSED, TEXT_UNABLE, TEXT_SELECTED, TEXT_FOCUSED))

        showStatusCompleted()
    }
}