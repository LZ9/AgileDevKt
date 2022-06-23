package com.lodz.android.agiledevkt.modules.rv.head

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityHeadRvBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.agiledevkt.modules.rv.popup.OrientationPopupWindow
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.layoutM
import com.lodz.android.pandora.widget.rv.anko.setup

/**
 * RV带头/底部测试
 * Created by zhouL on 2018/11/27.
 */
class HeadFooterRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HeadFooterRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityHeadRvBinding by bindingLayout(ActivityHeadRvBinding::inflate)

    /** 适配器 */
    private lateinit var mAdapter: HeadFooterAdapter

    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView
            .layoutM(getLayoutManager())
            .setup(HeadFooterAdapter(getContext()))
            .apply {
                setLayoutManagerType(mLayoutManagerType)
                setOrientation(mOrientation)
                setData(getItemList())
            }
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            val layoutManager = GridLayoutManager(getContext(), 3)
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

    override fun setListeners() {
        super.setListeners()
        // 头部选择按钮
        mBinding.headerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter.setHeaderData(if (isChecked) getString(R.string.rvhead_header_tips) else null)
            mAdapter.notifyDataSetChanged()
            mBinding.recyclerView.smoothScrollToPosition(0)
        }

        // 底部选择按钮
        mBinding.footerSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter.setFooterData(if (isChecked) getString(R.string.rvhead_footer_tips) else null)
            mAdapter.notifyDataSetChanged()
            mBinding.recyclerView.smoothScrollToPosition(mAdapter.itemCount)
        }

        // 方向按钮
        mBinding.orientationBtn.setOnClickListener { view ->
            showOrientationPopupWindow(view)
        }

        // 布局按钮
        mBinding.layoutManagerBtn.setOnClickListener { view ->
            showLayoutManagerPopupWindow(view)
        }

        // item点击
        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item)
        }

        // item长按
        mAdapter.setOnItemLongClickListener { viewHolder, item, position ->
            toastShort(item + "  " + getString(R.string.rvhead_item_long_click_tips))
        }

        // 头部点击
        mAdapter.setOnHeaderClickListener { viewHolder, data, position ->
            toastShort(data)
        }

        // 头部长按
        mAdapter.setOnHeaderLongClickListener { viewHolder, data, position ->
            toastShort(data + "  " + getString(R.string.rvhead_header_long_click_tips))
        }

        // 底部点击
        mAdapter.setOnFooterClickListener { viewHolder, data, position ->
            toastShort(data)
        }

        // 底部长按
        mAdapter.setOnFooterLongClickListener { viewHolder, data, position ->
            toastShort(data + "  " + getString(R.string.rvhead_footer_long_click_tips))
        }
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
        popupWindow.getPopup().showAsDropDown(view, -15, 20)

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
        popupWindow.getPopup().showAsDropDown(view, -45, 20)
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }

    private fun getItemList(): MutableList<String> {
        val list = ArrayList<String>()
        for (i in 0 until 20) {
            list.add("-.- " + (i + 1))
        }
        return list
    }

}