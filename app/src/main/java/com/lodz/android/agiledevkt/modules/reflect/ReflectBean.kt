package com.lodz.android.agiledevkt.modules.reflect

/**
 * 反射测试数据
 * Created by zhouL on 2018/7/16.
 */
class ReflectBean {

    val name = "Jack"

    protected var age = 30

    private var nationality = "China"

    internal var sex = "men"

    var size = 16
        set(value) {
            field = value + 5
        }
        get() {
            return field - 3
        }

    constructor()

    constructor(age: Int, nationality: String) {
        this.age = age
        this.nationality = nationality
    }

    fun getPersonName(): String = name

    protected fun getPersonAge(): Int = age

    private fun getPersonNationality(): String = nationality

    fun getAgeTest(num: Int = 0): String = (age + num).toString()

    fun setAgeTest(num: Int) {
        age = num * 2
    }
}