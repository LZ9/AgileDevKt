package com.lodz.android.agiledevkt.modules.keyboard

import android.content.Context
import android.content.Intent
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.widget.EditText
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.keyboard.CarPlateKeyboardView
import com.lodz.android.pandora.widget.keyboard.CommonCertKeyboardView
import com.lodz.android.pandora.widget.keyboard.IdcardKeyboardView

/**
 * 自定义键盘测试类
 * @author zhouL
 * @date 2021/2/22
 */
class KeyboardTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, KeyboardTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 身份证 */
    private val mIdcardCltedt by bindView<CltEditView>(R.id.idcard_cltedt)
    /** 身份证键盘 */
    private val mIdcardKeyboardView by bindView<IdcardKeyboardView>(R.id.idcard_keyboard_view)
    /** 车牌 */
    private val mCarplateCltedt by bindView<CltEditView>(R.id.carplate_cltedt)
    /** 车牌键盘 */
    private val mCarplateKeyboardView by bindView<CarPlateKeyboardView>(R.id.carplate_keyboard_view)
    /** 全键盘 */
    private val mCommonCertCltedt by bindView<CltEditView>(R.id.common_cert_cltedt)
    /** 全键盘 */
    private val mCommonCertKeyboardView by bindView<CommonCertKeyboardView>(R.id.common_cert_keyboard_view)

    /** 默认 */
    private val mDefaultCltedt by bindView<CltEditView>(R.id.default_cltedt)

    override fun getLayoutId(): Int = R.layout.activity_keyboard_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mIdcardKeyboardView.initView(window, mIdcardCltedt.getEditText())
        mCarplateKeyboardView.initView(window, mCarplateCltedt.getEditText())
        mCommonCertKeyboardView.initView(window, mCommonCertCltedt.getEditText())
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mIdcardKeyboardView.setOnClickFinishListener{
            mIdcardKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        mCarplateKeyboardView.setOnClickFinishListener{
            mCarplateKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        mCommonCertKeyboardView.setOnClickFinishListener{
            mCommonCertKeyboardView.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}