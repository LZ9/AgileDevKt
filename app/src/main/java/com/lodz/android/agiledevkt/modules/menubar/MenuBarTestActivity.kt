package com.lodz.android.agiledevkt.modules.menubar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.menu.BottomMenuBar
import com.lodz.android.pandora.widget.menu.MenuConfig
import com.lodz.android.pandora.widget.navigation.BaseNavigationView

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

    /** 首页 */
    private val TYPE_HOME = 1
    /** 功能 */
    private val TYPE_WORK = 2
    /** 消息 */
    private val TYPE_MSG = 3
    /** 我的 */
    private val TYPE_MINE = 4

    /** 提示控件 */
    private val mTabTv by bindView<TextView>(R.id.tab_tv)
    /** TabLayout */
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    /** 底部菜单栏 */
    private val mMenuBar by bindView<BottomMenuBar>(R.id.menu_bar)
    /** 底部菜单栏 */
    private val mBottomNavigationView by bindView<BaseNavigationView>(R.id.bottom_nv)


    override fun getLayoutId(): Int = R.layout.activity_menu_bar_test

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initMenuBar()
        initTabLayout()
        initNavigation()
    }

    private fun initMenuBar() {
        mMenuBar.setMenuConfigs(getConfigs())
        mMenuBar.setSelectedMenu(TYPE_HOME)
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
        config.setIconResId(getContext(), R.drawable.ic_person, R.drawable.ic_person_sel)
        config.setText(text)
        config.setTextColor(getContext(), R.color.color_666666, R.color.color_00a0e9)
//        config.setTextSize(18f)
        config.setNum(num)
//        config.setNumTextColor(getContext(), R.color.color_00a0e9)
//        config.setNumTextSize(15f)
//        config.setNumBackground(R.drawable.bg_ffffff_circle)
        config.setDrawablePadding(dp2px(2))
        return config
    }

    private fun initTabLayout() {
        val titles = arrayListOf(
            getString(R.string.menu_bar_home), getString(R.string.menu_bar_module),
            getString(R.string.menu_bar_msg), getString(R.string.menu_bar_mine)
        )
        titles.forEachIndexed { i, title ->
            val tabView = LayoutInflater.from(getContext()).inflate(R.layout.view_tab, null)
            val iconImg = tabView.findViewById<ImageView>(R.id.ic_img)
            val textTv = tabView.findViewById<TextView>(R.id.text_tv)
            val numTv = tabView.findViewById<TextView>(R.id.num_tv)

            iconImg.setImageResource(R.drawable.selector_ic_tab)
            textTv.text = title
            numTv.text = (i + 1).toString()
            numTv.visibility = if (i % 2 == 1) View.VISIBLE else View.GONE
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(tabView), i == 0)
        }
        mTabTv.text = "首页"
    }

    private fun initNavigation() {
        mBottomNavigationView.setItemBadgeNum(0, 2)
        mBottomNavigationView.setItemBadgePoint(2, true)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mTabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textTv = tab?.customView?.findViewById<TextView>(R.id.text_tv)
                if (textTv != null){
                    mTabTv.text = textTv.text
                }
//                val numTv = tab?.customView?.findViewById<TextView>(R.id.num_tv)
//                if (numTv != null){
//                    numTv.visibility = View.GONE
//                }
            }
        })

        mMenuBar.setOnSelectedListener { type ->
            mTabTv.text =when(type){
                TYPE_HOME -> "首页"
                TYPE_WORK -> "功能"
                TYPE_MSG -> "消息"
                TYPE_MINE -> "我的"
                else -> ""
            }
        }

        mBottomNavigationView.setOnNavigationItemSelectedListener { item ->
            mTabTv.text =when(item.itemId){
                R.id.develop_tab -> "首页"
                R.id.product_tab -> "功能"
                R.id.project_tab -> "消息"
                R.id.personnel_tab -> "我的"
                else -> ""
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}