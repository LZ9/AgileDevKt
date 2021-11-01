package com.lodz.android.agiledevkt.modules.rv.decoration

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDecorationTestBinding
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.agiledevkt.modules.rv.popup.OrientationPopupWindow
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.anko.intentExtrasInt
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.rv.decoration.*

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

    /** 俱乐部列表索引 */
    private val CLUB_INDEX_TITLE = arrayListOf("曼联", "阿森纳", "切尔西", "利物浦")
    private val PLAYER_FIX_SECTIONS = listOf(
            listOf("贝克汉姆", "吉格斯", "斯科尔斯", "鲁尼", "费迪南德", "范德萨", "卡里克", "罗伊基恩", "埃弗拉"),
            listOf("亨利"),
            listOf("兰帕德", "切赫", "特里", "乔科尔", "德罗巴", "巴拉克", "舍甫琴科", "罗本"),
            listOf("欧文", "杰拉德", "卡拉格", "福勒", "雷纳", "阿隆索", "库伊特", "里瑟")
    )

    private val mBinding: ActivityDecorationTestBinding by bindingLayout(ActivityDecorationTestBinding::inflate)

    private lateinit var mAdapter: DecorationRvAdapter

    /** 装饰器类型 */
    private val mDecorationType by intentExtrasInt(EXTRA_DECORATION_TYPE, DECORATION_TYPE_ROUND)
    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL

    /** 列表数据 */
    private lateinit var mList: ArrayList<String>

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
    }

    /** 初始化RV */
    private fun initRecyclerView() {
        mAdapter = DecorationRvAdapter(getContext())
        mBinding.recyclerView.layoutManager = getLayoutManager()
        mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)// 如果使用网格布局请设置此方法
        mAdapter.setLayoutManagerType(mLayoutManagerType)
        mAdapter.setOrientation(mOrientation)
        mBinding.recyclerView.addItemDecoration(getItemDecoration(mDecorationType))
        mBinding.recyclerView.setHasFixedSize(true)
        mBinding.recyclerView.adapter = mAdapter
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

        val orientationTv = getTextView(R.string.rvpopup_orientation)//方向
        orientationTv.setOnClickListener { view ->
            if (mDecorationType == DECORATION_TYPE_ROUND_BOTTOM || mDecorationType == DECORATION_TYPE_SECTION
                    || mDecorationType == DECORATION_TYPE_SECTION_FIX || mDecorationType == DECORATION_TYPE_STICKY_SECTION
                    || mDecorationType == DECORATION_TYPE_STICKY_SECTION_FIX) {
                toastShort(R.string.rvdecoration_unenabled_orientation)
                return@setOnClickListener
            }
            showOrientationPopupWindow(view)
        }
        linearLayout.addView(orientationTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val layoutManagerTv = getTextView(R.string.rvpopup_layout_manager)//布局
        layoutManagerTv.setOnClickListener { view ->
            if (mDecorationType == DECORATION_TYPE_ROUND_BOTTOM || mDecorationType == DECORATION_TYPE_ROUND
                    || mDecorationType == DECORATION_TYPE_SECTION || mDecorationType == DECORATION_TYPE_SECTION_FIX
                    || mDecorationType == DECORATION_TYPE_STICKY_SECTION || mDecorationType == DECORATION_TYPE_STICKY_SECTION_FIX) {
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
        tv.setPadding(dp2px(6), 0, dp2px(6), 0)
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
            mBinding.recyclerView.layoutManager = getLayoutManager()
            mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)
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
            mBinding.recyclerView.layoutManager = getLayoutManager()
            mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)
            mAdapter.notifyDataSetChanged()
            popup.dismiss()
        }
        popupWindow.getPopup().showAsDropDown(view, 0, 20)
    }

    /** 根据类型[type]获取装饰器 */
    private fun getItemDecoration(type: Int): RecyclerView.ItemDecoration {
        if (type == DECORATION_TYPE_ROUND_BOTTOM) {//只设置底部下划线
            return RoundItemDecoration.createBottomDivider(getContext(), 1, 10, R.color.color_9a9a9a, R.color.white)
        }
        if (type == DECORATION_TYPE_GRID) {//设置网格线
            return GridItemDecoration.create(getContext()).setDividerRes(R.color.color_9a9a9a).setDividerSpace(2)
        }
        if (type == DECORATION_TYPE_SECTION) {// 根据数据自动生成分组标签
            return SectionItemDecoration.create<String>(getContext())
                .setOnSectionCallback { position ->
                    mList[position]
                }
                .setSectionBgColorRes(R.color.color_ea6662)
                .setSectionTextColorRes(R.color.color_ffa630)
                .setSectionTextPaddingLeftDp(8)
                .setSectionTextTypeface(Typeface.DEFAULT_BOLD)
                .setSectionHeight(30)
                .setSectionTextSize(16f)
        }
        if (type == DECORATION_TYPE_SECTION_FIX) {//按传入的数据配置分组标签
            return SectionFixItemDecoration.create(getContext(), CLUB_INDEX_TITLE, PLAYER_FIX_SECTIONS)
                    .setSectionBgColorRes(R.color.color_1a1a1a)
                    .setSectionTextColorRes(R.color.color_f0f0f0)
                    .setSectionTextPaddingLeftDp(8)
                    .setSectionTextTypeface(Typeface.DEFAULT_BOLD)
                    .setSectionHeight(36)
                    .setSectionTextSize(16f)

        }
        if (type == DECORATION_TYPE_STICKY_SECTION) {// 根据数据自动生成分组标签
            return StickyItemDecoration.create<String>(getContext())
                .setOnSectionCallback { position ->
                    mList[position]
                }
                .setSectionBgColorRes(R.color.color_ea6662)
                .setSectionTextColorRes(R.color.color_ffa630)
                .setSectionTextPaddingLeftDp(8)
                .setSectionTextTypeface(Typeface.DEFAULT_BOLD)
                .setSectionHeight(30)
                .setSectionTextSize(16f)
        }
        if (type == DECORATION_TYPE_STICKY_SECTION_FIX) {//按传入的数据配置分组标签
            return StickyFixItemDecoration.create(getContext(), CLUB_INDEX_TITLE, PLAYER_FIX_SECTIONS)
                    .setSectionBgColorRes(R.color.color_1a1a1a)
                    .setSectionTextColorRes(R.color.color_f0f0f0)
                    .setSectionTextPaddingLeftDp(8)
                    .setSectionTextTypeface(Typeface.DEFAULT_BOLD)
                    .setSectionHeight(36)
                    .setSectionTextSize(16f)
        }
        // 订制上下左右边线
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
            return StaggeredGridLayoutManager(3, mOrientation)
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
        configByType(mDecorationType)
        mAdapter.setData(mList)
        mAdapter.notifyDataSetChanged()
        showStatusCompleted()
    }

    /** 根据类型[type]配置界面 */
    private fun configByType(type: Int) {
        mList = getList()
        if (type == DECORATION_TYPE_GRID) {
            mBinding.recyclerView.setPadding(dp2px(8))
        }
        if (type == DECORATION_TYPE_SECTION_FIX || type == DECORATION_TYPE_STICKY_SECTION_FIX) {
            val list = ArrayList<String>()
            for (source in PLAYER_FIX_SECTIONS) {
                list.addAll(source)
            }
            mList = list
        }
    }

    private fun getList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..100) {
            list.add(i.toString())
        }
        return list
    }
}