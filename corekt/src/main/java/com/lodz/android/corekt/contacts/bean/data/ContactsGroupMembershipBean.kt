package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录群组数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsGroupMembershipBean : BaseContactsDataBean() {

    /** 群组ID */
    var groupRowId = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE
}