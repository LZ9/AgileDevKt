package com.lodz.android.corekt.contacts

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.contacts.bean.*
import com.lodz.android.corekt.contacts.bean.data.*

/**
 * 通讯录帮助类
 * @author zhouL
 * @date 2022/3/28
 */


/** 查询联系人Contacts表得到路径为[uri]的ContactsID（不传获取全部） */
@SuppressLint("Range")
@JvmOverloads
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

/** 查询联系人RawContacts表根据[contactId]和路径[uri]获取RawContactsID（uri默认查询全部） */
@SuppressLint("Range")
@JvmOverloads
fun Context.getContactRawId(contactId: String, uri: Uri = ContactsContract.RawContacts.CONTENT_URI): ArrayList<String> {
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

/** 获取路径为[uri]的通讯录数据（不传获取全部） */
@SuppressLint("Range")
@JvmOverloads
fun Context.getContactData(uri: Uri = ContactsContract.Contacts.CONTENT_URI): ArrayList<ContactsInfoBean> {
    val list = ArrayList<ContactsInfoBean>()
    val idList = getContactId(uri)
    for (contactId in idList) {
        val rawIdList = getContactRawId(contactId)
        for (rawContactId in rawIdList) {
            val bean = getContactDataByRawId(contactId, rawContactId)
            if (bean != null) {
                list.add(bean)
            }
        }
    }
    return list
}

/** 查询联系人Data表根据[contactId]获取该联系人的各数据 */
@SuppressLint("Range")
fun Context.getContactData(contactId: String): ArrayList<ContactsInfoBean> {
    val list = ArrayList<ContactsInfoBean>()
    val rawIdList = getContactRawId(contactId)
    for (rawContactId in rawIdList) {
        val bean = getContactDataByRawId(contactId, rawContactId)
        if (bean != null){
            list.add(bean)
        }
    }
    return list
}


/** 查询联系人Data表根据[contactId]和[rawContactId]获取该联系人的各数据 */
@SuppressLint("Range")
fun Context.getContactDataByRawId(
    contactId: String,
    rawContactId: String
): ContactsInfoBean? = contentResolver.query(
    ContactsContract.Data.CONTENT_URI,
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
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.number = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: ""
                item.normalizedNumber = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)) ?: ""
                item.from = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA6)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)) ?: ""
                bean.phoneList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE) {
                bean.organizationBean.rawContactId = rawContactId
                bean.organizationBean.dataId = dataId
                bean.organizationBean.company = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)) ?: ""
                bean.organizationBean.title = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE) {
                val item = ContactsPostalBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.address = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE)) ?: ""
                item.street = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.LABEL)) ?: ""
                bean.postalList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE) {
                val item = ContactsEmailBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.address = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.LABEL)) ?: ""
                bean.emailList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE) {
                bean.noteBean.rawContactId = rawContactId
                bean.noteBean.dataId = dataId
                bean.noteBean.note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE) {
                bean.nameBean.rawContactId = rawContactId
                bean.nameBean.dataId = dataId
                bean.nameBean.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)) ?: ""
                bean.nameBean.givenName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)) ?: ""
                bean.nameBean.phonetic = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME)) ?: ""
                bean.nameBean.fullNameStyle = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FULL_NAME_STYLE)) ?: ""
                bean.nameBean.phoneticNameStyle = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_NAME_STYLE)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE) {
                bean.photoBean.rawContactId = rawContactId
                bean.photoBean.dataId = dataId
                bean.photoBean.photoArray = dataCursor.getBlob(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
            }
            if (mimeType == ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE) {
                val item = ContactsWebsiteBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.website = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)) ?: ""
                item.websiteType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.TYPE)) ?: ""
                bean.websiteList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE) {
                val item = ContactsImBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.account = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE)) ?: ""
                item.protocol = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL)) ?: ""
                item.customProtocolName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL)) ?: ""
                bean.imList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE) {
                val item = ContactsRelationBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.NAME)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.TYPE)) ?: ""
                item.label = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Relation.LABEL)) ?: ""
                bean.relationList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE) {
                val item = ContactsGroupMembershipBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.groupRowId = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)) ?: ""
                bean.groupList.add(item)
            }
            if (mimeType == ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE) {
                bean.nicknameBean.rawContactId = rawContactId
                bean.nicknameBean.dataId = dataId
                bean.nicknameBean.nickname = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME)) ?: ""
            }
            if (mimeType == ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE) {
                val item = ContactsEventBean()
                item.rawContactId = rawContactId
                item.dataId = dataId
                item.date = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)) ?: ""
                item.type = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)) ?: ""
                bean.eventList.add(item)
            }
        } while (dataCursor.moveToNext())
    }
    return@use bean
}

/** 删除全部通讯录数据 */
fun Context.deleteAllContact() = deleteContact("")

