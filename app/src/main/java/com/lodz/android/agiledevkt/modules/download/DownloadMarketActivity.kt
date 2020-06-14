package com.lodz.android.agiledevkt.modules.download

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.AppInfoBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.pandora.base.activity.BaseActivity
import zlc.season.rxdownload4.manager.TaskManager
import zlc.season.rxdownload4.manager.delete
import zlc.season.rxdownload4.manager.start
import zlc.season.rxdownload4.manager.stop

/**
 * 下载测试
 * @author zhouL
 * @date 2020/6/14
 */
class DownloadMarketActivity :BaseActivity() {

    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mAdapter: DownloadMarketAdapter

    override fun getLayoutId(): Int = R.layout.activity_download_market

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = DownloadMarketAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnDownloadListener(object : DownloadMarketAdapter.OnDownloadListener{
            override fun onClickDownload(taskManager: TaskManager, bean: AppInfoBean) {
                try {
                    taskManager.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClickPause(taskManager: TaskManager, bean: AppInfoBean) {
                taskManager.stop()
            }

            override fun onClickDelete(taskManager: TaskManager, bean: AppInfoBean) {
                taskManager.delete()
            }
        })
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(DownloadConstant.getMarketApps())
        showStatusCompleted()
    }
}