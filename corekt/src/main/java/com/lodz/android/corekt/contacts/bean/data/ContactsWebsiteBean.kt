package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录网站数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsWebsiteBean : BaseContactsDataBean() {

    /** 网站 */
    var website = ""

    /** 网站类型（ContactsContract.CommonDataKinds.Website.TYPE_OTHER） */
    var websiteType = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE
}