/** 删除[rawContactId]对应的通讯录数据（传空字符串删除全部） */
fun Context.deleteContact(rawContactId: String) {
    if (rawContactId.isNotEmpty()){
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=" + rawContactId, null)
        return
    }
    val idList = getContactId()
    for (contactId in idList) {
        val rawIdList = getContactRawId(contactId)
        for (rawId in rawIdList) {
            contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts._ID + "=" + rawId, null)
        }
    }
}

/** 全量更新通讯录数据 */
fun Context.updateContactAllData(bean: ContactsInfoBean) {
    updateContactStructuredName(bean.nameBean)
    updateContactOrganization(bean.organizationBean)
    for (item in bean.postalList) {
        updateContactStructuredPostal(item)
    }
    updateContactNote(bean.noteBean)
    updateContactPhoto(bean.photoBean)
    updateContactNickname(bean.nicknameBean)
    for (item in bean.emailList) {
        updateContactEmail(item)
    }
    for (item in bean.phoneList) {
        updateContactPhone(item)
    }
    for (item in bean.websiteList) {
        updateContactWebsite(item)
    }
    for (item in bean.imList) {
        updateContactIm(item)
    }
    for (item in bean.relationList) {
        updateContactRelation(item)
    }
    for (item in bean.groupList) {
        updateContactGroupMembership(item)
    }
    for (item in bean.eventList) {
        updateContactEvent(item)
    }
}

/** 更新通讯录姓名相关数据 */
fun Context.updateContactStructuredName(bean :ContactsNameBean) = updateContactIfNotEmpty(bean){ assembleStructuredName(it) }

/** 更新通讯录组织相关数据 */
fun Context.updateContactOrganization(bean :ContactsOrganizationBean) = updateContactIfNotEmpty(bean){ assembleOrganization(it) }

/** 更新通讯录地址相关数据 */
fun Context.updateContactStructuredPostal(bean :ContactsPostalBean) = updateContactIfNotEmpty(bean){ assembleStructuredPostal(it) }

/** 更新通讯录备注相关数据 */
fun Context.updateContactNote(bean :ContactsNoteBean) = updateContactIfNotEmpty(bean){ assembleNote(it) }

/** 更新通讯录图片相关数据 */
fun Context.updateContactPhoto(bean :ContactsPhotoBean) = updateContactIfNotEmpty(bean){ assemblePhoto(it) }

/** 更新通讯录昵称相关数据 */
fun Context.updateContactNickname(bean :ContactsNicknameBean) = updateContactIfNotEmpty(bean){ assembleNickname(it) }

/** 更新通讯录邮箱相关数据 */
fun Context.updateContactEmail(bean :ContactsEmailBean) = updateContactIfNotEmpty(bean){ assembleEmail(it) }

/** 更新通讯录手机相关数据 */
fun Context.updateContactPhone(bean :ContactsPhoneBean) = updateContactIfNotEmpty(bean){ assemblePhone(it) }

/** 更新通讯录网站相关数据 */
fun Context.updateContactWebsite(bean :ContactsWebsiteBean) = updateContactIfNotEmpty(bean){ assembleWebsite(it) }

/** 更新通讯录即时通讯相关数据 */
fun Context.updateContactIm(bean :ContactsImBean) = updateContactIfNotEmpty(bean){ assembleIm(it) }

/** 更新通讯录关系相关数据 */
fun Context.updateContactRelation(bean :ContactsRelationBean) = updateContactIfNotEmpty(bean){ assembleRelation(it) }

/** 更新通讯录群组相关数据 */
fun Context.updateContactGroupMembership(bean :ContactsGroupMembershipBean) = updateContactIfNotEmpty(bean){ assembleGroupMembership(it) }

/** 更新通讯录事件相关数据 */
fun Context.updateContactEvent(bean :ContactsEventBean) = updateContactIfNotEmpty(bean){ assembleEvent(it) }

