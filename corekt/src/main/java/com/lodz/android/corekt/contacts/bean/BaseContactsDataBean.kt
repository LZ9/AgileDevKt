package com.lodz.android.corekt.contacts.bean

/**
 * 通讯录基础数据实体
 * @author zhouL
 * @date 2022/4/6
 */
abstract class BaseContactsDataBean {

    /** raw外键id */
    var rawContactId = ""

    /** 数据编号 */
    var dataId = ""

    abstract fun getMimeType(): String

}