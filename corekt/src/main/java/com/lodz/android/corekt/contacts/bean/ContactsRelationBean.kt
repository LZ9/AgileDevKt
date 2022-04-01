package com.lodz.android.corekt.contacts.bean

/**
 * 通讯录关系数据
 * @author zhouL
 * @date 2022/3/29
 */
class ContactsRelationBean {

    /** 名称 */
    var name = ""

    /** 关系类型（ContactsContract.CommonDataKinds.Relation.TYPE_ASSISTANT） */
    var type = ""

    /** 自定义类型名称（type == ContactsContract.CommonDataKinds.Relation.TYPE_CUSTOM 时有值） */
    var label = ""
}