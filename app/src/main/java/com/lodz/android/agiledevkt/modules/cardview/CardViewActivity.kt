package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity

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


    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)

    private lateinit var mAdapter: CardViewAdapter

    override fun getLayoutId(): Int = R.layout.activity_card_view

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = CardViewAdapter(getContext())
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView.layoutManager = layoutManager
        mAdapter.onAttachedToRecyclerView(mRecyclerView)// 如果使用网格布局请设置此方法
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
            SnackbarUtils.createShort(mRecyclerView, item)
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