package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录邮箱数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsEmailBean : BaseContactsDataBean() {

    /** 邮箱地址 */
    var address = ""

    /** 邮箱类型（ContactsContract.CommonDataKinds.Email.TYPE_HOME） */
    var type = ""

    /** 自定义类型名称（type == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM 时有值） */
    var label = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
}