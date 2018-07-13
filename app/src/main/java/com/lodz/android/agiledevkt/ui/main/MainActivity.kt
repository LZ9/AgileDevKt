package com.lodz.android.agiledevkt.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.lodz.android.agiledevkt.App
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.ui.anko.AnkoLayoutActivity
import com.lodz.android.agiledevkt.ui.bitmap.BitmapTestActivity
import com.lodz.android.agiledevkt.ui.file.FileTestActivity
import com.lodz.android.agiledevkt.ui.image.GlideActivity
import com.lodz.android.agiledevkt.ui.notification.NotificationActivity
import com.lodz.android.agiledevkt.ui.security.EncryptTestActivity
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat

class MainActivity : BaseActivity() {

    companion object {
        /** 标题名称 */
        const val EXTRA_TITLE_NAME = "extra_title_name"

        /** 通过上下文[context]启动Activity */
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 功能名称 */
    private val NAME_LIST = arrayListOf(
            "AnkoLayout测试类", "文件测试类", "加密测试类",
            "Bitmap图片测试类", "Glide测试", "通知测试类"
    )

    /** 功能的activity */
    private val CLASS_LIST = arrayListOf(
            AnkoLayoutActivity::class.java, FileTestActivity::class.java, EncryptTestActivity::class.java,
            BitmapTestActivity::class.java, GlideActivity::class.java, NotificationActivity::class.java
    )

    /** 列表 */
    @BindView(R.id.recycler_view)
    lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: MainAdapter

    override fun getLayoutId() = R.layout.activity_main

    override fun findViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
    }

    private fun initTitleBar(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(R.string.main_title)
        val refreshBtn = TextView(getContext())
        refreshBtn.text = getString(R.string.main_change_mood)
        refreshBtn.setPadding(dp2px(15).toInt(), 0, dp2px(15).toInt(), 0)
        refreshBtn.setTextColor(getColorCompat(R.color.white))
        refreshBtn.setOnClickListener {
            mAdapter.notifyDataSetChanged()
        }
        titleBarLayout.addExpandView(refreshBtn)
        titleBarLayout.needBackButton(false)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mAdapter = MainAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun onPressBack(): Boolean {
        App.get().exit()
        return true
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { holder, item, position ->
            val intent = Intent(getContext(), CLASS_LIST[position])
            intent.putExtra(EXTRA_TITLE_NAME, item)
            startActivity(intent)
        }
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(NAME_LIST)
        showStatusCompleted()
    }

}
