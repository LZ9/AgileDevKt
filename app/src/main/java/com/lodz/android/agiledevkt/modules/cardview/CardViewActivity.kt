package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCardViewBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayoutLazy

/**
 * CardView测试类
 * @author zhouL
 * @date 2019/6/4
 */
class CardViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCardViewBinding by bindingLayoutLazy(ActivityCardViewBinding::inflate)

    private lateinit var mAdapter: CardViewAdapter

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = CardViewAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mBinding.recyclerView.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)// 如果使用网格布局请设置此方法
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            SnackbarUtils.createShort(mBinding.recyclerView, item)
                    .setBackgroundColor(getColorCompat(R.color.color_00a0e9))
                    .setTextColor(Color.WHITE)
                    .show()
        }
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(arrayListOf("欧冠最佳进球新", "37岁姚笛近照曝光", "5G商用牌照将发布", "纳达尔横扫锦织圭新", "章莹颖失踪案开庭"
                , "优衣库联名遭疯抢", "文言文写钢铁侠传", "校长表白毕业生", "世界环境日新", "内马尔或将被起诉"))
        showStatusCompleted()
    }
}