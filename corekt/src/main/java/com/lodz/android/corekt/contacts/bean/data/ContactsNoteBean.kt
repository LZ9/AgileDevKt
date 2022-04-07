package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录备注数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsNoteBean : BaseContactsDataBean() {

    /** 备注 */
    var note = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
}