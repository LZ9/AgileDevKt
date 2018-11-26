package com.lodz.android.agiledevkt.modules.rv.loadmore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.anim.AnimRvAdapter
import com.lodz.android.componentkt.base.activity.BaseRefreshActivity
import com.lodz.android.corekt.anko.bindView

/**
 * RV刷新/加载更多测试
 * Created by zhouL on 2018/11/23.
 */
class RefreshLoadMoreActivity : BaseRefreshActivity() {

    companion object {
        fun start(context: Context){
            val intent = Intent(context, RefreshLoadMoreActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 每页数量 */
    private val PAGE_SIZE = 20
    /** 最大数量 */
    private val MAX_SIZE = 120

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 加载失败开关 */
    private val mLoadFailSwitch by bindView<Switch>(R.id.load_fail_switch)
    /** 布局按钮 */
    private val mLayoutManagerBtn by bindView<TextView>(R.id.layout_manager_btn)

    /** 适配器 */
    private lateinit var mAdapter: AnimRvAdapter


    override fun getLayoutId(): Int = R.layout.activity_refresh_load_more

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun onDataRefresh() {

    }

}