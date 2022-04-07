package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录手机数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsPhoneBean : BaseContactsDataBean() {

    /** 手机号 */
    var number = ""

    /** 手机号带区号（+86） */
    var normalizedNumber = ""

    /** 手机号归属地 */
    var from = ""

    /** 类别（ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE） */
    var type = ""

    /** 自定义类别名称（type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM 时有值） */
    var label = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
}