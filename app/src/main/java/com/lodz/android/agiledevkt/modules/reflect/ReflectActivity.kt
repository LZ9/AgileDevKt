package com.lodz.android.agiledevkt.modules.reflect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * 反射测试类
 * Created by zhouL on 2018/7/16.
 */
class ReflectActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ReflectActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val BEAN_PATH = "com.lodz.android.agiledevkt.modules.reflect.ReflectBean"

    /** 消息展示 */
    private val mMsgTv by bindView<TextView>(R.id.text_msg)
    /** 获取所有构造函数名称 */
    private val mConstructorNameBtn by bindView<Button>(R.id.get_constructor_name)
    /** 获取对象里的变量名称 */
    private val mFieldNameBtn by bindView<Button>(R.id.get_field_name)
    /** 获取方法名 */
    private val mMethodNameButton by bindView<Button>(R.id.get_method_name)
    /** 获取对象里变量的值 */
    private val mFieldValueBtn by bindView<Button>(R.id.get_field_value)
    /** 执行函数 */
    private val mExecuteFunctionBtn by bindView<Button>(R.id.execute_function)
    /** 设置值并读取测试 */
    private val mSetValueBtn by bindView<Button>(R.id.set_value)

    override fun getLayoutId() = R.layout.activity_reflect

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 获取所有构造函数名称
        mConstructorNameBtn.setOnClickListener {
            getConstructorName()
        }

        // 获取对象里的变量名称
        mFieldNameBtn.setOnClickListener {
            getFieldName()
        }

        // 获取方法名
        mMethodNameButton.setOnClickListener {
            getMethodName()
        }

        // 获取对象里变量的值
        mFieldValueBtn.setOnClickListener {
            getFieldValue()
        }

        // 执行函数
        mExecuteFunctionBtn.setOnClickListener {
            //            executeFunction()
            executeParamFunction()
        }

        // 设置值并读取测试
        mSetValueBtn.setOnClickListener {
            setValue()
        }

    }

    /** 获取所有构造函数名称 */
    private fun getConstructorName() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = ReflectUtils.getConstructorName(c)
        mMsgTv.text = (c.simpleName + "\n" + list.toString())
    }

    /** 获取对象里的变量名称 */
    private fun getFieldName() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = ReflectUtils.getFieldName(c)
        mMsgTv.text = (c.simpleName + "\n" + list.toString())
    }

    /** 获取方法名 */
    private fun getMethodName() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = ReflectUtils.getMethodName(c)
        mMsgTv.text = (c.simpleName + "\n" + list.toString())
    }

    /** 获取对象里变量的值 */
    private fun getFieldValue() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        var result = c.simpleName + "\n"
        val list = ReflectUtils.getFieldName(c)
        for (name in list) {
            val value = ReflectUtils.getFieldValue(c, constructor, name)
            if (value != null) {
                result += name + " : " + value.toString() + "\n"
            }
        }
        mMsgTv.text = result
    }

    /** 执行某个函数 */
    private fun executeFunction() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        var result = c.simpleName + "\n"
        val list = ReflectUtils.getMethodName(c)
        for (name in list) {
            val method = ReflectUtils.executeFunction(c, constructor, name)
            if (method != null) {
                result += method.toString() + "\n"
            }
        }
        mMsgTv.text = result
    }

    /** 执行某个有参函数 */
    private fun executeParamFunction() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = ReflectUtils.getConstructor(c, arrayOf(Int::class.java, String::class.java), arrayOf(21, "Japan"))
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        var result = c.simpleName + "\n"
        val method = ReflectUtils.executeFunction(c, constructor, "getAgeTest", arrayOf(Int::class.java), arrayOf(5))
        if (method != null) {
            result += method.toString() + "\n"
        }
        mMsgTv.text = result
    }

    /** 设置值 */
    private fun setValue() {
        val c = ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        var result = c.simpleName + "\n"
        val valueOld = ReflectUtils.getFieldValue(c, constructor, "name")
        if (valueOld != null) {
            result += "old name : " + valueOld.toString() + "\n"
        }
        val isSuccess = ReflectUtils.setFieldValue(c, constructor, "name", "Shaw")
        result += "is success : " + isSuccess.toString() + "\n"
        val valueNew = ReflectUtils.getFieldValue(c, constructor, "name")
        if (valueNew != null) {
            result += "new name : " + valueNew.toString() + "\n"
        }
        mMsgTv.text = result
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}