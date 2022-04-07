package com.lodz.android.corekt.contacts.bean.data

import android.provider.ContactsContract
import com.lodz.android.corekt.contacts.bean.BaseContactsDataBean

/**
 * 通讯录图片数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsPhotoBean : BaseContactsDataBean() {

    /** 图片字节数组 */
    var photoArray: ByteArray? = null

    override fun getMimeType(): String = ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
}