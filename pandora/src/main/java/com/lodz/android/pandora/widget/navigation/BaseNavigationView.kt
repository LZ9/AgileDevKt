package com.lodz.android.pandora.widget.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.view.menu.MenuItemImpl
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lodz.android.corekt.utils.ReflectUtils
import com.lodz.android.pandora.R

/**
 * 基础导航栏
 * @author zhouL
 * @date 2019/10/22
 */
class BaseNavigationView : BottomNavigationView {

    companion object {
        /** 不需要角标 */
        const val BADGE_NONE_MODE = 0
        /** 数字角标 */
        const val BADGE_ALL_NUM_MODE = 1
        /** 红点角标 */
        const val BADGE_ALL_POINT_MODE = 2
        /** 红点角标 */
        const val BADGE_CUSTOM_MODE = 3
    }

    /** 默认的ItemIconTintList */
    private var mPdrDefaultItemIconTintList: ColorStateList? = null
    /** Item布局列表 */
    private var mPdrItemViews: ArrayList<BottomNavigationItemView>? = null
    /** 角标模式 */
    private var mPdrBadgeMode = BADGE_NONE_MODE

    constructor(context: Context) : super(context){
        init(null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        configLayout(attrs)
    }

    private fun configLayout(attrs: AttributeSet?) {
        var typedArray: TypedArray? = null
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseNavigationView)
        }
        // 是否显示原始图标图案
        mPdrDefaultItemIconTintList = itemIconTintList
        val isShowOriginalIcon: Boolean = typedArray?.getBoolean(R.styleable.BaseNavigationView_isShowOriginalIcon, true) ?: true
        setShowOriginalIcon(isShowOriginalIcon)

        // 角标类型
        configBadgeLayout()
        val badgeMode: Int = typedArray?.getInt(R.styleable.BaseNavigationView_badgeMode, BADGE_NONE_MODE) ?: BADGE_NONE_MODE
        setBadgeMode(badgeMode)
        typedArray?.recycle()
    }

    /** 配置默认的角标布局 */
    private fun configBadgeLayout() {
        val menuView = ReflectUtils.getFieldValue<BottomNavigationView>(this, "menuView") as? BottomNavigationMenuView
        if (menuView != null) {
            val itemView = ReflectUtils.getFieldValue<BottomNavigationMenuView>(menuView, "buttons") as? Array<*>
            if (itemView != null) {
                mPdrItemViews = ArrayList()
                itemView.forEach { any ->
                    if (any is BottomNavigationItemView) {
                        mPdrItemViews?.add(any)
                    }
                }
            }
        }
        setBadgeLayout(R.layout.pandora_view_badge)
    }

    /** 设置显示原始图标[isShow] */
    fun setShowOriginalIcon(isShow: Boolean) {
        itemIconTintList = if (isShow) null else mPdrDefaultItemIconTintList
    }

    /** 设置角标布局[view] */
    fun setBadgeLayout(view: View) {
        mPdrItemViews?.forEachIndexed { index, itemView ->
            itemView.addView(view)
        }
    }

    /** 设置角标布局资源id[layoutId] */
    fun setBadgeLayout(@LayoutRes layoutId: Int) {
        mPdrItemViews?.forEachIndexed { index, itemView ->
            itemView.addView(LayoutInflater.from(context).inflate(layoutId, itemView, false))
        }
    }

    /** 设置角标类型[badgeMode] */
    fun setBadgeMode(badgeMode: Int) {
        mPdrBadgeMode = badgeMode
    }

    /** 设置Item的角标数字，位置[position]，数字[num] */
    fun setItemBadgeNum(position: Int, num: Int) {
        if (mPdrBadgeMode == BADGE_NONE_MODE) {
            return
        }
        if (mPdrBadgeMode == BADGE_ALL_POINT_MODE) {
            return
        }
        if (position < mPdrItemViews?.size ?: 0) {
            val pointView = mPdrItemViews?.get(position)?.findViewById<View>(R.id.pdr_point_view)
            if (pointView != null) {
                pointView.visibility = View.GONE
            }
            val numTv = mPdrItemViews?.get(position)?.findViewById<TextView>(R.id.pdr_num_tv)
            if (numTv != null) {
                numTv.text = num.toString()
                numTv.visibility = if (num == 0) View.GONE else View.VISIBLE
            }
        }
    }

    /** 设置Item的角标数字，菜单项[item]，数字[num] */
    fun setItemBadgeNum(item: MenuItem, num: Int) {
        mPdrItemViews?.forEachIndexed { index, itemView ->
            val itemData = ReflectUtils.getFieldValue<BottomNavigationItemView>(itemView, "itemData") as? MenuItemImpl
            if (itemData != null){
                if (itemData.itemId == item.itemId){
                    setItemBadgeNum(index, num)
                }
            }
        }
    }

    /** 设置Item的角标红点提示，位置[position]，是否显示[isVisibility] */
    fun setItemBadgePoint(position: Int, isVisibility: Boolean) {
        if (mPdrBadgeMode == BADGE_NONE_MODE) {
            return
        }
        if (mPdrBadgeMode == BADGE_ALL_NUM_MODE) {
            return
        }
        if (position < mPdrItemViews?.size ?: 0) {
            val numTv = mPdrItemViews?.get(position)?.findViewById<TextView>(R.id.pdr_num_tv)
            if (numTv != null) {
                numTv.visibility = View.GONE
            }
            val pointView = mPdrItemViews?.get(position)?.findViewById<View>(R.id.pdr_point_view)
            if (pointView != null) {
                pointView.visibility = if (isVisibility) View.VISIBLE else View.GONE
            }
        }
    }

    /** 设置Item的角标红点提示，菜单项[item]，是否显示[isVisibility] */
    fun setItemBadgePoint(item: MenuItem, isVisibility: Boolean) {
        mPdrItemViews?.forEachIndexed { index, itemView ->
            val itemData = ReflectUtils.getFieldValue<BottomNavigationItemView>(itemView, "itemData") as? MenuItemImpl
            if (itemData != null){
                if (itemData.itemId == item.itemId){
                    setItemBadgePoint(index, isVisibility)
                }
            }
        }
    }

}