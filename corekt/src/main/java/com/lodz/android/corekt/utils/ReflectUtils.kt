package com.lodz.android.corekt.utils

import java.lang.reflect.Field

/**
 * 反射帮助类
 * Created by zhouL on 2018/6/25.
 */
object ReflectUtils {

    /** 根据类名[classPath]获取对应的Class（类名须包括包名，找不到返回null） */
    @JvmStatic
    fun getClassForName(classPath: String): Class<*>? {
        try {
            return Class.forName(classPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 根据类型[T]获取对应的Class */
    @JvmStatic
    inline fun <reified T> getClassForType(): Class<*> = T::class.java

    /** 获取类[c]的无参构造函数对象（如果目标对象没有无参构造函数返回null） */
    @JvmStatic
    fun getConstructor(c: Class<*>): Any? {
        try {
            return c.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 获取类型[T]的无参构造函数对象（如果目标对象没有无参构造函数返回null） */
    @JvmStatic
    inline fun <reified T> getConstructor(): Any? = getConstructor(T::class.java)

    /** 获取类[c]带有参数的构造函数对象，包括构造函数的参数类型[paramClassTypes]和构造函数的具体入参[params]（找不到返回null） */
    @JvmStatic
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

    /** 获取类型[T]带有参数的构造函数对象，包括构造函数的参数类型[paramClassTypes]和构造函数的具体入参[params]（找不到返回null） */
    @JvmStatic
    inline fun <reified T> getConstructor(paramClassTypes: Array<Class<*>>, params: Array<Any>): Any? = getConstructor(T::class.java, paramClassTypes, params)

    /** 执行类[c]的某个函数，需要类的构造函数对象[constructor]，函数名字[functionName]，函数的参数类型[paramClassTypes]和具体入参[params] */
    @JvmStatic
    fun executeFunction(c: Class<*>, constructor: Any, functionName: String, paramClassTypes: Array<Class<*>>? = null, params: Array<*>? = null): Any? {
        try {
            val method = if (paramClassTypes == null) c.getDeclaredMethod(functionName) else c.getDeclaredMethod(functionName, *paramClassTypes)// 方法名和参数类型
            method.isAccessible = true
            return if (params == null) method.invoke(constructor) else method.invoke(constructor, *params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /** 执行具有无参构造函数的类[c]的某个函数，函数名字[functionName]，函数的参数类型[paramClassTypes]和具体入参[params] */
    @JvmStatic
    fun executeFunction(c: Class<*>, functionName: String, paramClassTypes: Array<Class<*>>? = null, params: Array<*>? = null): Any? {
        val constructor = getConstructor(c) ?: return null
        return executeFunction(c, constructor, functionName, paramClassTypes, params)
    }

    /** 执行类型[T]的某个函数，需要类的构造函数对象[constructor]，函数名字[functionName]，函数的参数类型[paramClassTypes]和具体入参[params] */
    @JvmStatic
    inline fun <reified T> executeFunctionByConstructor(constructor: Any, functionName: String, paramClassTypes: Array<Class<*>>? = null, params: Array<*>? = null)
            : Any? = executeFunction(T::class.java, constructor, functionName, paramClassTypes, params)

    /** 执行具有无参构造函数的类型[T]的某个函数，函数名字[functionName]，函数的参数类型[paramClassTypes]和具体入参[params] */
    @JvmStatic
    inline fun <reified T> executeFunctions(functionName: String, paramClassTypes: Array<Class<*>>? = null, params: Array<*>? = null): Any? {
        val cls = T::class.java
        val constructor = getConstructor(cls) ?: return null
        return executeFunction(T::class.java, constructor, functionName, paramClassTypes, params)
    }

    /** 从类[c]的构造函数对象[constructor]里去获取变量名为[typeName]的值 */
    @JvmStatic
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

    /** 从具有无参构造函数的类[c]里去获取变量名为[typeName]的值 */
    @JvmStatic
    fun getFieldValue(c: Class<*>, typeName: String): Any? {
        val constructor = getConstructor(c) ?: return null
        return getFieldValue(c, constructor, typeName)
    }

    /** 从类型[T]的构造函数对象[constructor]里去获取变量名为[typeName]的值 */
    @JvmStatic
    inline fun <reified T> getFieldValue(constructor: Any, typeName: String): Any? = getFieldValue(T::class.java, constructor, typeName)

    /** 从具有无参构造函数的类型[T]里去获取变量名为[typeName]的值 */
    @JvmStatic
    inline fun <reified T> getFieldValue(typeName: String): Any? {
        val cls = T::class.java
        val constructor = getConstructor(cls) ?: return null
        return getFieldValue(cls, constructor, typeName)
    }

    /** 获取类[c]的变量名为[typeName]的Field对象 */
    @JvmStatic
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

    /** 获取类型[T]的变量名为[typeName]的Field对象 */
    @JvmStatic
    inline fun <reified T> getField(typeName: String): Field? = getField(T::class.java, typeName)

    /** 在类[c]的构造函数对象[constructor]里去设置变量名为[typeName]的[value]值 */
    @JvmStatic
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

    /** 在具有无参构造函数的类[c]里去设置变量名为[typeName]的[value]值 */
    @JvmStatic
    fun setFieldValue(c: Class<*>, typeName: String, value: Any): Boolean {
        val constructor = getConstructor(c) ?: return false
        return setFieldValue(c, constructor, typeName, value)
    }

    /** 在类型[T]的构造函数对象[constructor]里去设置变量名为[typeName]的[value]值 */
    @JvmStatic
    inline fun <reified T> setFieldValue(constructor: Any, typeName: String, value: Any): Boolean = setFieldValue(T::class.java, constructor, typeName, value)

    /** 在具有无参构造函数的类型[T]里去设置变量名为[typeName]的[value]值 */
    @JvmStatic
    inline fun <reified T> setFieldValue(typeName: String, value: Any): Boolean {
        val cls = T::class.java
        val constructor = getConstructor(cls) ?: return false
        return setFieldValue(T::class.java, constructor, typeName, value)
    }


    /** 获取类[c]里的方法名列表 */
    @JvmStatic
    fun getMethodName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        val methods = c.declaredMethods
        for (method in methods) {
            list.add(method.name)
        }
        return list
    }

    /** 获取类型[T]里的方法名列表 */
    @JvmStatic
    inline fun <reified T> getMethodName(): List<String> = getMethodName(T::class.java)

    /** 获取类[c]里的变量名列表 */
    @JvmStatic
    fun getFieldName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        val fields = c.declaredFields
        for (field in fields) {
            list.add(field.name)
        }
        return list
    }

    /** 获取类型[T]里的变量名列表 */
    @JvmStatic
    inline fun <reified T> getFieldName(): List<String> = getFieldName(T::class.java)

    /** 获取类[c]里所有构造函数名称 */
    @JvmStatic
    fun getConstructorName(c: Class<*>): List<String> {
        val list = ArrayList<String>()
        for (constructor in c.declaredConstructors) {
            list.add(constructor.toString())
        }
        return list
    }

    /** 获取类型[T]里所有构造函数名称 */
    @JvmStatic
    inline fun <reified T> getConstructorName(): List<String> = getConstructorName(T::class.java)

}