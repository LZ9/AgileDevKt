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
            val dataId = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data._ID))
            val mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
            if (mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE) {
                val item = ContactsPhoneBean()
                item.dataId = dataId
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
                item.dataId = dataId
                item.address = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)) ?: ""
                item.street = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL)) ?: ""
                bean.postalList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                val item = ContactsEmailBean()
                item.dataId = dataId
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
                item.dataId = dataId
                item.account = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)) ?: ""
                item.protocol = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)) ?: ""
                item.customProtocolName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL)) ?: ""
                bean.imList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE) {
                val item = ContactsRelationBean()
                item.dataId = dataId
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
                item.dataId = dataId
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

/** 全量更新通讯录数据 */
fun Context.updateContactData(bean: ContactsInfoBean) {
    if (bean.rawContactId.isEmpty()){
        return
    }
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, assembleStructuredName(bean.rawContactId, bean))
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE, assembleOrganization(bean.rawContactId, bean))
    for (item in bean.postalList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE, assembleStructuredPostal(bean.rawContactId, item), item.dataId)
    }
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE, assembleNote(bean.rawContactId, bean))
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, assemblePhoto(bean.rawContactId, bean))
    for (item in bean.emailList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, assembleEmail(bean.rawContactId, item), item.dataId)
    }
    for (item in bean.phoneList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, assemblePhone(bean.rawContactId, item), item.dataId)
    }
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE, assembleWebsite(bean.rawContactId, bean))
    for (item in bean.imList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE, assembleIm(bean.rawContactId, item), item.dataId)
    }
    for (item in bean.relationList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE, assembleRelation(bean.rawContactId, item), item.dataId)
    }
    for (rawId in bean.groupRowIdList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE, assembleGroupMembership(bean.rawContactId, rawId))
    }
    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE, assembleNickname(bean.rawContactId, bean))
    for (item in bean.eventList) {
        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, assembleEvent(bean.rawContactId, item), item.dataId)
    }
}

/** 更新通讯录的姓名相关数据 */
@JvmOverloads
fun Context.updateContactStructuredName(
    rawContactId: String,
    name: String,
    givenName: String = "",
    phonetic: String = "",
    fullNameStyle: Int = ContactsContract.FullNameStyle.CJK,
    phoneticNameStyle: Int = ContactsContract.PhoneticNameStyle.PINYIN
) {
    val bean = ContactsInfoBean()
    bean.rawContactId = rawContactId
    bean.name = name
    bean.givenName = givenName.ifEmpty { name }
    bean.phonetic = phonetic
    bean.fullNameStyle = fullNameStyle.toString()
    bean.phoneticNameStyle = phoneticNameStyle.toString()
    updateContactExecute(
        rawContactId,
        mimeType = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
        values = assembleStructuredName(rawContactId, bean)
    )
}

/** 更新通讯录的组织相关数据 */
@JvmOverloads
fun Context.updateContactOrganization(rawContactId: String, company: String, title: String = "") {
    val bean = ContactsInfoBean()
    bean.rawContactId = rawContactId
    bean.company = company
    bean.title = title
    updateContactExecute(
        rawContactId,
        mimeType = ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
        values = assembleOrganization(rawContactId, bean)
    )
}

/** 更新通讯录的地址相关数据 */
@JvmOverloads
fun Context.updateContactStructuredPostal(
    rawContactId: String,
    dataId: String,
    address: String,
    street: String = "",
    type: Int = ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
    label: String = ""
) {
    val bean = ContactsPostalBean()
    bean.dataId = dataId
    bean.address = address
    bean.street = street.ifEmpty { bean.address }
    bean.type = if (label.isNotEmpty()) {
        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()
    } else {
        type.toString()
    }
    bean.label = label
    updateContactExecute(rawContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE, assembleStructuredPostal(rawContactId, bean), dataId)
}




//
//
//
//    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE, assembleNote(bean.rawContactId, bean))
//    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, assemblePhoto(bean.rawContactId, bean))
//    for (item in bean.emailList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, assembleEmail(bean.rawContactId, item))
//    }
//    for (item in bean.phoneList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, assemblePhone(bean.rawContactId, item))
//    }
//    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE, assembleWebsite(bean.rawContactId, bean))
//    for (item in bean.imList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE, assembleIm(bean.rawContactId, item))
//    }
//    for (item in bean.relationList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE, assembleRelation(bean.rawContactId, item))
//    }
//    for (rawId in bean.groupRowIdList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE, assembleGroupMembership(bean.rawContactId, rawId))
//    }
//    updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE, assembleNickname(bean.rawContactId, bean))
//    for (item in bean.eventList) {
//        updateContactExecute(bean.rawContactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, assembleEvent(bean.rawContactId, item))
//    }


