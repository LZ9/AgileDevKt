package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录姓名数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsNameBean : BaseContactsDataBean() {

    /** 姓名 */
    var name = ""

    /** 别名（通常和name一样） */
    var givenName = ""

    /** 拼音 */
    var phonetic = ""

    /** 全名风格（通常是ContactsContract.FullNameStyle.CJK） */
    var fullNameStyle = ""

    /** 拼音风格（通常是ContactsContract.PhoneticNameStyle.PINYIN） */
    var phoneticNameStyle = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
}