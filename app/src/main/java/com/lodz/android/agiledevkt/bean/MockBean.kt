package com.lodz.android.agiledevkt.bean

/**
 * Mock数据实体
 * @author zhouL
 * @date 2022/1/19
 */
class MockBean<T> {
    var id = 0
    var gzdw = ""
    var loginName = ""
    var mobile = ""
    var name = ""
    var phone = ""
    var zzdh = ""
    var ssxzjd = ""
    var sssq = ""
    var roles = ArrayList<T>()

    override fun toString(): String {
        return "MockBean(id=$id, gzdw='$gzdw', loginName='$loginName', mobile='$mobile', name='$name', phone='$phone', zzdh='$zzdh', ssxzjd='$ssxzjd', sssq='$sssq', roles=$roles)"
    }
}