package com.lodz.android.agiledevkt.modules.reflect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
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
    private val mMsgTv by bindView<TextView>(R.id.text_msg_tv)
    /** 类型开关 */
    private val mTypeSwitch by bindView<SwitchMaterial>(R.id.type_switch)

    /** 获取Class */
    private val mGetClassBtn by bindView<Button>(R.id.get_class_btn)
    /** 获取所有构造函数名称 */
    private val mConstructorNameBtn by bindView<Button>(R.id.get_constructor_name_btn)
    /** 获取对象里的变量名称 */
    private val mFieldNameBtn by bindView<Button>(R.id.get_field_name_btn)
    /** 获取方法名 */
    private val mMethodNameButton by bindView<Button>(R.id.get_method_name_btn)
    /** 获取对象里变量的值 */
    private val mFieldValueBtn by bindView<Button>(R.id.get_field_value_btn)
    /** 执行无参函数 */
    private val mExecuteFunctionBtn by bindView<Button>(R.id.execute_function_btn)
    /** 执行有参函数 */
    private val mExecuteParamFunctionBtn by bindView<Button>(R.id.execute_param_function_btn)

    /** 设置值并读取测试 */
    private val mSetValueBtn by bindView<Button>(R.id.set_value_btn)

    override fun getLayoutId() = R.layout.activity_reflect

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 通过路径获取Class
        mGetClassBtn.setOnClickListener {
            getClassName()
        }

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

        // 执行无参函数
        mExecuteFunctionBtn.setOnClickListener {
            executeFunction()
        }

        // 执行有参函数
        mExecuteParamFunctionBtn.setOnClickListener {
            executeParamFunction()
        }

        // 设置值并读取测试
        mSetValueBtn.setOnClickListener {
            setValue()
        }

    }

    /** 通过路径获取Class */
    private fun getClassName() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        mMsgTv.text = c?.name ?: getString(R.string.reflect_unfind_class)
    }

    /** 获取所有构造函数名称 */
    private fun getConstructorName() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mTypeSwitch.isChecked) ReflectUtils.getConstructorName<ReflectBean>() else ReflectUtils.getConstructorName(c)
        val sb = StringBuilder()
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mMsgTv.text = sb
    }

    /** 获取对象里的变量名称 */
    private fun getFieldName() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mTypeSwitch.isChecked) ReflectUtils.getFieldName<ReflectBean>() else ReflectUtils.getFieldName(c)
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mMsgTv.text = sb
    }

    /** 获取方法名 */
    private fun getMethodName() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mTypeSwitch.isChecked) ReflectUtils.getMethodName<ReflectBean>() else ReflectUtils.getMethodName(c)
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mMsgTv.text = sb
    }

    /** 获取对象里变量的值 */
    private fun getFieldValue() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mTypeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val nameList = if (mTypeSwitch.isChecked) ReflectUtils.getFieldName<ReflectBean>() else ReflectUtils.getFieldName(c)
        for (name in nameList) {
            val value = if (mTypeSwitch.isChecked) ReflectUtils.getFieldValue<ReflectBean>(name) else ReflectUtils.getFieldValue(c, constructor, name)
            if (value != null) {
                sb.append(name).append(" : ").append(value.toString()).append("\n\n")
            }
        }
        mMsgTv.text = sb
    }

    /** 执行某个函数 */
    private fun executeFunction() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mTypeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val methodList = if (mTypeSwitch.isChecked) ReflectUtils.getMethodName<ReflectBean>() else ReflectUtils.getMethodName(c)
        for (methodName in methodList) {
            val result = ReflectUtils.executeFunction(c, constructor, methodName)
            if (result != null) {
                sb.append(methodName).append(" : ").append(result.toString()).append("\n\n")
            }
        }
        mMsgTv.text = sb
    }

    /** 执行某个有参函数 */
    private fun executeParamFunction() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mTypeSwitch.isChecked) {
            ReflectUtils.getConstructor<ReflectBean>(arrayOf(Int::class.java, String::class.java), arrayOf(21, "Japan"))
        } else {
            ReflectUtils.getConstructor(c, arrayOf(Int::class.java, String::class.java), arrayOf(21, "Japan"))
        }
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val value = if (mTypeSwitch.isChecked) ReflectUtils.getFieldValue<ReflectBean>("age") else ReflectUtils.getFieldValue(c, constructor, "age")
        if (value != null) {
            sb.append("age").append(" : ").append(value.toString()).append("\n\n")
        }
        val result = if (mTypeSwitch.isChecked) {
            ReflectUtils.executeFunctionByConstructor<ReflectBean>(constructor, "getAgeTest", arrayOf(Int::class.java), arrayOf(5))
        } else {
            ReflectUtils.executeFunction(c, constructor, "getAgeTest", arrayOf(Int::class.java), arrayOf(5))
        }
        if (result != null) {
            sb.append("getAgeTest").append(" : ").append(result.toString()).append("\n\n")
        }
        mMsgTv.text = sb
    }

    /** 设置值 */
    private fun setValue() {
        val c = if (mTypeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mMsgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mTypeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mMsgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val valueOld = if (mTypeSwitch.isChecked) {
            ReflectUtils.getFieldValue<ReflectBean>(constructor, "nationality")
        } else {
            ReflectUtils.getFieldValue(c, constructor, "nationality")
        }
        if (valueOld != null) {
            sb.append("old nationality : ").append(valueOld.toString()).append("\n\n")
        }
        val isSuccess = if (mTypeSwitch.isChecked) {
            ReflectUtils.setFieldValue<ReflectBean>(constructor, "nationality", "Brazil")
        } else {
            ReflectUtils.setFieldValue(c, constructor, "nationality", "Brazil")
        }
        sb.append("is success : ").append(isSuccess.toString()).append("\n\n")
        val valueNew = if (mTypeSwitch.isChecked) {
            ReflectUtils.getFieldValue<ReflectBean>(constructor, "nationality")
        } else {
            ReflectUtils.getFieldValue(c, constructor, "nationality")
        }
        if (valueNew != null) {
            sb.append("old nationality : ").append(valueNew.toString()).append("\n\n")
        }
        mMsgTv.text = sb
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}