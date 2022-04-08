package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录昵称数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsNicknameBean : BaseContactsDataBean() {

    /** 昵称 */
    var nickname = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE
}