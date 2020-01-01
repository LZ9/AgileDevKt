package com.lodz.android.pandora.widget.vp2

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.corekt.anko.dp2px

/**
 * ViewPager2扩展类
 * @author zhouL
 * @date 2020/1/1
 */

/** 设置RV的间距 */
fun ViewPager2.setRvPadding(dp: Int): Unit = setRvPadding(dp, dp, dp, dp)

/** 设置RV的间距 */
fun ViewPager2.setRvPadding(leftDp: Int, topDp: Int, rightDp: Int, bottomDp: Int) {
    val recyclerView = getChildAt(0)
    if (recyclerView is RecyclerView) {
        recyclerView.setPadding(dp2px(leftDp), dp2px(topDp), dp2px(rightDp), dp2px(bottomDp))
        recyclerView.clipToPadding = false
    }
}

/** 设置RV的间距，横向滑动时上下贴边，纵向滑动时左右贴边 */
fun ViewPager2.setRvPaddingByOrientation(dp: Int){
    val recyclerView = getChildAt(0)
    if (recyclerView is RecyclerView) {
        if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            recyclerView.setPadding(dp2px(dp), 0, dp2px(dp), 0)
        } else {
            recyclerView.setPadding(0, dp2px(dp), 0, dp2px(dp))
        }
        recyclerView.clipToPadding = false
    }
}