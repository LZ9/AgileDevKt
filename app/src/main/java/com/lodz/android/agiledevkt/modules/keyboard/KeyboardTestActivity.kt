package com.lodz.android.agiledevkt.modules.keyboard

import android.content.Context
import android.content.Intent
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.camera.CameraMainActivity
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.utils.keyboard.KeyboardManager
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView
import kotlinx.android.synthetic.main.toast_custom.*

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
    private lateinit var mIdcardKeyboardManager: KeyboardManager
    /** 身份证键盘 */
    private val mIdcardKeyboardView by bindView<KeyboardView>(R.id.idcard_keyboard_view)
    /** 车牌 */
    private val mCarplateCltedt by bindView<CltEditView>(R.id.carplate_cltedt)
    /** 车牌键盘 */
    private lateinit var mCarplateKeyboardManager: KeyboardManager
    /** 车牌键盘 */
    private val mCarplateKeyboardView by bindView<KeyboardView>(R.id.carplate_keyboard_view)
    /** 全键盘 */
    private val mCommonCertCltedt by bindView<CltEditView>(R.id.common_cert_cltedt)
    /** 全键盘 */
    private lateinit var mCommonCertKeyboardManager: KeyboardManager
    /** 全键盘 */
    private val mCommonCertKeyboardView by bindView<KeyboardView>(R.id.common_cert_keyboard_view)

    /** 默认 */
    private val mDefaultCltedt by bindView<CltEditView>(R.id.default_cltedt)

    override fun getLayoutId(): Int = R.layout.activity_keyboard_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        mIdcardKeyboardManager = createKeyboard(mIdcardCltedt.getEditText(), mIdcardKeyboardView, KeyboardManager.TYPE_ID_CARD)
        mCarplateKeyboardManager = createKeyboard(mCarplateCltedt.getEditText(), mCarplateKeyboardView, KeyboardManager.TYPE_CAR_NUM)
        mCommonCertKeyboardManager = createKeyboard(mCommonCertCltedt.getEditText(), mCommonCertKeyboardView, KeyboardManager.TYPE_COMMON_CERT)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mIdcardKeyboardManager.setOnClickFinishListener{
            mIdcardKeyboardManager.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        mCarplateKeyboardManager.setOnClickFinishListener{
            mCarplateKeyboardManager.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }

        mCommonCertKeyboardManager.setOnClickFinishListener{
            mCommonCertKeyboardManager.goneKeyboard()
            if (it.isNotEmpty()){
                toastShort(it)
            }
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun createKeyboard(editText: EditText, keyboardView: KeyboardView, keyboardType: Int): KeyboardManager {
        val keyboardManager = KeyboardManager(editText.context, window, keyboardView, editText, keyboardType)
        keyboardManager.goneKeyboard()
        return keyboardManager
    }
}