package com.lodz.android.agiledevkt.modules.anko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.base.activity.UseAnkoLayout
import com.lodz.android.corekt.utils.toastShort
import org.jetbrains.anko.*

/**
 * AnkoLayout测试类
 * Created by zhouL on 2018/6/28.
 */
@UseAnkoLayout
class AnkoLayoutActivity : BaseActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, AnkoLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mAccountEdit: EditText
    private lateinit var mPswdEdit: EditText
    private lateinit var mLloginBtn: Button

    override fun getLayoutId() = 0

    override fun findViews(savedInstanceState: Bundle?) {
        verticalLayout {
            padding = dip(30)
            mAccountEdit = editText {
                hint = "Account"
                textSize = 24f
            }
            mPswdEdit = editText {
                hint = "Password"
                textSize = 24f
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mLloginBtn = button("Login") {
                textSize = 26f
            }
        }
    }

    override fun setListeners() {
        super.setListeners()
        mLloginBtn.setOnClickListener { view ->
            if (mAccountEdit.text.isEmpty()){
                MainActivity@ this.toastShort("您尚未输入账号")
                return@setOnClickListener
            }
            if (mPswdEdit.text.isEmpty()){
                MainActivity@ this.toastShort("您尚未输入密码")
                return@setOnClickListener
            }
            MainActivity@ this.toastShort(mAccountEdit.text.toString() + "    " + mPswdEdit.text.toString())
        }
    }
}