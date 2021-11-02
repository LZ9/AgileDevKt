package com.lodz.android.agiledevkt.bean

import java.io.Serializable

/**
 * Serializable测试类
 * @author zhouL
 * @date 2021/11/1
 */
class SerializableBean(var id: String) : Serializable {
    override fun toString(): String {
        return id
    }
}