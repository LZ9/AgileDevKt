package com.lodz.android.agiledevkt.modules.viewpager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.ScrollTypeBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.radio.CltRadioGroup
import com.lodz.android.pandora.widget.collect.radio.Radioable

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

    /** 滑动类型单选 */
    private val mScrollRadioGroup by bindView<CltRadioGroup>(R.id.scroll_type_crg)
    /** 允许滑动单选 */
    private val mEnableRadioGroup by bindView<CltRadioGroup>(R.id.scroll_enable_crg)
    /** 上一页 */
    private val mPreBtn by bindView<MaterialButton>(R.id.pre_btn)
    /** 下一页 */
    private val mNextBtn by bindView<MaterialButton>(R.id.next_btn)

    private val mViewPager by bindView<ViewPager2>(R.id.view_pager)
    private lateinit var mAdapter: ViewPagerAdapter
    private var mList: MutableList<Pair<String, Int>>? = null

    override fun getLayoutId(): Int = R.layout.activity_view_pager

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRadioGroup()
        initViewPager()
    }

    private fun initRadioGroup() {
        mScrollRadioGroup.setDataList(getScrollTypeList())
        mScrollRadioGroup.setSelectedId(ViewPager2.ORIENTATION_HORIZONTAL.toString())
        mEnableRadioGroup.setDataList(getEnableTypeList())
        mEnableRadioGroup.setSelectedId(SCROLL_ENABLE_TYPE[0].toString())
    }

    private fun initViewPager() {
        mAdapter = ViewPagerAdapter(getContext())
        mViewPager.adapter = mAdapter
        mViewPager.orientation = mScrollRadioGroup.getSelectedId()[0].toInt()
        mViewPager.isUserInputEnabled = mEnableRadioGroup.getSelectedId()[0] == R.string.vp_yes.toString()
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mScrollRadioGroup.setOnCheckedChangeListener { radioable, isSelected ->
            if (isSelected) {
                mViewPager.orientation = radioable.getIdTag().toInt()
            }
        }

        mEnableRadioGroup.setOnCheckedChangeListener { radioable, isSelected ->
            if (isSelected) {
                mViewPager.isUserInputEnabled = radioable.getIdTag() == R.string.vp_yes.toString()
            }
        }

        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                PrintLog.d("testtag", "show pager ".append(mList?.get(position)?.first))
            }
        })

        mPreBtn.setOnClickListener {
            mViewPager.setCurrentItem(mViewPager.currentItem - 1)
        }

        mNextBtn.setOnClickListener {
            mViewPager.setCurrentItem(mViewPager.currentItem + 1)
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