/** 执行更新通讯录函数 */
private fun Context.updateContactExecute(
    rawContactId: String,
    mimeType: String,
    values: ContentValues,
    dataId: String = ""
) {
    val where = if (dataId.isEmpty()){
        ContactsContract.Data.RAW_CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "= ?"
    } else {
        ContactsContract.Data.RAW_CONTACT_ID + "= ? AND " + ContactsContract.Data._ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "= ?"
    }
    val selectionArgs = if (dataId.isEmpty()) {
        arrayOf(rawContactId, mimeType)
    } else {
        arrayOf(rawContactId, dataId, mimeType)
    }
    contentResolver.update(ContactsContract.Data.CONTENT_URI, values, where, selectionArgs)
}

/** 新增通讯录信息数据[bean] */
fun Context.insertContactData(bean: ContactsInfoBean) {
    val values = ContentValues()
    val uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values) ?: return
    val rawContactId = ContentUris.parseId(uri).toString()

    if (bean.name.isNotEmpty() || bean.givenName.isNotEmpty()){//插入姓名
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleStructuredName(rawContactId, bean))
    }

    if (bean.company.isNotEmpty() || bean.title.isNotEmpty()){//插入公司和职员
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleOrganization(rawContactId, bean))
    }

    if (bean.postalList.isNotEmpty()){//插入地址
        for (item in bean.postalList) {
            if (item.address.isEmpty() && item.street.isEmpty()){
                continue
            }
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleStructuredPostal(rawContactId, item))
        }
    }

    if (bean.note.isNotEmpty()){//插入备注
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleNote(rawContactId, bean))
    }

    if (bean.avatarArray != null){//插入头像
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assemblePhoto(rawContactId, bean))
    }

    if (bean.emailList.isNotEmpty()){//插入邮箱
        for (item in bean.emailList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleEmail(rawContactId, item))
        }
    }

    if (bean.phoneList.isNotEmpty()){//插入手机
        for (item in bean.phoneList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assemblePhone(rawContactId, item))
        }
    }

    if (bean.website.isNotEmpty()){//插入网站
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleWebsite(rawContactId, bean))
    }

    if (bean.imList.isNotEmpty()){//插入及时通讯
        for (item in bean.imList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleIm(rawContactId, item))
        }
    }

    if (bean.relationList.isNotEmpty()){//插入关系
        for (item in bean.relationList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleRelation(rawContactId, item))
        }
    }

    if (bean.groupRowIdList.isNotEmpty()){//插入群组ID
        for (rawId in bean.groupRowIdList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleGroupMembership(rawContactId, rawId))
        }
    }

    if (bean.nickName.isNotEmpty()){//插入昵称
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleNickname(rawContactId, bean))
    }

    if (bean.eventList.isNotEmpty()){//插入事件
        for (item in bean.eventList) {
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleEvent(rawContactId, item))
        }
    }
}

/** 组装基础数据 */
private fun assembleBase(rawContactId: String, mimeType: String): ContentValues {
    val values = ContentValues()
    values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
    values.put(ContactsContract.Data.MIMETYPE, mimeType)
    return values
}

/** 组装姓名相关数据 */
private fun assembleStructuredName(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, bean.name.ifEmpty { bean.givenName })
    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, bean.givenName.ifEmpty { bean.name })
    if (bean.phonetic.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME, bean.phonetic)
    }
    values.put(
        ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE,
        bean.fullNameStyle.ifEmpty { ContactsContract.FullNameStyle.CJK.toString() }
    )
    values.put(
        ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE,
        bean.phoneticNameStyle.ifEmpty { ContactsContract.PhoneticNameStyle.PINYIN.toString() }
    )
    return values
}

/** 组装公司组织相关数据 */
private fun assembleOrganization(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
    if (bean.company.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Organization.COMPANY, bean.company)
    }
    if (bean.title.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Organization.TITLE, bean.title)
    }
    return values
}

