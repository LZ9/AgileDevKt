package com.lodz.android.agiledevkt.modules.menubar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.menu.BottomMenuBar
import com.lodz.android.pandora.widget.menu.MenuConfig

/**
 * 底部菜单栏测试类
 * @author zhouL
 * @date 2019/2/25
 */
class MenuBarTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MenuBarTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val TYPE_HOME = 1
    private val TYPE_WORK = 2
    private val TYPE_MSG = 3
    private val TYPE_MINE = 4


    /** 提示控件 */
    private val mTipsTv by bindView<TextView>(R.id.tips_tv)
    /** 底部菜单栏 */
    private val mMenuBar by bindView<BottomMenuBar>(R.id.menu_bar)


    override fun getLayoutId(): Int = R.layout.activity_menu_bar_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        mMenuBar.setMenuConfigs(getConfigs())
        mMenuBar.setSelectedMenu(TYPE_HOME)
        mTipsTv.text = "首页"
    }

    private fun getConfigs(): List<MenuConfig> {
        val list = ArrayList<MenuConfig>()
        list.add(createMenuConfig(TYPE_HOME, "首页", 8))
        list.add(createMenuConfig(TYPE_WORK, "功能", 0))
        list.add(createMenuConfig(TYPE_MSG, "消息", 87))
        list.add(createMenuConfig(TYPE_MINE, "我的", 12))
        return list
    }

    private fun createMenuConfig(type: Int, text: String, num: Int): MenuConfig {
        val config = MenuConfig()
        config.setType(type)
        config.setIconSize(30)
        config.setIconResId(getContext(), R.drawable.pandora_ic_take_photo_cancel, R.drawable.pandora_ic_take_photo_confirm)
        config.setText(text)
        config.setTextColor(getContext(), R.color.white, R.color.color_b3e5fc)
//        config.setTextSize(18f)
        config.setNum(num)
//        config.setNumTextColor(getContext(), R.color.color_00a0e9)
//        config.setNumTextSize(15f)
//        config.setNumBackground(R.drawable.bg_ffffff_circle)
        return config
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mMenuBar.setOnSelectedListener { type ->
            mTipsTv.text =when(type){
                TYPE_HOME -> "首页"
                TYPE_WORK -> "功能"
                TYPE_MSG -> "消息"
                TYPE_MINE -> "我的"
                else -> ""
            }
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}