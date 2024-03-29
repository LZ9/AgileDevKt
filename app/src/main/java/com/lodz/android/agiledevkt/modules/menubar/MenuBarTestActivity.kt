package com.lodz.android.agiledevkt.modules.menubar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityMenuBarTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.menu.MenuConfig
import kotlin.random.Random

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

    private val mBinding: ActivityMenuBarTestBinding by bindingLayout(ActivityMenuBarTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initMenuBar()
        initTabLayout()
        initBaseNavigation()
        initNavigationView()
    }

    private fun initMenuBar() {
        mBinding.menuBar.setMenuConfigs(getConfigs())
        mBinding.menuBar.setSelectedMenu(TYPE_HOME)
    }

    private fun getConfigs(): List<MenuConfig> {
        val list = ArrayList<MenuConfig>()
        list.add(createMenuConfig(TYPE_HOME, "首页阿萨现在才王鹏噢四点", 8))
        list.add(createMenuConfig(TYPE_WORK, "功能", 0, View.VISIBLE))
        list.add(createMenuConfig(TYPE_MSG, "消息", 87))
        list.add(createMenuConfig(TYPE_MINE, "我的", 0, badgeImgVisibility = View.VISIBLE))
        return list
    }

    private fun createMenuConfig(
        type: Int,
        text: String,
        num: Int,
        pointVisibility: Int = View.GONE,
        badgeImgVisibility: Int = View.GONE
    ): MenuConfig {
        val config = MenuConfig()
        config.type = type
        config.iconSizeDp = 30
        config.setIconResId(getContext(), R.drawable.ic_person, R.drawable.ic_person_sel)

        config.text = text
        config.setTextColor(getContext(), R.color.color_666666, R.color.color_00a0e9)
        config.textSizeSp = 12f
        config.drawablePaddingPx = dp2px(2)
//        config.textWidthPx = dp2px(70)
        config.textEllipsize = TextUtils.TruncateAt.END
//        config.textMaxLength = 3

        config.num = num
//        config.setNumTextColor(getContext(), R.color.color_00a0e9)
//        config.numTextSizeSp = 15f
//        config.numBackgroundDrawableResId = R.drawable.bg_ffffff_circle
//        config.numTextAngle = 90
//        config.numTextRadiusPx = dp2px(5)

        config.pointVisibility = pointVisibility
//        config.pointAngle = 90
//        config.pointRadiusPx = dp2px(5)

        config.badgeImgVisibility = badgeImgVisibility
        config.badgeImgResId = R.drawable.pandora_ic_take_photo_cancel
        config.badgeImgWidthPx = dp2px(16)
        config.badgeImgHeightPx = dp2px(16)
//        config.badgeImgAngle = 90
//        config.badgeImgRadiusPx = dp2px(5)

        config.isUseIndicator = true
        config.indicatorHeightPx = dp2px(3)
        config.indicatorMarginStartPx = dp2px(30)
        config.indicatorMarginEndPx = dp2px(30)
        config.setIndicatorColor(getContext(), R.color.color_00a0e9)
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
            val pointView = tabView.findViewById<View>(R.id.point_view)
            val badgeImg = tabView.findViewById<ImageView>(R.id.badge_img)

            iconImg.setImageResource(R.drawable.selector_ic_tab)
            textTv.text = title
            numTv.text = (i + 1).toString()
            numTv.visibility = if (i % 2 == 1) View.VISIBLE else View.GONE

            pointView.visibility = if (i == 0) View.VISIBLE else View.GONE

            badgeImg.visibility = if (i == 2) View.VISIBLE else View.GONE
            badgeImg.setImageResource(R.drawable.pandora_ic_take_photo_cancel)

            tabView.tag = i + 1

            mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setCustomView(tabView), i == 0)
        }
        mBinding.tabTv.text = getTimeText("首页")
    }

    private fun initBaseNavigation() {
        mBinding.baseBnv.setItemBadgeNum(0, 2)
        mBinding.baseBnv.setItemBadgeImg(
            mBinding.baseBnv.menu.getItem(1),
            View.VISIBLE,
            R.drawable.pandora_ic_take_photo_cancel,
            dp2px(17),
            dp2px(17)
        )
        mBinding.baseBnv.setItemBadgePoint(2, View.VISIBLE)
        mBinding.baseBnv.setItemBadgePoint(mBinding.baseBnv.menu.getItem(3), View.VISIBLE)
    }

    private fun initNavigationView() {
        mBinding.bottomNv.itemIconTintList = null
        mBinding.bottomNv.getOrCreateBadge(R.id.msg_tab).apply {
            backgroundColor = Color.RED
            badgeTextColor = Color.WHITE
            number = 12
        }
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val textTv = tab?.customView?.findViewById<TextView>(R.id.text_tv)
                if (textTv != null){
                    mBinding.tabTv.text = getTimeText(textTv.text.toString())
                }
                val type = tab?.customView?.tag as? Int
                if (type != null && (type == TYPE_WORK || type == TYPE_MINE)) {
                    val numTv = tab.customView?.findViewById<TextView>(R.id.num_tv)
                    if (numTv != null) {
                        val num = Random.nextInt(20)
                        numTv.visibility = (num == 0).then { View.GONE } ?: View.VISIBLE
                        numTv.text = num.toString()
                    }
                }
            }
        })

        // 底部菜单栏设置菜单选中监听器
        mBinding.menuBar.setOnMenuSelectedListener { view, menuConfig ->
            mBinding.tabTv.text = when (menuConfig.type) {
                TYPE_HOME -> getTimeText("首页")
                TYPE_WORK -> getTimeText("功能")
                TYPE_MSG -> getTimeText("消息")
                TYPE_MINE -> getTimeText("我的")
                else -> ""
            }
        }

        // 底部菜单栏设置菜单点击监听器
        mBinding.menuBar.setOnMenuClickListener { view, menuConfig ->
            if (menuConfig.type == TYPE_WORK) {
                mBinding.menuBar.updatePointVisibility(
                    menuConfig.type,
                    visibility = (menuConfig.pointVisibility == View.GONE).then { View.VISIBLE }?: View.GONE)
                return@setOnMenuClickListener true
            }

            if (menuConfig.type == TYPE_MINE) {
                mBinding.menuBar.updateBadgeImgVisibility(
                    menuConfig.type,
                    visibility = (menuConfig.badgeImgVisibility == View.GONE).then { View.VISIBLE }?: View.GONE)
                return@setOnMenuClickListener true
            }
            return@setOnMenuClickListener false
        }

        // 底部菜单栏设置菜单长按监听器
        mBinding.menuBar.setOnMenuLongClickListener { view, menuConfig ->
            SnackbarUtils.createShort(view, menuConfig.text).show()
            return@setOnMenuLongClickListener true
        }

        mBinding.menuBar.setOnMenuBadgeImgClickListener { view, menuConfig ->
            mBinding.menuBar.cleanMenu()
            val list = ArrayList<MenuConfig>()
            list.add(createMenuConfig(TYPE_HOME, "首页", 1))
            list.add(createMenuConfig(TYPE_WORK, "功能", 0, View.VISIBLE))
            list.add(createMenuConfig(TYPE_MSG, "消息", 2))
            mBinding.menuBar.setMenuConfigs(list)
        }

        mBinding.baseBnv.setOnItemSelectedListener { item ->
            mBinding.tabTv.text = when (item.itemId) {
                R.id.home_tab -> {
                    mBinding.baseBnv.setItemBadgeNum(item, Random.nextInt(20))
                    getTimeText("首页")
                }
                R.id.module_tab -> getTimeText("功能")
                R.id.msg_tab -> getTimeText("消息")
                R.id.mine_tab -> getTimeText("我的")
                else -> ""
            }
            return@setOnItemSelectedListener true
        }

        mBinding.bottomNv.setOnItemSelectedListener { item ->
            mBinding.bottomNv.getBadge(item.itemId)?.isVisible = !(mBinding.bottomNv.getBadge(item.itemId)?.isVisible ?: true)
            mBinding.tabTv.text = when (item.itemId) {
                R.id.home_tab ->  getTimeText("首页")
                R.id.module_tab -> getTimeText("功能")
                R.id.msg_tab -> getTimeText("消息")
                R.id.mine_tab -> getTimeText("我的")
                else -> ""
            }
            return@setOnItemSelectedListener true
        }

        mBinding.currentSelectedBtn.setOnClickListener {
            val text = "menuBar : ".append(mBinding.menuBar.getCurrentSelectedType()).append("\n")
                .append("tabLayout : ").append(mBinding.tabLayout.selectedTabPosition.toString()).append("\n")
                .append("baseBnv : ").append(mBinding.baseBnv.selectedItemId.toString()).append("\n")
                .append("bottomNv : ").append(mBinding.bottomNv.selectedItemId.toString())
            toastShort(text)
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun getTimeText(text: String) :String = DateUtils.getCurrentFormatString(DateUtils.TYPE_10).append("\n").append(text)
}