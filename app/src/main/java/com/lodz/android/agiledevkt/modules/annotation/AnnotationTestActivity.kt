package com.lodz.android.agiledevkt.modules.annotation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.security.AES
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView

/*
    java中元注解有四个： @Retention @Target @Document @Inherited；

　　 @Retention：注解的保留位置　　　　　　　　　
        @Retention(AnnotationRetention.RUNTIME)   // 默认的保留策略，注解仅存在于源码中，不存储在编译后的Class文件。
        @Retention(AnnotationRetention.BINARY)    // 存储在编译后的Class文件，但是反射不可见。
        @Retention(AnnotationRetention.RUNTIME)   // 存储在编译后的Class文件，反射可见。

　　@Target:注解的作用目标
        @Target(AnnotationTarget.CLASS) //类，接口或对象，注解类也包括在内。
        @Target(AnnotationTarget.ANNOTATION_CLASS) //只有注解类
        @Target(AnnotationTarget.TYPE_PARAMETER) //通用类型参数（还不支持）
        @Target(AnnotationTarget.PROPERTY) //属性
        @Target(AnnotationTarget.FIELD) //字段，包括属性的支持字段
        @Target(AnnotationTarget.LOCAL_VARIABLE) //局部变量
        @Target(AnnotationTarget.VALUE_PARAMETER) //函数或构造函数的值参数
        @Target(AnnotationTarget.CONSTRUCTOR) //仅构造函数（主函数或者第二函数）
        @Target(AnnotationTarget.FUNCTION) //方法（不包括构造函数）
        @Target(AnnotationTarget.PROPERTY_GETTER) //只有属性的getter
        @Target(AnnotationTarget.PROPERTY_SETTER) //只有属性的setter
        @Target(AnnotationTarget.TYPE) //类型使用
        @Target(AnnotationTarget.EXPRESSION) //任何表达式
        @Target(AnnotationTarget.FILE) //文件
        @Target(AnnotationTarget.TYPEALIAS) //类型别名，Kotlin1.1已可用

    @MustBeDocumented：说明该注解将被包含在文档中

　  @Repeatable：说明该注释在同个位置可以使用多次
 */


/**
 * 注解测试类
 * @author zhouL
 * @date 2019/5/28
 */
class AnnotationTestActivity : BaseActivity() {

    companion object {
        const val AES_KEY = "asdcxzqwer123456"
        fun start(context: Context) {
            val intent = Intent(context, AnnotationTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 注解内容展示 */
    private val mContentTv by bindView<TextView>(R.id.content_tv)
    /** 内容输入框 */
    private val mContentCltedit by bindView<CltEditView>(R.id.content_cltedit)
    /** 注解使用按钮 */
    private val mAnnotationBtn by bindView<MaterialButton>(R.id.annotation_btn)
    /** 解密按钮 */
    @BindViews(R.id.decrypt_btn)
    private lateinit var mDecryptBtn : MaterialButton

    /** 内容 */
    @EncryptionAES(AES_KEY)
    private var mContent = ""

    override fun getLayoutId(): Int = R.layout.activity_annotation_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        AnnotationTestUtils.bind(this)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun setListeners() {
        super.setListeners()

        mAnnotationBtn.setOnClickListener {
            mContent = mContentCltedit.getContentText()
            if (mContent.isEmpty()) {
                toastShort(R.string.annotation_content_hint)
                return@setOnClickListener
            }

            AnnotationTestUtils.injectEncryption(this)
            mContentTv.text = mContent
        }

        mDecryptBtn.setOnClickListener {
            val content = mContentTv.text.toString()
            if (content.isNotEmpty()) {
                val result = AES.decrypt(content, AES_KEY) ?: ""
                mContentTv.text = result
            }
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}