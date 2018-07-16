package com.lodz.android.corekt.utils

import java.lang.reflect.Field

/**
 * 反射帮助类
 * Created by zhouL on 2018/6/25.
 */
object ReflectUtils {

    /** 根据类名[classPath]获取对应的class（类名须包括包名，找不到返回null） */
    fun getClassForName(classPath: String): Class<*>? {
        try {
            return Class.forName(classPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 获取类[c]的无参构造函数对象（如果目标对象没有无参构造函数返回null） */
    fun getConstructor(c: Class<*>): Any? {
        try {
            return c.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 获取类[c]带有参数的构造函数对象，包括构造函数的参数类型[paramClassTypes]和构造函数的具体入参[params]（找不到返回null） */
    fun getConstructor(c: Class<*>, paramClassTypes: Array<Class<*>>, params: Array<Any>): Any? {
        try {
            val constructor = c.getDeclaredConstructor(*paramClassTypes)
            constructor.isAccessible = true// 构造函数是private的话需要设置为true
            return constructor.newInstance(*params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 执行类[c]的某个函数，需要类的对象[obj]，函数名字[functionName]，函数的参数类型[paramClassTypes]和具体入参[params] */
    fun executeFunction(c: Class<*>, constructor: Any, functionName: String, paramClassTypes: Array<Class<*>>? = null, params: Array<Any>? = null): Any? {
        try {
            val method = if (paramClassTypes == null) c.getDeclaredMethod(functionName) else c.getDeclaredMethod(functionName, *paramClassTypes)// 方法名和参数类型
            method.isAccessible = true
            return if(params == null) method.invoke(constructor) else method.invoke(constructor, *params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 从类[c]的对象[obj]里去获取变量名为[typeName]的值 */
    fun getFieldValue(c: Class<*>, constructor: Any, typeName: String): Any? {
        try {
            val field = c.getDeclaredField(typeName) // privategetField不能获取private
            field.isAccessible = true// 设置是否允许访问，因为变量是private的，所以要手动设置允许访问
            return field.get(constructor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 获取类[c]的变量名为[typeName]的Field对象 */
    fun getField(c: Class<*>, typeName: String): Field? {
        try {
            val field = c.getDeclaredField(typeName)
            field.isAccessible = true
            return field
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 在类[c]的对象[obj]里去设置变量名为[typeName]的[value]值 */
    fun setFieldValue(c: Class<*>, constructor: Any, typeName: String, value: Any): Boolean {
        try {
            val field = c.getDeclaredField(typeName)
            field.isAccessible = true
            field.set(constructor, value)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /** 获取类[c]里的方法名列表 */
    fun getMethodName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        val methods = c.declaredMethods
        for (method in methods) {
            list.add(method.name)
        }
        return list
    }

    /** 获取类[c]里的变量名列表 */
    fun getFieldName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        val fields = c.declaredFields
        for (field in fields) {
            list.add(field.name)
        }
        return list
    }

    /** 获取类[c]里所有构造函数名称 */
    fun getConstructorName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        val constructors = c.declaredConstructors
        for (constructor in constructors) {
            list.add(constructor.toString())
        }
        return list
    }

}