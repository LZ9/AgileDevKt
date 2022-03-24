package com.lodz.android.corekt.utils

import android.content.Context
import androidx.core.content.edit
import java.lang.ref.WeakReference

/**
 * SP工具类
 * @author zhouL
 * @date 2019/11/19
 */
class SharedPreferencesUtils private constructor() {

    companion object {
        private const val SP_NAME = "sp_data"

        private val sInstance = SharedPreferencesUtils()
        @JvmStatic
        fun get(): SharedPreferencesUtils = sInstance
    }

    private var mContext: WeakReference<Context>? = null

    private var mName: String = SP_NAME

    private var mMode: Int = Context.MODE_PRIVATE

    /** 初始化[context]上下文，[name]文件名，[mode]模式 */
    @JvmOverloads
    fun init(context: Context, name: String = SP_NAME, mode: Int = Context.MODE_PRIVATE) {
        mContext = WeakReference(context)
        mName = name
        mMode = mode
    }

    /** 保存String类型数据,键[key]，值[value] */
    fun putString(key: String, value: String) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putString(key, value)
        }
    }

    /** 获取String型数据,键[key]，默认值[defValue] */
    fun getString(key: String, defValue: String): String = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getString(key, defValue) ?: defValue
    }

    /** 保存Boolean类型数据,键[key]，值[value] */
    fun putBoolean(key: String, value: Boolean) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putBoolean(key, value)
        }
    }

    /** 获取Boolean型数据,键[key]，默认值[defValue] */
    fun getBoolean(key: String, defValue: Boolean): Boolean = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getBoolean(key, defValue) ?: defValue
    }

    /** 保存Float类型数据,键[key]，值[value] */
    fun putFloat(key: String, value: Float) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putFloat(key, value)
        }
    }

    /** 获取Float型数据,键[key]，默认值[defValue] */
    fun getFloat(key: String, defValue: Float): Float = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getFloat(key, defValue) ?: defValue
    }

    /** 保存Int类型数据,键[key]，值[value] */
    fun putInt(key: String, value: Int) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putInt(key, value)
        }
    }

    /** 获取Int型数据,键[key]，默认值[defValue] */
    fun getInt(key: String, defValue: Int): Int = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getInt(key, defValue) ?: defValue
    }

    /** 保存Long类型数据,键[key]，值[value] */
    fun putLong(key: String, value: Long) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putLong(key, value)
        }
    }

    /** 获取Long型数据,键[key]，默认值[defValue] */
    fun getLong(key: String, defValue: Long): Long = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getLong(key, defValue) ?: defValue
    }

    /** 保存StringSet类型数据,键[key]，值[value] */
    fun putStringSet(key: String, value: Set<String>) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.putStringSet(key, value)
        }
    }

    /** 获取StringSet型数据,键[key]，默认值[defValue] */
    fun getStringSet(key: String, defValue: Set<String>): Set<String> = catch(defValue) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.getStringSet(key, defValue) ?: defValue
    }


    /** 删除指定键的数据,键[key] */
    fun remove(key: String) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.remove(key)
        }
    }

    /** 清空整个sp数据 */
    fun clear() {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.edit {
            this.clear()
        }
    }

    /** 获取全部的sp数据，没有返回null */
    fun getAll(): Map<String, *>? = catch(null) {
        mContext?.get()?.getSharedPreferences(mName, mMode)?.all
    }

    private fun <T> catch(defValue: T, block: () -> T): T = try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        defValue
    }

}