package com.lodz.android.agiledevkt.modules.reflect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityReflectBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 反射测试类
 * Created by zhouL on 2018/7/16.
 */
class ReflectActivity : BaseActivity() {

    companion object {
        private const val BEAN_PATH = "com.lodz.android.agiledevkt.modules.reflect.ReflectBean"
        fun start(context: Context) {
            val intent = Intent(context, ReflectActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityReflectBinding by bindingLayout(ActivityReflectBinding::inflate)

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

        // 通过路径获取Class
        mBinding.getClassBtn.setOnClickListener {
            getClassName()
        }

        // 获取所有构造函数名称
        mBinding.getConstructorNameBtn.setOnClickListener {
            getConstructorName()
        }

        // 获取对象里的变量名称
        mBinding.getFieldNameBtn.setOnClickListener {
            getFieldName()
        }

        // 获取方法名
        mBinding.getMethodNameBtn.setOnClickListener {
            getMethodName()
        }

        // 获取对象里变量的值
        mBinding.getFieldValueBtn.setOnClickListener {
            getFieldValue()
        }

        // 执行无参函数
        mBinding.executeFunctionBtn.setOnClickListener {
            executeFunction()
        }

        // 执行有参函数
        mBinding.executeParamFunctionBtn.setOnClickListener {
            executeParamFunction()
        }

        // 设置值并读取测试
        mBinding.setValueBtn.setOnClickListener {
            setValue()
        }

    }

    /** 通过路径获取Class */
    private fun getClassName() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        mBinding.msgTv.text = c?.name ?: getString(R.string.reflect_unfind_class)
    }

    /** 获取所有构造函数名称 */
    private fun getConstructorName() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mBinding.typeSwitch.isChecked) ReflectUtils.getConstructorName<ReflectBean>() else ReflectUtils.getConstructorName(c)
        val sb = StringBuilder()
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mBinding.msgTv.text = sb
    }

    /** 获取对象里的变量名称 */
    private fun getFieldName() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mBinding.typeSwitch.isChecked) ReflectUtils.getFieldName<ReflectBean>() else ReflectUtils.getFieldName(c)
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mBinding.msgTv.text = sb
    }

    /** 获取方法名 */
    private fun getMethodName() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val list = if (mBinding.typeSwitch.isChecked) ReflectUtils.getMethodName<ReflectBean>() else ReflectUtils.getMethodName(c)
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        for (funName in list) {
            sb.append(funName).append("\n\n")
        }
        mBinding.msgTv.text = sb
    }

    /** 获取对象里变量的值 */
    private fun getFieldValue() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mBinding.typeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val nameList = if (mBinding.typeSwitch.isChecked) ReflectUtils.getFieldName<ReflectBean>() else ReflectUtils.getFieldName(c)
        for (name in nameList) {
            val value = if (mBinding.typeSwitch.isChecked) ReflectUtils.getFieldValue<ReflectBean>(name) else ReflectUtils.getFieldValue(c, constructor, name)
            if (value != null) {
                sb.append(name).append(" : ").append(value.toString()).append("\n\n")
            }
        }
        mBinding.msgTv.text = sb
    }

    /** 执行某个函数 */
    private fun executeFunction() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mBinding.typeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val methodList = if (mBinding.typeSwitch.isChecked) ReflectUtils.getMethodName<ReflectBean>() else ReflectUtils.getMethodName(c)
        for (methodName in methodList) {
            val result = ReflectUtils.executeFunction(c, constructor, methodName)
            if (result != null) {
                sb.append(methodName).append(" : ").append(result.toString()).append("\n\n")
            }
        }
        mBinding.msgTv.text = sb
    }

    /** 执行某个有参函数 */
    private fun executeParamFunction() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mBinding.typeSwitch.isChecked) {
            ReflectUtils.getConstructor<ReflectBean>(arrayOf(Int::class.java, String::class.java), arrayOf(21, "Japan"))
        } else {
            ReflectUtils.getConstructor(c, arrayOf(Int::class.java, String::class.java), arrayOf(21, "Japan"))
        }
        if (constructor == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val value = if (mBinding.typeSwitch.isChecked) ReflectUtils.getFieldValue<ReflectBean>("age") else ReflectUtils.getFieldValue(c, constructor, "age")
        if (value != null) {
            sb.append("age").append(" : ").append(value.toString()).append("\n\n")
        }
        val result = if (mBinding.typeSwitch.isChecked) {
            ReflectUtils.executeFunctionByConstructor<ReflectBean>(constructor, "getAgeTest", arrayOf(Int::class.java), arrayOf(5))
        } else {
            ReflectUtils.executeFunction(c, constructor, "getAgeTest", arrayOf(Int::class.java), arrayOf(5))
        }
        if (result != null) {
            sb.append("getAgeTest").append(" : ").append(result.toString()).append("\n\n")
        }
        mBinding.msgTv.text = sb
    }

    /** 设置值 */
    private fun setValue() {
        val c = if (mBinding.typeSwitch.isChecked) ReflectUtils.getClassForType<ReflectBean>() else ReflectUtils.getClassForName(BEAN_PATH)
        if (c == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_class)
            return
        }
        val constructor = if (mBinding.typeSwitch.isChecked) ReflectUtils.getConstructor<ReflectBean>() else ReflectUtils.getConstructor(c)
        if (constructor == null) {
            mBinding.msgTv.setText(R.string.reflect_unfind_no_param_constructor)
            return
        }
        val sb = StringBuilder(c.simpleName).append("\n--------------\n")
        val valueOld = if (mBinding.typeSwitch.isChecked) {
            ReflectUtils.getFieldValue<ReflectBean>(constructor, "nationality")
        } else {
            ReflectUtils.getFieldValue(c, constructor, "nationality")
        }
        if (valueOld != null) {
            sb.append("old nationality : ").append(valueOld.toString()).append("\n\n")
        }
        val isSuccess = if (mBinding.typeSwitch.isChecked) {
            ReflectUtils.setFieldValue<ReflectBean>(constructor, "nationality", "Brazil")
        } else {
            ReflectUtils.setFieldValue(c, constructor, "nationality", "Brazil")
        }
        sb.append("is success : ").append(isSuccess.toString()).append("\n\n")
        val valueNew = if (mBinding.typeSwitch.isChecked) {
            ReflectUtils.getFieldValue<ReflectBean>(constructor, "nationality")
        } else {
            ReflectUtils.getFieldValue(c, constructor, "nationality")
        }
        if (valueNew != null) {
            sb.append("old nationality : ").append(valueNew.toString()).append("\n\n")
        }
        mBinding.msgTv.text = sb
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}