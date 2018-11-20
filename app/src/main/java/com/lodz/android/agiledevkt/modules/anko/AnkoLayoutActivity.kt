package com.lodz.android.agiledevkt.modules.anko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.base.activity.UseAnkoLayout
import com.lodz.android.corekt.utils.toastShort
import org.jetbrains.anko.*

/**
 * AnkoLayout测试类
 * Created by zhouL on 2018/6/28.
 */
@UseAnkoLayout
class AnkoLayoutActivity : AbsActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AnkoLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mAccountEdit: EditText
    private lateinit var mPswdEdit: EditText
    private lateinit var mLloginBtn: Button
    private lateinit var mResultTv: TextView

    override fun getAbsLayoutId(): Int = 0

    override fun findViews(savedInstanceState: Bundle?) {
        verticalLayout {
            padding = dip(30)
            mAccountEdit = editText {
                id = android.R.id.edit
                setHint(R.string.al_account)
                textSize = 24f
            }
            mPswdEdit = editText {
                id = android.R.id.inputExtractEditText
                setHint(R.string.al_pswd)
                textSize = 24f
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mLloginBtn = button(R.string.al_login) {
                id = android.R.id.button1
                textSize = 26f
            }
            mResultTv = textView {
                id = android.R.id.text1
                text = "1"
                textSize = 24f
            }
        }
    }

    override fun setListeners() {
        super.setListeners()
        mLloginBtn.setOnClickListener { view ->
            if (mAccountEdit.text.isEmpty()) {
                MainActivity@ this.toastShort(R.string.al_account_empty)
                return@setOnClickListener
            }
            if (mPswdEdit.text.isEmpty()) {
                MainActivity@ this.toastShort(R.string.al_pswd_empty)
                return@setOnClickListener
            }
            mResultTv.text = "账号：${mAccountEdit.text} ; 密码：${mPswdEdit.text}"
            FragmentChangeActivity.start(getContext(), mAccountEdit.text.toString(), mPswdEdit.text.toString())
        }
    }
}