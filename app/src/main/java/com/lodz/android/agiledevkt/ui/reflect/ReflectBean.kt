package com.lodz.android.agiledevkt.ui.reflect

/**
 * 反射测试数据
 * Created by zhouL on 2018/7/16.
 */
class ReflectBean {

    val name = "Jack"

    protected var age = 30

    private var nationality = "China"

    internal var sex = "men"

    constructor()

    constructor(age: Int, nationality: String) {
        this.age = age
        this.nationality = nationality
    }

    fun getPersonName() = name

    protected fun getPersonAge() = age

    private fun getPersonNationality() = nationality

    fun getAgeTest(num: Int = 0) = (age + num).toString()
}