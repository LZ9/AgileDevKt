package com.lodz.android.agiledevkt.utils.ds

import com.lodz.android.agiledevkt.App
import com.lodz.android.corekt.anko.getData
import com.lodz.android.corekt.anko.putData
import com.lodz.android.pandora.rx.utils.getDataRx
import com.lodz.android.pandora.rx.utils.putDataRx

/**
 * DataStore管理类
 * @author zhouL
 * @date 2023/8/4
 */
object DsManager {

    /**
     * 设置编号
     * @param id 编号
     */
    suspend fun setId(id: Long?) {
        App.get().dataStore.putData(DsConfig.KEY_ID, id ?: 0L)
    }

    /** 获取编号  */
    suspend fun getId() = App.get().dataStore.getData(DsConfig.KEY_ID, 0L)

    /**
     * 设置编号
     * @param id 编号
     */
    fun setIdRx(id: Long?) = App.get().dataStore.putDataRx(DsConfig.KEY_ID, id ?: 0L)

    /** 获取编号  */
    fun getIdRx() = App.get().dataStore.getDataRx(DsConfig.KEY_ID, 0L)

    /**
     * 设置姓名
     * @param name 姓名
     */
    suspend fun setName(name: String?) {
        App.get().dataStore.putData(DsConfig.KEY_NAME, name ?: "无")
    }

    /** 获取姓名  */
    suspend fun getName() = App.get().dataStore.getData(DsConfig.KEY_NAME, "无")

    /**
     * 设置姓名
     * @param name 姓名
     */
    fun setNameRx(name: String?) = App.get().dataStore.putDataRx(DsConfig.KEY_NAME, name ?: "无")

    /** 获取姓名  */
    fun getNameRx() = App.get().dataStore.getDataRx(DsConfig.KEY_NAME, "无")

    /**
     * 设置年龄
     * @param age 年龄
     */
    suspend fun setAge(age: Int?) {
        App.get().dataStore.putData(DsConfig.KEY_AGE, age ?: 0)
    }

    /** 获取年龄 */
    suspend fun getAge() = App.get().dataStore.getData(DsConfig.KEY_AGE, 0)

    /**
     * 设置年龄
     * @param age 年龄
     */
    fun setAgeRx(age: Int?) = App.get().dataStore.putDataRx(DsConfig.KEY_AGE, age ?: 0)

    /** 获取年龄  */
    fun getAgeRx() = App.get().dataStore.getDataRx(DsConfig.KEY_AGE, 0)

    /**
     * 设置身高
     * @param height 身高
     */
    suspend fun setHeight(height: Float?) {
        App.get().dataStore.putData(DsConfig.KEY_HEIGHT, height ?: 0f)
    }

    /** 获取身高 */
    suspend fun getHeight() = App.get().dataStore.getData(DsConfig.KEY_HEIGHT, 0f)

    /**
     * 设置身高
     * @param height 身高
     */
    fun setHeightRx(height: Float?) = App.get().dataStore.putDataRx(DsConfig.KEY_HEIGHT, height ?: 0f)

    /** 获取身高  */
    fun getHeightRx() = App.get().dataStore.getDataRx(DsConfig.KEY_HEIGHT, 0f)

    /**
     * 设置是否研究生
     * @param isPostgraduate 是否研究生
     */
    suspend fun setPostgraduate(isPostgraduate: Boolean?) {
        App.get().dataStore.putData(DsConfig.KEY_IS_POSTGRADUATE, isPostgraduate ?: false)
    }

    /** 获取是否研究生 */
    suspend fun getPostgraduate() = App.get().dataStore.getData(DsConfig.KEY_IS_POSTGRADUATE, false)

    /**
     * 设置是否研究生
     * @param isPostgraduate 是否研究生
     */
    fun setPostgraduateRx(isPostgraduate: Boolean?) = App.get().dataStore.putDataRx(DsConfig.KEY_IS_POSTGRADUATE, isPostgraduate ?: false)

    /** 获取是否研究生  */
    fun getPostgraduateRx() = App.get().dataStore.getDataRx(DsConfig.KEY_IS_POSTGRADUATE, false)

    /**
     * 设置薪资
     * @param salary 薪资
     */
    suspend fun setSalary(salary: Double?) {
        App.get().dataStore.putData(DsConfig.KEY_SALARY, salary ?: 0.0)
    }

    /** 获取薪资 */
    suspend fun getSalary() = App.get().dataStore.getData(DsConfig.KEY_SALARY, 0.0)

    /**
     * 设置薪资
     * @param salary 薪资
     */
    fun setSalaryRx(salary: Double?) = App.get().dataStore.putDataRx(DsConfig.KEY_SALARY, salary ?: 0.0)

    /** 获取薪资  */
    fun getSalaryRx() = App.get().dataStore.getDataRx(DsConfig.KEY_SALARY, 0.0)

    /**
     * 设置兴趣爱好
     * @param hobby 兴趣爱好
     */
    suspend fun setHobby(hobby: Set<String>?) {
        App.get().dataStore.putData(DsConfig.KEY_HOBBY, hobby ?: setOf())
    }

    /** 获取兴趣爱好 */
    suspend fun getHobby() = App.get().dataStore.getData(DsConfig.KEY_HOBBY, setOf())

    /**
     * 设置兴趣爱好
     * @param hobby 兴趣爱好
     */
    fun setHobbyRx(hobby: Set<String>?) = App.get().dataStore.putDataRx(DsConfig.KEY_HOBBY, hobby ?: setOf())

    /** 获取兴趣爱好  */
    fun getHobbyRx() = App.get().dataStore.getDataRx(DsConfig.KEY_HOBBY, setOf())

}