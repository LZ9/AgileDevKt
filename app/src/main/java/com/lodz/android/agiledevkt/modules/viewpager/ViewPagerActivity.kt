package com.lodz.android.agiledevkt.modules.viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.ScrollTypeBean
import com.lodz.android.agiledevkt.databinding.ActivityViewPagerBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.collect.radio.Radioable
import com.lodz.android.pandora.widget.vp2.ScaleInTransformer

/**
 * ViewPager2测试类
 * @author zhouL
 * @date 2019/12/30
 */
class ViewPagerActivity : BaseActivity() {

    companion object {
        /** 颜色资源 */
        val COLOR_RES = arrayListOf(R.color.color_ea413c, R.color.color_303f9f, R.color.color_00796b, R.color.color_ff6307)
        /** 滑动类型 */
        val SCROLL_TYPE = arrayListOf(R.string.vp_horizontal, R.string.vp_vertical)
        /** 是否可滑动 */
        val SCROLL_ENABLE_TYPE = arrayListOf(R.string.vp_yes, R.string.vp_no)

        fun start(context: Context) {
            val intent = Intent(context, ViewPagerActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mAdapter: ViewPagerAdapter
    private var mList: MutableList<Pair<String, Int>>? = null

    private val mBinding: ActivityViewPagerBinding by bindingLayout(ActivityViewPagerBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRadioGroup()
        initViewPager()
    }

    private fun initRadioGroup() {
        mBinding.scrollTypeCrg.setDataList(getScrollTypeList())
        mBinding.scrollTypeCrg.setSelectedId(ViewPager2.ORIENTATION_HORIZONTAL.toString())
        mBinding.scrollEnableCrg.setDataList(getEnableTypeList())
        mBinding.scrollEnableCrg.setSelectedId(SCROLL_ENABLE_TYPE[0].toString())
    }

    private fun initViewPager() {
        mAdapter = ViewPagerAdapter(getContext())
        mBinding.viewPager.adapter = mAdapter
        mBinding.viewPager.orientation = mBinding.scrollTypeCrg.getSelectedId()[0].toInt()
        mBinding.viewPager.isUserInputEnabled = mBinding.scrollEnableCrg.getSelectedId()[0] == R.string.vp_yes.toString()
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(ScaleInTransformer())
        mBinding.viewPager.setPageTransformer(compositePageTransformer)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 滑动类型单选
        mBinding.scrollTypeCrg.setOnCheckedChangeListener { radioable, isSelected ->
            if (isSelected) {
                mBinding.viewPager.orientation = radioable.getIdTag().toInt()
            }
        }

        // 允许滑动单选
        mBinding.scrollEnableCrg.setOnCheckedChangeListener { radioable, isSelected ->
            if (isSelected) {
                mBinding.viewPager.isUserInputEnabled = radioable.getIdTag() == R.string.vp_yes.toString()
            }
        }

        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                PrintLog.d("testtag", "show pager ".append(mList?.get(position)?.first))
            }
        })

        // 上一页
        mBinding.preBtn.setOnClickListener {
            mBinding.viewPager.setCurrentItem(mBinding.viewPager.currentItem - 1)
        }

        // 下一页
        mBinding.nextBtn.setOnClickListener {
            mBinding.viewPager.setCurrentItem(mBinding.viewPager.currentItem + 1)
        }
    }

    override fun initData() {
        super.initData()
        mList = getTestData()
        mAdapter.setData(mList)
        showStatusCompleted()
    }

    private fun getScrollTypeList(): MutableList<Radioable> {
        val list = ArrayList<Radioable>()
        list.add(ScrollTypeBean(ViewPager2.ORIENTATION_HORIZONTAL.toString(), getString(SCROLL_TYPE[0])))
        list.add(ScrollTypeBean(ViewPager2.ORIENTATION_VERTICAL.toString(), getString(SCROLL_TYPE[1])))
        return list
    }

    private fun getEnableTypeList(): MutableList<Radioable> {
        val list = ArrayList<Radioable>()
        list.add(ScrollTypeBean(SCROLL_ENABLE_TYPE[0].toString(), getString(SCROLL_ENABLE_TYPE[0])))
        list.add(ScrollTypeBean(SCROLL_ENABLE_TYPE[1].toString(), getString(SCROLL_ENABLE_TYPE[1])))
        return list
    }

    private fun getTestData(): MutableList<Pair<String, Int>> {
        val list = ArrayList<Pair<String, Int>>()
        for (i in 0 until COLOR_RES.size) {
            val pair = Pair((i + 1).toString(), COLOR_RES[i])
            list.add(pair)
        }
        return list
    }
}