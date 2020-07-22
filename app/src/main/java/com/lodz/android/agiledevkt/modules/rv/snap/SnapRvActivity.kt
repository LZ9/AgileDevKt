package com.lodz.android.agiledevkt.modules.rv.snap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.rv.snap.TabPagerSnapHelper
import com.lodz.android.pandora.widget.rv.snap.ViewPagerSnapHelper

/**
 * RvSnap测试类
 * Created by zhouL on 2018/12/3.
 */
class SnapRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SnapRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 国旗 */
    private val NATION_IMGS = arrayOf(
        Constant.CHN_FLAG_URL,
        Constant.USA_FLAG_URL,
        Constant.RUS_FLAG_URL,
        Constant.JPN_FLAG_URL,
        Constant.KOR_FLAG_URL,
        Constant.AUS_FLAG_URL,
        Constant.UKR_FLAG_URL,
        Constant.PRK_FLAG_URL,
        Constant.BRA_FLAG_URL
    )

    /** 名称 */
    private val NATION_NAMES = arrayOf(
            "中国", "美国", "俄罗斯", "日本", "韩国", "澳大利亚", "乌克兰", "朝鲜", "巴西"
    )

    /** 缩写 */
    private val NATION_CODES = arrayOf(
            "CHN", "USA", "RUS", "JPN", "KOR", "AUS", "UKR", "PRK", "BRA"
    )
    private val mTabLayout by bindView<TabLayout>(R.id.tab_layout)
    private val mTabRecyclerView by bindView<RecyclerView>(R.id.tab_rv)
    private val mPageTv by bindView<TextView>(R.id.page_tv)
    private val mPagerRecyclerView by bindView<RecyclerView>(R.id.pager_rv)

    override fun getLayoutId(): Int = R.layout.activity_snap

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initTab()
        initPager()
    }

    private fun initTab() {
        NATION_NAMES.forEachIndexed { index, name ->
            mTabLayout.addTab(mTabLayout.newTab().setText(name), index == 0)
        }
        initTabRecyclerView()
    }

    private fun initTabRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        val adapter = SnapAdapter(getContext())
        mTabRecyclerView.layoutManager = layoutManager
        mTabRecyclerView.setHasFixedSize(true)
        mTabRecyclerView.adapter = adapter
        val snapHelper = TabPagerSnapHelper(0)
        snapHelper.attachToRecyclerView(mTabRecyclerView)
        snapHelper.setupWithTabLayout(mTabLayout)

        adapter.setData(getNationList())
        adapter.notifyDataSetChanged()
    }

    private fun initPager() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.HORIZONTAL
        val adapter = SnapAdapter(getContext())
        mPagerRecyclerView.layoutManager = layoutManager
        mPagerRecyclerView.setHasFixedSize(true)
        mPagerRecyclerView.adapter = adapter
        val snapHelper = ViewPagerSnapHelper(0)
        snapHelper.attachToRecyclerView(mPagerRecyclerView)
        snapHelper.setOnPageChangeListener { position ->
            mPageTv.text = getString(R.string.rvsnap_page, (position + 1).toString())
        }
        adapter.setData(getNationList())
        adapter.notifyDataSetChanged()
        mPageTv.text = getString(R.string.rvsnap_page, "1")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun getNationList(): ArrayList<NationBean> {
        val list = ArrayList<NationBean>()
        for (i in NATION_IMGS.indices) {
            val bean = NationBean()
            bean.imgUrl = NATION_IMGS[i]
            bean.name = NATION_NAMES[i]
            bean.code = NATION_CODES[i]
            list.add(bean)
        }
        return list
    }

}