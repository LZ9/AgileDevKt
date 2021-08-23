package com.lodz.android.agiledevkt.bean

/**
 * 用户信息
 * @author zhouL
 * @date 2021/8/20
 */
class UserBean {
    /** 账号 */
    var account: String = ""

    /** 密码 */
    var pswd: String = ""

    constructor()

    constructor(account: String, pswd: String) {
        this.account = account
        this.pswd = pswd
    }
}