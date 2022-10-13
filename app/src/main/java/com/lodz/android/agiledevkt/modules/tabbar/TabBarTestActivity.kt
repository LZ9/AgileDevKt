package com.lodz.android.agiledevkt.modules.tabbar

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Typeface
import android.os.Bundle
import com.lodz.android.pandora.base.activity.BaseActivity

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityTabBarTestBinding
import com.lodz.android.agiledevkt.databinding.ViewTabLottieBinding
import com.lodz.android.agiledevkt.modules.dialogfragment.TestFragment
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.getColorStateListCompat
import com.lodz.android.corekt.anko.setupViewPager
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.vp2.SimpleTabAdapter

/**
 * 顶部菜单栏测试类
 * @author zhouL
 * @date 2022/10/12
 */
class TabBarTestActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, TabBarTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val TAB_NAMES_ID = arrayListOf(
        R.string.tabbar_tt,
        R.string.tabbar_jx,
        R.string.tabbar_gz,
        R.string.tabbar_kx
    )

    private val ANIM_MAP = mapOf(
        "party" to R.raw.anim_party,
        "pizza" to R.raw.anim_pizza,
        "apple" to R.raw.anim_apple
    )

    private val mTabNameList = ArrayList<String>()

    private val mBinding: ActivityTabBarTestBinding by bindingLayout(ActivityTabBarTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        for (id in TAB_NAMES_ID) {
            mTabNameList.add(getString(id))
        }
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initViewPager()
        initTabLayout1()//基础样式
        initTabLayout2()//添加icon & 去掉indicator
        initTabLayout3()//style 字体大小、加粗 & 顶部indicator
        initTabLayout4()//下划线样式 & tab分割线
        initTabLayout5()//Badge 数字 & 红点 & 滚动隐藏
        initTabLayout6()//TabLayout样式 & 选中字体加粗
        initTabLayout7()//自定义item view & lottie
    }

    private fun initViewPager() {
        val list = ArrayList<Fragment>()
        for (tabName in mTabNameList) {
            list.add(TestFragment.newInstance(tabName))
        }
        mBinding.viewPager.adapter = SimpleTabAdapter(this, list)
        mBinding.viewPager.offscreenPageLimit = 1
        mBinding.viewPager.setCurrentItem(0, true)
    }


    /** 基础样式 */
    private fun initTabLayout1() {
        mBinding.tabLayout1.setupViewPager(mBinding.viewPager, mTabNameList)
    }

    /** 添加icon & 去掉indicator */
    private fun initTabLayout2() {
        mBinding.tabLayout2.setupViewPager(mBinding.viewPager, mTabNameList)
        for (index in 0..mBinding.tabLayout2.tabCount) {
            mBinding.tabLayout2.getTabAt(index)?.setIcon(R.drawable.ic_collect)
        }
    }

    /** style 字体大小、加粗 & 顶部indicator */
    private fun initTabLayout3() {
        mBinding.tabLayout3.setupViewPager(mBinding.viewPager, mTabNameList)
    }

    /** 下划线样式 & tab分割线 */
    private fun initTabLayout4() {
        mBinding.tabLayout4.setupViewPager(mBinding.viewPager, mTabNameList)
        //设置 分割线
        for (index in 0..mBinding.tabLayout4.tabCount) {
            val linearLayout = mBinding.tabLayout4.getChildAt(index) as? LinearLayout
            linearLayout?.let {
                it.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                it.dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_tab_divider)
                it.dividerPadding = 30
            }
        }
    }

    /** Badge 数字 & 红点 & 滚动隐藏 */
    private fun initTabLayout5() {
        mBinding.tabLayout5.setupViewPager(mBinding.viewPager, mTabNameList)
        // 数字
        mBinding.tabLayout5.getTabAt(1)?.let { tab ->
            tab.orCreateBadge.apply {
                backgroundColor = Color.RED
                maxCharacterCount = 3
                number = 99999
                badgeTextColor = Color.WHITE
            }
        }

        // 红点
        mBinding.tabLayout5.getTabAt(2)?.let { tab ->
            tab.orCreateBadge.backgroundColor = ContextCompat.getColor(this, R.color.red)
        }

        mBinding.tabLayout5.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    it.badge?.isVisible = false
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    /** TabLayout样式 & 选中字体加粗 */
    private fun initTabLayout6() {
        mBinding.tabLayout6.setupViewPager(mBinding.viewPager, mTabNameList)
        mBinding.tabLayout6.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                switchTextStyle(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                switchTextStyle(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    /** 设置文字样式 */
    private fun switchTextStyle(tab: TabLayout.Tab?) {
        tab?.let {
            val textView = it.view.getChildAt(1) as TextView
            textView.typeface = Typeface.defaultFromStyle(if (it.isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    /** 自定义item view & lottie */
    private fun initTabLayout7() {
        ANIM_MAP.keys.forEach { key ->
            val tab = mBinding.tabLayout7.newTab()
            val binding = ViewTabLottieBinding.inflate(layoutInflater, mBinding.tabLayout7, false)
            val rawId = ANIM_MAP[key]
            if (rawId != null) {
                binding.lottieView.setAnimation(rawId)
            }
            binding.lottieView.setColorFilter(Color.BLUE)
            binding.tabNameTv.text = key
            tab.customView = binding.root
            mBinding.tabLayout7.addTab(tab)
        }

        val defaultTab = mBinding.tabLayout7.getTabAt(0)
        defaultTab?.select()
        setTabSelected(defaultTab)

        mBinding.tabLayout7.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                setTabUnselected(tab)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    /** 设置Tab为选中状态 */
    fun setTabSelected(tab: TabLayout.Tab?) {
        tab?.customView?.let {
            it.findViewById<TextView>(R.id.tab_name_tv).setTextColor(getContext().getColorCompat(R.color.color_00a0e9))
            val imageView = it.findViewById<LottieAnimationView>(R.id.lottie_view)
            if (!imageView.isAnimating) {
                imageView.playAnimation()
            }
            setLottieColor(imageView, true)
        }
    }

    /** 设置Tab为未选中状态 */
    fun setTabUnselected(tab: TabLayout.Tab?) {
        tab?.customView?.let {
            it.findViewById<TextView>(R.id.tab_name_tv).setTextColor(Color.BLACK)
            val imageView = it.findViewById<LottieAnimationView>(R.id.lottie_view)
            if (imageView.isAnimating) {
                imageView.cancelAnimation()
                imageView.progress = 0f // 还原初始状态
            }
            setLottieColor(imageView, false)
        }
    }

    /** 设置Lottie图标颜色 */
    private fun setLottieColor(imageView: LottieAnimationView?, isSelected: Boolean) {
        imageView?.let {
            val csl = getContext().getColorStateListCompat(if (isSelected) R.color.color_00a0e9 else R.color.black)
            val callback = LottieValueCallback<ColorFilter>(SimpleColorFilter(csl.defaultColor))
            it.addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER, callback)
        }
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}