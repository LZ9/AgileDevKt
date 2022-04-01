package com.lodz.android.corekt.contacts.bean

/**
 * 通讯录及时通讯数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsImBean {

    /** 账号 */
    var account = ""

    /** 类型（ContactsContract.CommonDataKinds.Im.TYPE_OTHER） */
    var type = ""

    /** 协议（ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ） */
    var protocol = ""

    /** 自定义协议名称（protocol == ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM 时有值） */
    var customProtocolName = ""
}