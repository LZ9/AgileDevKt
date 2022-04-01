package com.lodz.android.corekt.contacts

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.bean.*

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
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)) ?: ""
                bean.phoneList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE) {
                bean.company = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)) ?: ""
                bean.title = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE) {
                val item = ContactsPostalBean()
                item.address = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)) ?: ""
                item.street = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL)) ?: ""
                bean.postalList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                val item = ContactsEmailBean()
                item.address = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)) ?: ""
                bean.emailList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE) {
                bean.note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) {
                bean.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)) ?: ""
                bean.givenName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)) ?: ""
                bean.phonetic = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME)) ?: ""
                bean.fullNameStyle = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE)) ?: ""
                bean.phoneticNameStyle = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE) {
                bean.avatarArray = dataCursor.getBlob(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
            }
            if (mimeType == ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE) {
                bean.website = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)) ?: ""
                bean.websiteType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE) {
                val item = ContactsImBean()
                item.account = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)) ?: ""
                item.protocol = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)) ?: ""
                item.customProtocolName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL)) ?: ""
                bean.imList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE) {
                val item = ContactsRelationBean()
                item.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.NAME)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL)) ?: ""
                bean.relationList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE) {
                bean.groupRowIdList.add(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)) ?: "")
            }
            if (mimeType == ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE) {
                bean.nickName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE) {
                val item = ContactsEventBean()
                item.date = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)) ?: ""
                bean.eventList.add(item)
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

    if (contactsInfoBean.name.isNotEmpty() || contactsInfoBean.givenName.isNotEmpty()){//插入姓名
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactsInfoBean.name.ifEmpty { contactsInfoBean.givenName })
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactsInfoBean.givenName.ifEmpty { contactsInfoBean.name })
        if (contactsInfoBean.phonetic.isNotEmpty()){
            values.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME, contactsInfoBean.phonetic)
        }
        values.put(
            ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE,
            contactsInfoBean.fullNameStyle.ifEmpty { ContactsContract.FullNameStyle.CJK.toString() }
        )
        values.put(
            ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE,
            contactsInfoBean.phoneticNameStyle.ifEmpty { ContactsContract.PhoneticNameStyle.PINYIN.toString() }
        )
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

    if (contactsInfoBean.postalList.isNotEmpty()){//插入地址
        for (item in contactsInfoBean.postalList) {
            if (item.address.isEmpty() && item.street.isEmpty()){
                continue
            }
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, item.address.ifEmpty { item.street })
            values.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, item.street.ifEmpty { item.address })
            val type = when {
                item.label.isNotEmpty() -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()
                item.type.isNotEmpty() -> item.type
                else -> ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME.toString()
            }
            values.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, type)
            if (item.label.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, item.label)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
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
        for (item in contactsInfoBean.emailList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, item.address)
            val type = when {
                item.label.isNotEmpty() -> ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM.toString()
                item.type.isNotEmpty() -> item.type
                else -> ContactsContract.CommonDataKinds.Email.TYPE_HOME.toString()
            }
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, type)
            if (item.label.isNotEmpty()) {
                values.put(ContactsContract.CommonDataKinds.Email.LABEL, item.label)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.phoneList.isNotEmpty()){//插入手机
        for (item in contactsInfoBean.phoneList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, item.number)
            val normalizedNumber = when {
                item.normalizedNumber.isNotEmpty() -> item.normalizedNumber
                item.number.isNotEmpty() -> "+86".append(item.number.trim())
                else -> ""
            }
            if (normalizedNumber.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, normalizedNumber)
            }
            if (item.from.isNotEmpty()){
                values.put(ContactsContract.CommonDataKinds.Phone.DATA6, item.from)
            }
            val type = when {
                item.label.isNotEmpty() -> ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM.toString()
                item.type.isNotEmpty() -> item.type
                else -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString()
            }
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, type)
            if (item.label.isNotEmpty()) {
                values.put(ContactsContract.CommonDataKinds.Phone.LABEL, item.label)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.website.isNotEmpty()){//插入网站
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.Website.URL, contactsInfoBean.website)
        values.put(
            ContactsContract.CommonDataKinds.Website.TYPE,
            contactsInfoBean.websiteType.ifEmpty { ContactsContract.CommonDataKinds.Website.TYPE_OTHER.toString() }
        )
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.imList.isNotEmpty()){//插入及时通讯
        for (item in contactsInfoBean.imList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Im.DATA, item.account)
            values.put(
                ContactsContract.CommonDataKinds.Im.TYPE,
                item.type.ifEmpty { ContactsContract.CommonDataKinds.Im.TYPE_OTHER.toString() }
            )
            val protocol = when {
                item.customProtocolName.isNotEmpty() -> ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM.toString()
                item.protocol.isNotEmpty() -> item.protocol
                else -> ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM.toString()
            }
            values.put(ContactsContract.CommonDataKinds.Im.PROTOCOL, protocol)
            if (item.customProtocolName.isNotEmpty()) {
                values.put(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL, item.customProtocolName)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.relationList.isNotEmpty()){//插入关系
        for (item in contactsInfoBean.relationList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Relation.NAME, item.name)
            val type = when {
                item.label.isNotEmpty() -> ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM.toString()
                item.type.isNotEmpty() -> item.type
                else -> ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT.toString()
            }
            values.put(ContactsContract.CommonDataKinds.Relation.TYPE, type)
            if (item.label.isNotEmpty()) {
                values.put(ContactsContract.CommonDataKinds.Relation.LABEL, item.label)
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.groupRowIdList.isNotEmpty()){//插入群组ID
        for (rawId in contactsInfoBean.groupRowIdList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, rawId)
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }

    if (contactsInfoBean.nickName.isNotEmpty()){//插入昵称
        values.clear()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
        values.put(ContactsContract.CommonDataKinds.Nickname.NAME, contactsInfoBean.nickName)
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
    }

    if (contactsInfoBean.eventList.isNotEmpty()){//插入事件
        for (item in contactsInfoBean.eventList) {
            values.clear()
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
            values.put(ContactsContract.CommonDataKinds.Event.START_DATE, item.date)
            values.put(ContactsContract.CommonDataKinds.Event.TYPE, item.type.ifEmpty { ContactsContract.CommonDataKinds.Event.TYPE_OTHER.toString() })
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        }
    }
}


