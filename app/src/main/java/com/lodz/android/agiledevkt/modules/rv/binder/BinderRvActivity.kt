package com.lodz.android.agiledevkt.modules.rv.binder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.config.Constant
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.binder.first.FirstBinder
import com.lodz.android.agiledevkt.modules.rv.binder.second.SecondBinder
import com.lodz.android.agiledevkt.modules.rv.binder.third.ThirdBinder
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.rv.binder.RvBinderAdapter

/**
 * RecyclerBinder测试类
 * Created by zhouL on 2018/12/10.
 */
class BinderRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BinderRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val FIRST_BINDER = 1
    private val SECOND_BINDER = 2
    private val THIRD_BINDER = 3


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


    /** 根布局 */
    private val mRootLayout by bindView<ViewGroup>(R.id.root_layout)

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mAdapter: RvBinderAdapter

    override fun getLayoutId(): Int = R.layout.activity_rv_binder

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
        addBinder()
    }

    /** 初始化RV */
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = RvBinderAdapter(getContext())
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    /** 添加binder */
    private fun addBinder() {
        mAdapter.addBinder(getFirstBinder())
        mAdapter.addBinder(getSecondBinder())
        mAdapter.addBinder(getThirdBinder())
    }

    /** 添加FirstBinder */
    private fun getFirstBinder(): FirstBinder {
        val binder = FirstBinder(getContext(), FIRST_BINDER)
        binder.setData(getNationList())
        binder.setListener { item ->
            toastShort(item.name)
        }
        return binder
    }

    private fun getSecondBinder(): SecondBinder {
        val binder = SecondBinder(getContext(), SECOND_BINDER)
        binder.setData(getNationList())
        binder.setListener { item -> toastShort(item.name) }
        return binder
    }

    private fun getThirdBinder(): ThirdBinder {
        val binder = ThirdBinder(getContext(), THIRD_BINDER)
        binder.setData(getNationList())
        binder.setListener { item ->
            SnackbarUtils.createShort(mRootLayout, item.name).setBackgroundColor(getColorCompat(R.color.color_a0191919)).show()
        }
        return binder
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    /** 获取数据 */
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