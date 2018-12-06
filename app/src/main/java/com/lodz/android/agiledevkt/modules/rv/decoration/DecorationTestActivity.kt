package com.lodz.android.agiledevkt.modules.rv.decoration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.agiledevkt.modules.rv.popup.OrientationPopupWindow
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.widget.base.TitleBarLayout
import com.lodz.android.componentkt.widget.rv.decoration.GridItemDecoration
import com.lodz.android.componentkt.widget.rv.decoration.RoundItemDecoration
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.toastShort

/**
 * 装饰器测试类
 * Created by zhouL on 2018/12/5.
 */
class DecorationTestActivity : BaseActivity() {

    companion object {

        /** 底部分割线 */
        const val DECORATION_TYPE_ROUND_BOTTOM = 0
        /** 外围分割线 */
        const val DECORATION_TYPE_ROUND = 1
        /** 网格分割线 */
        const val DECORATION_TYPE_GRID = 2
        /** 分组标签 */
        const val DECORATION_TYPE_SECTION = 3
        /** 固定数据的分组标签 */
        const val DECORATION_TYPE_SECTION_FIX = 4
        /** 粘黏分组标签 */
        const val DECORATION_TYPE_STICKY_SECTION = 5
        /** 固定数据的粘黏分组标签 */
        const val DECORATION_TYPE_STICKY_SECTION_FIX = 6

        private const val EXTRA_DECORATION_TYPE = "extra_decoration_type"

        /** 启动，上下文[context]，装饰器类型[type] */
        fun start(context: Context, type: Int) {
            val intent = Intent(context, DecorationTestActivity::class.java)
            intent.putExtra(EXTRA_DECORATION_TYPE, type)
            context.startActivity(intent)
        }
    }

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    private lateinit var mAdapter: DecorationRvAdapter

    /** 装饰器类型 */
    private var mDecorationType = DECORATION_TYPE_ROUND
    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL

    override fun startCreate() {
        super.startCreate()
        mDecorationType = intent.getIntExtra(EXTRA_DECORATION_TYPE, DECORATION_TYPE_ROUND)
    }

