package com.lodz.android.agiledevkt.modules.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.security.AES
import com.lodz.android.corekt.security.MD5
import com.lodz.android.corekt.security.RSA
import com.lodz.android.corekt.security.SHA1
import com.lodz.android.pandora.base.activity.BaseActivity
import kotlinx.coroutines.GlobalScope
import java.util.*

/**
 * 加密测试类
 * Created by zhouL on 2018/6/29.
 */
class EncryptTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, EncryptTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 滚动控件 */
    private val mScrollView by bindView<ScrollView>(R.id.scroll_view)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result)

    /** 输入框 */
    private val mInputEdit by bindView<EditText>(R.id.input_edit)

    /** AES秘钥初始化 */
    private val mAESInitBtn by bindView<Button>(R.id.aes_init_btn)
    /** AES加密 */
    private val mAESEncryptBtn by bindView<Button>(R.id.aes_encrypt_btn)
    /** AES解密 */
    private val mAESDecryptBtn by bindView<Button>(R.id.aes_decrypt_btn)
    /** RSA秘钥初始化 */
    private val mRsaInitBtn by bindView<Button>(R.id.rsa_init_btn)
    /** RSA公钥加密 */
    private val mRsaEncryptBtn by bindView<Button>(R.id.rsa_encrypt_btn)
    /** RSA私钥解密 */
    private val mRsaDecryptBtn by bindView<Button>(R.id.rsa_decrypt_btn)

    /** MD5信息摘要 */
    private val mMD5Btn by bindView<Button>(R.id.md5_btn)
    /** SHA1信息摘要 */
    private val mSHA1Btn by bindView<Button>(R.id.sha1_btn)

    /** 清空 */
    private val mCleanBtn by bindView<Button>(R.id.clean_btn)

    /** AES秘钥 */
    private var mAESKey = ""
    /** RSA私钥 */
    private var mRSAPrivateKey = ""
    /** RSA公钥 */
    private var mRSAPublicKey = ""

    /** 内容 */
    private var mContent: String? = ""

    override fun getLayoutId() = R.layout.activity_encrypt

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // AES秘钥初始化
        mAESInitBtn.setOnClickListener {
            mAESKey = System.currentTimeMillis().toString() + (Random().nextInt(899) + 100)
            mContent = ""
            printResult("生成AES秘钥：" + mAESKey + "         " + mAESKey.length + "位")
        }

        // AES加密
        mAESEncryptBtn.setOnClickListener {
            if (mAESKey.isEmpty()) {
                toastShort("您尚未初始化AES秘钥")
                return@setOnClickListener
            }
            if (mInputEdit.text.isEmpty()) {
                toastShort("您尚未输入加密内容")
                return@setOnClickListener
            }
            mContent = AES.encrypt(mInputEdit.text.toString(), mAESKey)
            printResult(if (mContent == null) "加密失败" else "密文：$mContent")
        }

        // AES解密
        mAESDecryptBtn.setOnClickListener {
            if (mAESKey.isEmpty()) {
                toastShort("您尚未初始化AES秘钥")
                return@setOnClickListener
            }
            if (mContent.isNullOrEmpty()) {
                toastShort("您尚未加密过数据")
                return@setOnClickListener
            }
            val result = AES.decrypt(mContent!!, mAESKey)
            printResult(if (result == null) "解密失败" else "原始内容：$result")
            mContent = ""
        }

        // RSA秘钥初始化
        mRsaInitBtn.setOnClickListener {
            mContent = ""
            try {
                val pair = RSA.initKeyBase64()
                mRSAPublicKey = pair.first
                mRSAPrivateKey = pair.second
                printResult("生成RSA私钥：$mRSAPrivateKey")
                printResult("生成RSA公钥：$mRSAPublicKey")
            } catch (e: Exception) {
                e.printStackTrace()
                mRSAPrivateKey = ""
                mRSAPublicKey = ""
                printResult("生成RSA秘钥失败")
            }
        }

        // RSA公钥加密
        mRsaEncryptBtn.setOnClickListener {
            if (mRSAPublicKey.isEmpty()) {
                toastShort("您尚未初始化RSA公钥")
                return@setOnClickListener
            }
            if (mInputEdit.text.isEmpty()) {
                toastShort("您尚未输入加密内容")
                return@setOnClickListener
            }
            mContent = RSA.encryptByPublicKey(mInputEdit.text.toString(), mRSAPublicKey)
            printResult(if (mContent == null) "加密失败" else "密文：$mContent")
        }

        // RSA私钥解密
        mRsaDecryptBtn.setOnClickListener {
            if (mRSAPrivateKey.isEmpty()) {
                toastShort("您尚未初始化RSA私钥")
                return@setOnClickListener
            }
            if (mContent.isNullOrEmpty()) {
                toastShort("您尚未加密过数据")
                return@setOnClickListener
            }
            val result = RSA.decryptByPrivateKey(mContent!!, mRSAPrivateKey)
            printResult(if (result == null) "解密失败" else "原始内容：$result")
            mContent = ""
        }

        // MD5信息摘要
        mMD5Btn.setOnClickListener {
            if (mInputEdit.text.isEmpty()) {
                toastShort("您尚未输入内容")
                return@setOnClickListener
            }
            val result = MD5.md(mInputEdit.text.toString())
            printResult(if (result == null) "信息摘要失败" else "MD5信息摘要：$result")
        }

        // SHA1信息摘要
        mSHA1Btn.setOnClickListener {
            if (mInputEdit.text.isEmpty()) {
                toastShort("您尚未输入内容")
                return@setOnClickListener
            }
            val result = SHA1.md(mInputEdit.text.toString())
            printResult(if (result == null) "信息摘要失败" else "SHA1信息摘要：$result")
        }

        // 清空
        mCleanBtn.setOnClickListener {
            mResultTv.text = ""
        }
    }


    override fun initData() {
        super.initData()
        showStatusCompleted()
    }


    private fun printResult(result: String) {
        mResultTv.text = (mResultTv.text.toString() + "\n" + result)
        GlobalScope.runOnMainDelay(100){
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

}