/** 新增通讯录信息数据[bean] */
fun Context.insertContactData(bean: ContactsInfoBean) {
    val values = ContentValues()
    val uri = contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, values) ?: return
    val rawContactId = ContentUris.parseId(uri).toString()

    if (bean.nameBean.name.isNotEmpty() || bean.nameBean.givenName.isNotEmpty()){//插入姓名
        bean.nameBean.rawContactId = rawContactId
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleStructuredName(bean.nameBean))
    }

    if (bean.organizationBean.company.isNotEmpty() || bean.organizationBean.title.isNotEmpty()){//插入公司和职员
        bean.organizationBean.rawContactId = rawContactId
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleOrganization(bean.organizationBean))
    }

    if (bean.nicknameBean.nickname.isNotEmpty()){//插入昵称
        bean.nicknameBean.rawContactId = rawContactId
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleNickname(bean.nicknameBean))
    }

    if (bean.postalList.isNotEmpty()){//插入地址
        for (item in bean.postalList) {
            if (item.address.isEmpty() && item.street.isEmpty()){
                continue
            }
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleStructuredPostal(item))
        }
    }

    if (bean.noteBean.note.isNotEmpty()){//插入备注
        bean.noteBean.rawContactId = rawContactId
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleNote(bean.noteBean))
    }

    if (bean.photoBean.photoArray != null){//插入头像
        bean.photoBean.rawContactId = rawContactId
        contentResolver.insert(ContactsContract.Data.CONTENT_URI, assemblePhoto(bean.photoBean))
    }

    if (bean.emailList.isNotEmpty()){//插入邮箱
        for (item in bean.emailList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleEmail(item))
        }
    }

    if (bean.phoneList.isNotEmpty()){//插入手机
        for (item in bean.phoneList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assemblePhone(item))
        }
    }

    if (bean.websiteList.isNotEmpty()){//插入网站
        for (item in bean.websiteList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleWebsite(item))
        }
    }

    if (bean.imList.isNotEmpty()){//插入及时通讯
        for (item in bean.imList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleIm(item))
        }
    }

    if (bean.relationList.isNotEmpty()){//插入关系
        for (item in bean.relationList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleRelation(item))
        }
    }

    if (bean.groupList.isNotEmpty()){//插入群组ID
        for (item in bean.groupList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleGroupMembership(item))
        }
    }

    if (bean.eventList.isNotEmpty()){//插入事件
        for (item in bean.eventList) {
            item.rawContactId = rawContactId
            contentResolver.insert(ContactsContract.Data.CONTENT_URI, assembleEvent(item))
        }
    }
}

/** 更新通讯录数据 */
private fun <T : BaseContactsDataBean> Context.updateContactIfNotEmpty(t: T, block: (T) -> ContentValues) {
    if (t.rawContactId.isEmpty() || t.dataId.isEmpty()) {
        return
    }
    contentResolver.update(
        ContactsContract.Data.CONTENT_URI,
        block(t),
        ContactsContract.Data.RAW_CONTACT_ID + "= ? AND " + ContactsContract.Data._ID + "= ? AND " + ContactsContract.Data.MIMETYPE + "= ?",
        arrayOf(t.rawContactId, t.dataId, t.getMimeType())
    )
}

/** 组装基础数据 */
private fun assembleBase(bean: BaseContactsDataBean): ContentValues {
    val values = ContentValues()
    values.put(ContactsContract.Data.RAW_CONTACT_ID, bean.rawContactId)
    values.put(ContactsContract.Data.MIMETYPE, bean.getMimeType())
    return values
}

/** 组装姓名相关数据 */
private fun assembleStructuredName(bean: ContactsNameBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assembleOrganization(bean: ContactsOrganizationBean): ContentValues {
    val values = assembleBase(bean)
    if (bean.company.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Organization.COMPANY, bean.company)
    }
    if (bean.title.isNotEmpty()){
        values.put(ContactsContract.CommonDataKinds.Organization.TITLE, bean.title)
    }
    return values
}

/** 组装地址相关数据 */
private fun assembleStructuredPostal(bean: ContactsPostalBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assembleNote(bean: ContactsNoteBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.Note.NOTE, bean.note)
    return values
}

/** 组装头像相关数据 */
private fun assemblePhoto(bean: ContactsPhotoBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, bean.photoArray)
    return values
}

/** 组装邮箱相关数据 */
private fun assembleEmail(bean: ContactsEmailBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assemblePhone(bean: ContactsPhoneBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assembleWebsite(bean: ContactsWebsiteBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.Website.URL, bean.website)
    values.put(
        ContactsContract.CommonDataKinds.Website.TYPE,
        bean.websiteType.ifEmpty { ContactsContract.CommonDataKinds.Website.TYPE_OTHER.toString() }
    )
    return values
}

/** 组装即时通讯相关数据 */
private fun assembleIm(bean: ContactsImBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assembleRelation(bean: ContactsRelationBean): ContentValues {
    val values = assembleBase(bean)
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
private fun assembleGroupMembership(bean: ContactsGroupMembershipBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, bean.groupRowId)
    return values
}

/** 组装昵称相关数据 */
private fun assembleNickname(bean: ContactsNicknameBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.Nickname.NAME, bean.nickname)
    return values
}

/** 组装事件相关数据 */
private fun assembleEvent(bean: ContactsEventBean): ContentValues {
    val values = assembleBase(bean)
    values.put(ContactsContract.CommonDataKinds.Event.START_DATE, bean.date)
    values.put(ContactsContract.CommonDataKinds.Event.TYPE, bean.type.ifEmpty { ContactsContract.CommonDataKinds.Event.TYPE_OTHER.toString() })
    return values
}