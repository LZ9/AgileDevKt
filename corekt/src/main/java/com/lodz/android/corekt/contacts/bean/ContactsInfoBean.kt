package com.lodz.android.corekt.contacts.bean

import com.lodz.android.corekt.contacts.bean.data.*

/**
 * 通讯录信息数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsInfoBean {

    var contactId = ""

    var rawContactId = ""

    /** 图片 */
    var photoBean: ContactsPhotoBean = ContactsPhotoBean()

    /** 姓名 */
    var nameBean: ContactsNameBean = ContactsNameBean()

    /** 组织 */
    var organizationBean: ContactsOrganizationBean = ContactsOrganizationBean()

    /** 地址列表 */
    var postalList: ArrayList<ContactsPostalBean> = arrayListOf()

    /** 手机列表 */
    var phoneList: ArrayList<ContactsPhoneBean> = arrayListOf()

    /** 邮箱列表 */
    var emailList: ArrayList<ContactsEmailBean> = arrayListOf()

    /** 备注 */
    var noteBean: ContactsNoteBean = ContactsNoteBean()

    /** 网站列表 */
    var websiteList: ArrayList<ContactsWebsiteBean> = arrayListOf()

    /** 即时通讯列表 */
    var imList: ArrayList<ContactsImBean> = arrayListOf()

    /** 关系列表 */
    var relationList: ArrayList<ContactsRelationBean> = arrayListOf()

    /** 群组列表 */
    var groupList: ArrayList<ContactsGroupMembershipBean> = arrayListOf()

    /** 昵称 */
    var nicknameBean: ContactsNicknameBean = ContactsNicknameBean()

    /** 事件列表 */
    var eventList: ArrayList<ContactsEventBean> = arrayListOf()
}