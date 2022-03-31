package com.lodz.android.corekt.contacts

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract

/**
 * 通讯录帮助类
 * @author zhouL
 * @date 2022/3/28
 */


/** 获取路径为[uri]的通讯录数据（不传获取全部） */
@SuppressLint("Range")
@JvmOverloads
fun Context.getContactData(uri: Uri = ContactsContract.Contacts.CONTENT_URI): ArrayList<ContactsInfoBean> {
    val list = ArrayList<ContactsInfoBean>()
    val contactIdList = getContactId(uri)
    for (contactId in contactIdList) {
        val rawContactIdList = getRawContactId(contactId)
        for (rawContactId in rawContactIdList) {
            val bean = getDataByRawContactId(contactId, rawContactId)
            if (bean != null) {
                list.add(bean)
            }
        }
    }
    return list
}

@JvmOverloads
/** 查询联系人Contacts表得到所有ContactsID */
@SuppressLint("Range")
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

/** 查询联系人RawContacts表根据[contactId]获取RawContactsID */
@SuppressLint("Range")
@JvmOverloads
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

/** 查询联系人Data表根据[contactId]获取该联系人的各数据 */
@SuppressLint("Range")
fun Context.getDataByContactId(
    contactId: String,
): ContactsInfoBean? {
    val list = getRawContactId(contactId)
    if (list.size == 0){
        return null
    }
    return getDataByRawContactId(contactId, list[0])
}


/** 查询联系人Data表根据[rawContactId]获取该联系人的各数据 */
@SuppressLint("Range")
@JvmOverloads
fun Context.getDataByRawContactId(
    contactId: String,
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
    bean.contactId = contactId
    bean.rawContactId = rawContactId
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

/** 删除[rawContactId]对应的通讯录数据（不传参数删除全部） */
@JvmOverloads
fun Context.deleteContact(rawContactId: String = "") {
    if (rawContactId.isNotEmpty()){
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=" + rawContactId, null)
        return
    }
    val contactIdList = getContactId()
    for (contactId in contactIdList) {
        val rawContactIdList = getRawContactId(contactId)
        for (rawId in rawContactIdList) {
            contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=" + rawId, null)
        }
    }
}

/** 新增通讯录信息数据[contactsInfoBean] */
fun Context.insertContactData(contactsInfoBean: ContactsInfoBean) {
    val values = ContentValues()
    val uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values) ?: return
    val rawContactId = ContentUris.parseId(uri)

    if (contactsInfoBean.name.isNotEmpty()){//插入姓名
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactsInfoBean.name)
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactsInfoBean.name)
        values.put(ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE, ContactsContract.CommonDataKinds.StructuredName.AWAY)
        values.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE, ContactsContract.CommonDataKinds.StructuredName.OFFLINE)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.company.isNotEmpty() || contactsInfoBean.title.isNotEmpty()){//插入公司和职员
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
        if (contactsInfoBean.company.isNotEmpty()){
            values.put(ContactsContract.CommonDataKinds.Organization.COMPANY, contactsInfoBean.company)
        }
        if (contactsInfoBean.title.isNotEmpty()){
            values.put(ContactsContract.CommonDataKinds.Organization.TITLE, contactsInfoBean.title)
        }
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.postal.isNotEmpty()){//插入单位
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, contactsInfoBean.postal)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.note.isNotEmpty()){//插入备注
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.Note.NOTE, contactsInfoBean.note)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.avatarArray != null){//插入头像
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, contactsInfoBean.avatarArray)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.emailList.isNotEmpty()){//插入邮箱
        for (email in contactsInfoBean.emailList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email)
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.phoneList.isNotEmpty()){//插入手机
        for (phoneBean in contactsInfoBean.phoneList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            if (phoneBean.number.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneBean.number)
            }
            if (phoneBean.normalizedNumber.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, phoneBean.normalizedNumber)
            }
            if (phoneBean.from.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.Phone.DATA6, phoneBean.from)
            }
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }

    }
}


