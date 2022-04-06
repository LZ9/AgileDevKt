package com.lodz.android.corekt.contacts.bean

/**
 * 通讯录邮箱数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsEmailBean {

    /** 数据编号 */
    var dataId = ""

    /** 邮箱地址 */
    var address = ""

    /** 邮箱类型（ContactsContract.CommonDataKinds.Email.TYPE_HOME） */
    var type = ""

    /** 自定义类型名称（type == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM 时有值） */
    var label = ""
}