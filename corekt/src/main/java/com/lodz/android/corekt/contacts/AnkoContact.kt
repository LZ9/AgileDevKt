package com.lodz.android.corekt.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract

/**
 * 通讯录帮助类
 * @author zhouL
 * @date 2022/3/28
 */

@SuppressLint("Range")
@JvmOverloads
/** 获取所有通讯录数据 */
fun Context.getAllContactData(uri: Uri = ContactsContract.Contacts.CONTENT_URI): ArrayList<ContactsInfoBean> {
    val list = ArrayList<ContactsInfoBean>()
    val contactIdList = getContactId(uri)
    for (contactId in contactIdList) {
        val rawContactIdList = getRawContactId(contactId)
        for (rawContactId in rawContactIdList) {
            val bean = getDataByRawContactId(rawContactId)
            if (bean != null) {
                list.add(bean)
            }
        }
    }
    return list
}


@SuppressLint("Range")
@JvmOverloads
/** 查询联系人Contacts表得到所有ContactsID */
fun Context.getContactId(uri: Uri = ContactsContract.Contacts.CONTENT_URI): ArrayList<String> {
    val list = ArrayList<String>()
    contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    )?.use { contactsCursor ->
        while (contactsCursor.moveToNext()) {
            list.add(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID)))
        }
    }
    return list
}

@SuppressLint("Range")
@JvmOverloads
/** 查询联系人RawContacts表根据[contactId]获取RawContactsID */
fun Context.getRawContactId(
    contactId: String,
    uri: Uri = ContactsContract.RawContacts.CONTENT_URI
): ArrayList<String> {
    val list = ArrayList<String>()
    contentResolver.query(
        uri,
        null,
        ContactsContract.RawContacts.CONTACT_ID + "=" + contactId,
        null,
        null
    )?.use { rawCursor ->
        while (rawCursor.moveToNext()) {
            list.add(rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts._ID)))
        }
    }
    return list
}

@SuppressLint("Range")
/** 查询联系人Data表根据[contactId]获取该联系人的各数据 */
fun Context.getDataByContactId(
    contactId: String,
): ContactsInfoBean? {
    val list = getRawContactId(contactId)
    if (list.size == 0){
        return null
    }
    return getDataByRawContactId(list[0])
}

@SuppressLint("Range")
@JvmOverloads
/** 查询联系人Data表根据[rawContactId]获取该联系人的各数据 */
fun Context.getDataByRawContactId(
    rawContactId: String,
    uri: Uri = ContactsContract.Data.CONTENT_URI
): ContactsInfoBean? = contentResolver.query(
    uri,
    null,
    ContactsContract.Data.RAW_CONTACT_ID + "=" + rawContactId,
    null,
    null
)?.use { dataCursor ->
    val bean = ContactsInfoBean()
    if (dataCursor.moveToFirst()) {
        do {
            val mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
            if (mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                val item = ContactsPhoneBean()
                item.number = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: ""
                item.normalizedNumber = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)) ?: ""
                item.from = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA6)) ?: ""
                bean.phoneList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE) {
                bean.company = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)) ?: ""
                bean.title = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE) {
                bean.postal = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                bean.emailList.add(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) ?: "")
            }
            if (mimeType == ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE) {
                bean.note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) {
                bean.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE) {
                val array = dataCursor.getBlob(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
                if (array != null) {
                    bean.avatarArray = array
                    bean.avatarBitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
                }
            }
        } while (dataCursor.moveToNext())
    }
    return@use bean
}