package com.lodz.android.agiledevkt.modules.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.databinding.ActivityEncryptBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.security.AES
import com.lodz.android.corekt.security.MD5
import com.lodz.android.corekt.security.RSA
import com.lodz.android.corekt.security.SHA1
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
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

    private val mBinding: ActivityEncryptBinding by bindingLayout(ActivityEncryptBinding::inflate)

    /** AES秘钥 */
    private var mAESKey = ""
    /** RSA私钥 */
    private var mRSAPrivateKey = ""
    /** RSA公钥 */
    private var mRSAPublicKey = ""

    /** 内容 */
    private var mContent: String? = ""

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

        // AES秘钥初始化
        mBinding.aesInitBtn.setOnClickListener {
            mAESKey = System.currentTimeMillis().toString() + (Random().nextInt(899) + 100)
            mContent = ""
            printResult("生成AES秘钥：" + mAESKey + "         " + mAESKey.length + "位")
        }

        // AES加密
        mBinding.aesEncryptBtn.setOnClickListener {
            if (mAESKey.isEmpty()) {
                toastShort("您尚未初始化AES秘钥")
                return@setOnClickListener
            }
            if (mBinding.inputEdit.text.isEmpty()) {
                toastShort("您尚未输入加密内容")
                return@setOnClickListener
            }
            mContent = AES.encrypt(mBinding.inputEdit.text.toString(), mAESKey)
            printResult(if (mContent == null) "加密失败" else "密文：$mContent")
        }

        // AES解密
        mBinding.aesDecryptBtn.setOnClickListener {
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
        mBinding.rsaInitBtn.setOnClickListener {
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
        mBinding.rsaEncryptBtn.setOnClickListener {
            if (mRSAPublicKey.isEmpty()) {
                toastShort("您尚未初始化RSA公钥")
                return@setOnClickListener
            }
            if (mBinding.inputEdit.text.isEmpty()) {
                toastShort("您尚未输入加密内容")
                return@setOnClickListener
            }
            mContent = RSA.encryptByPublicKey(mBinding.inputEdit.text.toString(), mRSAPublicKey)
            printResult(if (mContent == null) "加密失败" else "密文：$mContent")
        }

        // RSA私钥解密
        mBinding.rsaDecryptBtn.setOnClickListener {
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
        mBinding.md5Btn.setOnClickListener {
            if (mBinding.inputEdit.text.isEmpty()) {
                toastShort("您尚未输入内容")
                return@setOnClickListener
            }
            val result = MD5.md(mBinding.inputEdit.text.toString())
            printResult(if (result == null) "信息摘要失败" else "MD5信息摘要：$result")
        }

        // SHA1信息摘要
        mBinding.sha1Btn.setOnClickListener {
            if (mBinding.inputEdit.text.isEmpty()) {
                toastShort("您尚未输入内容")
                return@setOnClickListener
            }
            val result = SHA1.md(mBinding.inputEdit.text.toString())
            printResult(if (result == null) "信息摘要失败" else "SHA1信息摘要：$result")
        }

        // 清空
        mBinding.cleanBtn.setOnClickListener {
            mBinding.resultTv.text = ""
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun printResult(result: String) {
        mBinding.resultTv.text = result.append("\n").append(mBinding.resultTv.text.toString())
    }

}