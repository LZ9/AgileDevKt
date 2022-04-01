package com.lodz.android.corekt.contacts.bean

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

    /** 姓名 */
    var name = ""

    /** 别名（通常和name一样） */
    var givenName = ""

    /** 拼音 */
    var phonetic = ""

    /** 全名风格（通常是ContactsContract.FullNameStyle.CJK） */
    var fullNameStyle = ""

    /** 拼音风格（通常是ContactsContract.PhoneticNameStyle.PINYIN） */
    var phoneticNameStyle = ""

    /** 公司 */
    var company = ""

    /** 职位 */
    var title = ""

    /** 地址列表 */
    var postalList: ArrayList<ContactsPostalBean> = arrayListOf()

    /** 手机列表 */
    var phoneList: ArrayList<ContactsPhoneBean> = arrayListOf()

    /** 邮箱列表 */
    var emailList: ArrayList<ContactsEmailBean> = arrayListOf()

    /** 备注 */
    var note = ""

    /** 网站 */
    var website = ""

    /** 网站类型（ContactsContract.CommonDataKinds.Website.TYPE_OTHER） */
    var websiteType = ""

    /** 即时通讯列表 */
    var imList: ArrayList<ContactsImBean> = arrayListOf()

    /** 关系列表 */
    var relationList: ArrayList<ContactsRelationBean> = arrayListOf()

    /** 群组ID列表 */
    var groupRowIdList: ArrayList<String> = arrayListOf()

    /** 昵称 */
    var nickName = ""

    /** 事件列表 */
    var eventList: ArrayList<ContactsEventBean> = arrayListOf()
}