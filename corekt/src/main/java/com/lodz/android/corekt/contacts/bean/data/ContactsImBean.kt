package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录即时通讯数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsImBean : BaseContactsDataBean() {

    /** 账号 */
    var account = ""

    /** 类型（ContactsContract.CommonDataKinds.Im.TYPE_OTHER） */
    var type = ""

    /** 协议（ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ） */
    var protocol = ""

    /** 自定义协议名称（protocol == ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM 时有值） */
    var customProtocolName = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE
}