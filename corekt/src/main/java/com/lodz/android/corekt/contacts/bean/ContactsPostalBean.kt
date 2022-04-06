package com.lodz.android.corekt.contacts.bean

/**
 * 通讯录地址数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsPostalBean {

    /** 数据编号 */
    var dataId = ""

    /** 地址名称 */
    var address = ""

    /** 街道（通常和地址一样） */
    var street = ""

    /** 类型（ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME） */
    var type = ""

    /** 自定义类型名称（type == ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM 时有值） */
    var label = ""
}