    override fun getLayoutId(): Int = R.layout.activity_decoration_test

    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
    }

    /** 初始化RV */
    private fun initRecyclerView() {
        mAdapter = DecorationRvAdapter(getContext())
        mRecyclerView.layoutManager = getLayoutManager()
        mAdapter.onAttachedToRecyclerView(mRecyclerView)// 如果使用网格布局请设置此方法
        mAdapter.setLayoutManagerType(mLayoutManagerType)
        mAdapter.setOrientation(mOrientation)
        mRecyclerView.addItemDecoration(getItemDecoration(mDecorationType))
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    /** 初始化标题栏 */
    private fun initTitleBar(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(getTitleName(mDecorationType))
        titleBarLayout.needExpandView(true)
        titleBarLayout.addExpandView(getExpandView())
    }

    /** 根据装饰器类型[type]获取标题名称 */
    private fun getTitleName(type: Int): String = when (type) {
        DECORATION_TYPE_ROUND_BOTTOM -> getString(R.string.rvdecoration_round_bottom)
        DECORATION_TYPE_ROUND -> getString(R.string.rvdecoration_round)
        DECORATION_TYPE_GRID -> getString(R.string.rvdecoration_grid)
        DECORATION_TYPE_SECTION -> getString(R.string.rvdecoration_section)
        DECORATION_TYPE_SECTION_FIX -> getString(R.string.rvdecoration_section_fix)
        DECORATION_TYPE_STICKY_SECTION -> getString(R.string.rvdecoration_sticky_section)
        DECORATION_TYPE_STICKY_SECTION_FIX -> getString(R.string.rvdecoration_sticky_section_fix)
        else -> getString(R.string.rvdecoration_round)
    }

    /** 获取扩展view */
    private fun getExpandView(): View {
        val linearLayout = LinearLayout(getContext())
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.HORIZONTAL

        val orientationTv = getTextView(R.string.rvpopup_orientation);//方向
        orientationTv.setOnClickListener { view ->
            if (mDecorationType == DECORATION_TYPE_ROUND_BOTTOM) {
                toastShort(R.string.rvdecoration_unenabled_orientation)
                return@setOnClickListener
            }
            showOrientationPopupWindow(view)
        }
        linearLayout.addView(orientationTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val layoutManagerTv = getTextView(R.string.rvpopup_layout_manager);//布局
        layoutManagerTv.setOnClickListener { view ->
            if (mDecorationType == DECORATION_TYPE_ROUND_BOTTOM || mDecorationType == DECORATION_TYPE_ROUND) {
                toastShort(R.string.rvdecoration_unenabled_layout_manager)
                return@setOnClickListener
            }
            showLayoutManagerPopupWindow(view)
        }
        linearLayout.addView(layoutManagerTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return linearLayout
    }

    /** 获取TextView，标题名称资源[resId] */
    private fun getTextView(@StringRes resId: Int): TextView {
        val tv = TextView(getContext())
        tv.setText(resId)
        tv.setPadding(dp2px(6).toInt(), 0, dp2px(6).toInt(), 0)
        tv.setTextColor(getColorCompat(R.color.white))
        return tv
    }

    /** 横纵方向PopupWindow */
    private fun showOrientationPopupWindow(view: View) {
        val popupWindow = OrientationPopupWindow(getContext())
        popupWindow.create()
        popupWindow.setOrientationType(mOrientation)
        popupWindow.setOnClickListener { popup, orientation ->
            mOrientation = orientation
            mAdapter.setOrientation(mOrientation)
            mRecyclerView.layoutManager = getLayoutManager()
            mAdapter.onAttachedToRecyclerView(mRecyclerView)
            mAdapter.notifyDataSetChanged()
            popup.dismiss()
        }
        popupWindow.getPopup().showAsDropDown(view, -50, 20)

    }

    /** 显示布局的PopupWindow */
    private fun showLayoutManagerPopupWindow(view: View) {
        val popupWindow = LayoutManagerPopupWindow(getContext())
        popupWindow.create()
        popupWindow.setLayoutManagerType(mLayoutManagerType)
        popupWindow.setOnClickListener { popup, type ->
            mLayoutManagerType = type
            mAdapter.setLayoutManagerType(mLayoutManagerType)
            mRecyclerView.layoutManager = getLayoutManager()
            mAdapter.onAttachedToRecyclerView(mRecyclerView)
            mAdapter.notifyDataSetChanged()
            popup.dismiss()
        }
        popupWindow.getPopup().showAsDropDown(view, 0, 20)
    }

    /** 根据类型[type]获取装饰器 */
    private fun getItemDecoration(type: Int): RecyclerView.ItemDecoration {
        if (type == DECORATION_TYPE_ROUND_BOTTOM) {
            return RoundItemDecoration.createBottomDivider(getContext(), 1, 10, R.color.color_9a9a9a, R.color.white)
        }
        if (type == DECORATION_TYPE_GRID) {
            return GridItemDecoration.create(getContext()).setDividerRes(R.color.color_9a9a9a).setDividerSpace(2)
        }
        return RoundItemDecoration.create(getContext())
                .setTopDividerRes(3, 0, R.color.color_9a9a9a, R.color.color_ea413c)
                .setLeftDividerRes(3, 0, R.color.color_9a9a9a, R.color.color_ea413c)
                .setRightDividerRes(3, 0, R.color.color_9a9a9a, R.color.color_ea413c)
                .setBottomDividerRes(3, 0, R.color.color_9a9a9a, R.color.color_ea413c)
    }

    /** 获取布局 */
    private fun getLayoutManager(): RecyclerView.LayoutManager {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            val layoutManager = GridLayoutManager(getContext(), 6)
            layoutManager.orientation = mOrientation
            return layoutManager
        }
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_STAGGERED) {
            return StaggeredGridLayoutManager(2, mOrientation)
        }

        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = mOrientation
        return layoutManager
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(getList())
        mAdapter.notifyDataSetChanged()
        showStatusCompleted()
    }

    private fun getList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..100) {
            list.add(i.toString())
        }
        return list
    }
}