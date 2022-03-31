package com.lodz.android.corekt.contacts

import android.graphics.Bitmap

/**
 * 通讯录信息数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsInfoBean {

    var contactId = ""

    var rawContactId = ""

    /** 头像字节数组 */
    var avatarArray: ByteArray? = null

    /** 头像 */
    var avatarBitmap: Bitmap? = null

    /** 姓名 */
    var name = ""

    /** 公司 */
    var company = ""

    /** 职位 */
    var title = ""

    /** 单位 */
    var postal = ""

    /** 手机列表 */
    var phoneList: ArrayList<ContactsPhoneBean> = arrayListOf()

    /** 邮箱列表 */
    var emailList : ArrayList<String> = arrayListOf()

    /** 备注 */
    var note = ""

}