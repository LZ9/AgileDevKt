package com.lodz.android.agiledevkt.modules.array

import com.lodz.android.corekt.array.Groupable

/**
 * 职工数据
 * Created by zhouL on 2018/11/2.
 */
class StaffBean : Groupable {
    /** 编号 */
    var id = 0L
    /** 姓名 */
    var name = ""
    /** 年龄 */
    var age = 0

    override fun getSortStr(): String = name

    override fun toString(): String {
        return "id : $id   name : $name   age : $age \n"
    }
}