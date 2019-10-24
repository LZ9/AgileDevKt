package com.lodz.android.agiledevkt.modules.transition

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.TransitionBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * 共享元素动画
 * Created by zhouL on 2018/11/20.
 */
class TransitionActivity : BaseActivity() {

    companion object {
        /** 图片 */
        const val IMG = "img"
        /** 标题 */
        const val TITLE = "title"

        fun start(context: Context) {
            val intent = Intent(context, TransitionActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val IMGS = arrayOf(
            R.drawable.ic_progress_loading_cutom_1,
            R.drawable.ic_progress_loading_cutom_3,
            R.drawable.ic_progress_loading_cutom_5,
            R.drawable.ic_progress_loading_cutom_1,
            R.drawable.ic_progress_loading_cutom_3,
            R.drawable.ic_progress_loading_cutom_5,
            R.drawable.ic_progress_loading_cutom_1,
            R.drawable.ic_progress_loading_cutom_3,
            R.drawable.ic_progress_loading_cutom_5,
            R.drawable.ic_progress_loading_cutom_1,
            R.drawable.ic_progress_loading_cutom_3,
            R.drawable.ic_progress_loading_cutom_5)

    private val TITLES = arrayOf(
            "大风车吱呀吱哟哟地转",
            "这里的风景呀真好看",
            "天好看",
            "地好看",
            "还有一起快乐的小伙伴",
            "大风车转啊转悠悠",
            "快乐的伙伴手牵着手",
            "牵着你的手",
            "牵着我的手",
            "今天的小伙伴",
            "明天的好朋友",
            "好朋友")

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 适配器 */
    private lateinit var mAdapter: TransitionAdapter

    override fun getLayoutId(): Int = R.layout.activity_transition

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = TransitionAdapter(getContext())
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
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            if (viewHolder !is TransitionAdapter.DataViewHolder){
                toastShort(R.string.share_element_unable)
                return@setOnItemClickListener
            }
            val sharedElements = ArrayList<Pair<View, String>>()//创建共享元素列表
            sharedElements.add(Pair.create(viewHolder.img, TransitionActivity.IMG))
            sharedElements.add(Pair.create(viewHolder.titleTv, TransitionActivity.TITLE))
            ElementActivity.start(this, item.imgRes, item.title, sharedElements)
        }
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(getData())
        showStatusCompleted()
    }

    private fun getData(): ArrayList<TransitionBean> {
        val list = ArrayList<TransitionBean>()
        for (i in IMGS.indices) {
            val bean = TransitionBean()
            bean.imgRes = IMGS[i]
            bean.title = TITLES[i]
            list.add(bean)
        }
        return list
    }
}