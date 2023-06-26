package com.lodz.android.agiledevkt.modules.transition

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.TransitionBean
import com.lodz.android.agiledevkt.databinding.ActivityTransitionBinding
import com.lodz.android.agiledevkt.databinding.RvItemTransitionBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setupVB

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

    private val mBinding: ActivityTransitionBinding by bindingLayout(ActivityTransitionBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.recyclerView
            .linear()
            .setupVB<TransitionBean, RvItemTransitionBinding>(RvItemTransitionBinding::inflate) { context, vb, holder, position ->
                val bean = getItem(position) ?: return@setupVB
                vb.img.setImageResource(bean.imgRes)
                vb.titleTv.text = bean.title
            }.apply {
                setOnItemClickListener { viewHolder, item, position ->
                    viewHolder.getVB<RvItemTransitionBinding>().apply {
                        val sharedElements = ArrayList<Pair<View, String>>()//创建共享元素列表
                        sharedElements.add(Pair.create(img, TransitionActivity.IMG))
                        sharedElements.add(Pair.create(titleTv, TransitionActivity.TITLE))
                        ElementActivity.start(this@TransitionActivity, item.imgRes, item.title, sharedElements)
                    }
                }
            }.setData(getData())
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
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