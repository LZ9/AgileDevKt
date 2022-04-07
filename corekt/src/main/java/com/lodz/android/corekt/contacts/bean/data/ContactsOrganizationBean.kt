package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录组织数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsOrganizationBean : BaseContactsDataBean() {

    /** 公司 */
    var company = ""

    /** 职位 */
    var title = ""

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
}