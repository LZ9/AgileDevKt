package com.lodz.android.agiledevkt.modules.rv.binder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.NationBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.binder.first.FirstBinder
import com.lodz.android.agiledevkt.modules.rv.binder.second.SecondBinder
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.widget.rv.binder.RvBinderAdapter
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.toastShort

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
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528284956583&di=673c40bfcb1603c4547f0e684619b636&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F5bafa40f4bfbfbed0528385b72f0f736afc31fa4.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528879767&di=ade0e932e2a79fc67f6eaf0a10a85f0a&imgtype=jpg&er=1&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fe1fe9925bc315c601e092f9f8db1cb1349547725.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528879811&di=3705bdd84e8ec5e5e874bb42369a9170&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd4628535e5dde711fd3a64f6adefce1b9c1661c1.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528285142563&di=5c338bb523a9aa41dcae9d003c03bb67&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fb7003af33a87e950909a752316385343faf2b4f4.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1513270247,2810085725&fm=27&gp=0.jpg",
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1479658781,2612823267&fm=200&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=373478989,3635638859&fm=27&gp=0.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528285365045&di=92f5f3a4dbf36274e381b14a2b8c6ed5&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F01%2F29%2F39%2F45bOOOPIC74.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528285464640&di=bec9062ec87cfb18b4bed7fc66e9e068&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D5f6687252c9759ee5e5d6888da922963%2F3c6d55fbb2fb43163abe99522aa4462309f7d371.jpg"
    )

    /** 名称 */
    private val NATION_NAMES = arrayOf(
            "中国", "美国", "俄罗斯", "日本", "韩国", "澳大利亚", "乌克兰", "朝鲜", "巴西"
    )

    /** 缩写 */
    private val NATION_CODES = arrayOf(
            "CHN", "USA", "RUS", "JPN", "KOR", "AUS", "UKR", "PRK", "BRA"
    )

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mAdapter: RvBinderAdapter

    override fun getLayoutId(): Int = R.layout.activity_rv_binder

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
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

    }

    /** 添加FirstBinder */
    private fun getFirstBinder(): FirstBinder {
        val binder = FirstBinder(getContext(), FIRST_BINDER)
        binder.setData(getNationList())
        binder.setListener(object :FirstBinder.Listener{
            override fun onClick(item: NationBean) {
                toastShort(item.name)
            }
        })
        return binder
    }

    private fun getSecondBinder(): SecondBinder {
        val binder = SecondBinder(getContext(), SECOND_BINDER)
        binder.setData(getNationList())
        binder.setListener(object :SecondBinder.Listener{
            override fun onClick(item: NationBean) {
                toastShort(item.name)
            }
        })
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
        for (i in 0 until NATION_IMGS.size) {
            val bean = NationBean()
            bean.imgUrl = NATION_IMGS.get(i)
            bean.name = NATION_NAMES.get(i)
            bean.code = NATION_CODES.get(i)
            list.add(bean)
        }
        return list
    }
}