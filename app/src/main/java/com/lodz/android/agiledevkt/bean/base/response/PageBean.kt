package com.lodz.android.agiledevkt.bean.base.response

/**
 * 分页包装类
 * @author zhouL
 * @date 2021/12/27
 */
class PageBean<T> {

    companion object {
        /** 默认每页数量 */
        const val DEFAULT_PAGE_SIZE = 10

        /** 默认起始页码 */
        const val DEFAULT_START_PAGE_NUM = 1

        /** 默认总数 */
        const val DEFAULT_TOTAL = 30
    }

    /** 数据 */
    var data: T? = null

    /** 每页数量 */
    var pageSize = DEFAULT_PAGE_SIZE

    /** 当前页码 */
    var pageNum = DEFAULT_START_PAGE_NUM

    /** 总数 */
    var total = DEFAULT_TOTAL
}