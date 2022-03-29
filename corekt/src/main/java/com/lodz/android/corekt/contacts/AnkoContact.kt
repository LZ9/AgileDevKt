package com.lodz.android.corekt.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.provider.ContactsContract

/**
 * 通讯录帮助类
 * @author zhouL
 * @date 2022/3/28
 */
@SuppressLint("Range")
fun Context.getAllContacts(): ArrayList<ContactsInfoBean> {
    val list = ArrayList<ContactsInfoBean>()

    // 查询联系人Contacts表得到所有ContactsID
    contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        null,
        null,
        null,
        null
    )?.use {contactsCursor->
        while (contactsCursor.moveToNext()){
            val id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))

            //查询联系人RawContacts表根据ContactsID获取RawContactsID
            contentResolver.query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.RawContacts.CONTACT_ID + "=" + id,
                null,
                null
            )?.use { rawCursor ->
                while (rawCursor.moveToNext()) {
                    val rawId = rawCursor.getString(rawCursor.getColumnIndex(ContactsContract.RawContacts._ID))
                    val bean = ContactsInfoBean()

                    //查询联系人Data表根据RawContactsID获取该联系人的各数据
                    contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.RAW_CONTACT_ID + "=" + rawId,
                        null,
                        null
                    )?.use { dataCursor ->
                        if (dataCursor.moveToFirst()){
                            do {
                                val mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                                if (mimeType == ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE){
                                    val item = ContactsPhoneBean()
                                    item.number = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: ""
                                    item.normalizedNumber = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER)) ?: ""
                                    item.from = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA6)) ?: ""
                                    bean.phoneList.add(item)
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE){
                                    bean.company = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)) ?: ""
                                    bean.title = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)) ?: ""
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE){
                                    bean.postal = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)) ?: ""
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE){
                                    bean.emailList.add(dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)) ?: "")
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE){
                                    bean.note = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)) ?: ""
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE){
                                    bean.name = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)) ?: ""
                                }
                                if (mimeType == ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE){
                                    val array = dataCursor.getBlob(dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))
                                    if (array != null) {
                                        bean.avatarArray = array
                                        bean.avatarBitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
                                    }
                                }
                            }while (dataCursor.moveToNext())
                        }
                    }
                    list.add(bean)
                }
            }
        }
    }
    return list
}

fun getContactData(rawContactId: Int) {

}