/** 组装地址相关数据 */
private fun assembleStructuredPostal(rawContactId: String, bean: ContactsPostalBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, bean.address.ifEmpty { bean.street })
    values.put(ContactsContract.CommonDataKinds.StructuredPostal.STREET, bean.street.ifEmpty { bean.address })
    val type = bean.type.ifEmpty { ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME.toString() }
    if (type != ContactsContract.CommonDataKinds.StructuredPostal.TYPE_CUSTOM.toString()){
        bean.label = ""
    }
    values.put(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, type)
    if (bean.label.isNotEmpty()) {
        values.put(ContactsContract.CommonDataKinds.StructuredPostal.LABEL, bean.label)
    }
    return values
}

/** 组装备注相关数据 */
private fun assembleNote(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Note.NOTE, bean.note)
    return values
}

/** 组装头像相关数据 */
private fun assemblePhoto(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bean.avatarArray)
    return values
}

/** 组装邮箱相关数据 */
private fun assembleEmail(rawContactId: String, bean: ContactsEmailBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, bean.address)
    val type = bean.type.ifEmpty { ContactsContract.CommonDataKinds.Email.TYPE_HOME.toString() }
    if (type != ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM.toString()){
        bean.label = ""
    }
    values.put(ContactsContract.CommonDataKinds.Email.TYPE, type)
    if (bean.label.isNotEmpty()) {
        values.put(ContactsContract.CommonDataKinds.Email.LABEL, bean.label)
    }
    return values
}

/** 组装手机相关数据 */
private fun assemblePhone(rawContactId: String, bean: ContactsPhoneBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, bean.number)
    val normalizedNumber = when {
        bean.normalizedNumber.isNotEmpty() -> bean.normalizedNumber
        bean.number.isNotEmpty() -> "+86".append(bean.number.trim())
        else -> ""
    }
    if (normalizedNumber.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER, normalizedNumber)
    }
    if (bean.from.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Phone.DATA6, bean.from)
    }
    val type = bean.type.ifEmpty { ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE.toString() }
    if (type != ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM.toString()){
        bean.label = ""
    }
    values.put(ContactsContract.CommonDataKinds.Phone.TYPE, type)
    if (bean.label.isNotEmpty()) {
        values.put(ContactsContract.CommonDataKinds.Phone.LABEL, bean.label)
    }
    return values

}

/** 组装网站相关数据 */
private fun assembleWebsite(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Website.URL, bean.website)
    values.put(
        ContactsContract.CommonDataKinds.Website.TYPE,
        bean.websiteType.ifEmpty { ContactsContract.CommonDataKinds.Website.TYPE_OTHER.toString() }
    )
    return values
}

/** 组装即时通讯相关数据 */
private fun assembleIm(rawContactId: String, bean: ContactsImBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Im.DATA, bean.account)
    values.put(
        ContactsContract.CommonDataKinds.Im.TYPE,
        bean.type.ifEmpty { ContactsContract.CommonDataKinds.Im.TYPE_OTHER.toString() }
    )

    val protocol = bean.protocol.ifEmpty { ContactsContract.CommonDataKinds.Im.PROTOCOL_AIM.toString() }
    if (protocol != ContactsContract.CommonDataKinds.Im.PROTOCOL_CUSTOM.toString()){
        bean.customProtocolName = ""
    }
    values.put(ContactsContract.CommonDataKinds.Im.PROTOCOL, protocol)
    if (bean.customProtocolName.isNotEmpty()) {
        values.put(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL, bean.customProtocolName)
    }
    return values
}

/** 组装关系相关数据 */
private fun assembleRelation(rawContactId: String, bean: ContactsRelationBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Relation.NAME, bean.name)
    val type = bean.type.ifEmpty { ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT.toString() }
    if (type != ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM.toString()) {
        bean.label = ""
    }
    values.put(ContactsContract.CommonDataKinds.Relation.TYPE, type)
    if (bean.label.isNotEmpty()) {
        values.put(ContactsContract.CommonDataKinds.Relation.LABEL, bean.label)
    }
    return values
}

/** 组装群组ID相关数据 */
private fun assembleGroupMembership(rawContactId: String, groupId: String): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, groupId)
    return values
}

/** 组装昵称相关数据 */
private fun assembleNickname(rawContactId: String, bean: ContactsInfoBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Nickname.NAME, bean.nickName)
    return values
}

/** 组装事件相关数据 */
private fun assembleEvent(rawContactId: String, bean: ContactsEventBean): ContentValues {
    val values = assembleBase(rawContactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
    values.put(ContactsContract.CommonDataKinds.Event.START_DATE, bean.date)
    values.put(ContactsContract.CommonDataKinds.Event.TYPE, bean.type.ifEmpty { ContactsContract.CommonDataKinds.Event.TYPE_OTHER.toString() })
    return values
}


