package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录事件数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsEventBean : BaseContactsDataBean() {

    /** 日期(yyyy-MM-dd) */
    var date = ""

    /** 事件类型（ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY） */
    var type = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE
}