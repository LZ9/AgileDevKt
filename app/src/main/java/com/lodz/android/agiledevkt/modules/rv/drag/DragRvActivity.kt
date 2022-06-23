package com.lodz.android.agiledevkt.modules.rv.drag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDragBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.agiledevkt.modules.rv.popup.LayoutManagerPopupWindow
import com.lodz.android.agiledevkt.modules.rv.popup.OrientationPopupWindow
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.log.PrintLog
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.layoutM
import com.lodz.android.pandora.widget.rv.anko.setup
import com.lodz.android.pandora.widget.rv.drag.RecyclerViewDragHelper

/**
 * RV拖拽测试
 * Created by zhouL on 2018/11/30.
 */
class DragRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DragRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityDragBinding by bindingLayout(ActivityDragBinding::inflate)

    /** 适配器 */
    private lateinit var mAdapter: DragRvAdapter
    /** 拖拽帮助类 */
    private lateinit var mRecyclerViewDragHelper: RecyclerViewDragHelper<String>
    /** 当前布局 */
    @LayoutManagerPopupWindow.LayoutManagerType
    private var mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
    /** 布局方向 */
    private var mOrientation = RecyclerView.VERTICAL
    /** 拖拽回调 */
    private val mCallback = DragSpeedCallback<String>()

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = mBinding.recyclerView
            .layoutM(getLayoutManager())
            .setup(DragRvAdapter(getContext()))
            .apply {
                setLayoutManagerType(mLayoutManagerType)
                setOrientation(mOrientation)
            }
        mRecyclerViewDragHelper = RecyclerViewDragHelper(getContext())
        mRecyclerViewDragHelper.setUseDrag(true)// 设置是否允许拖拽
            .setLongPressDragEnabled(true)// 是否启用长按拖拽效果
            .setUseLeftToRightSwipe(true)// 设置允许从左往右滑动
            .setUseRightToLeftSwipe(true)// 设置允许从右往左滑动
            .setSwipeEnabled(false)// 设置是否允许滑动
            .setVibrateEnabled(true)// 启用震动效果
        mRecyclerViewDragHelper.build(mBinding.recyclerView, mAdapter, mCallback)
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        if (mLayoutManagerType == LayoutManagerPopupWindow.TYPE_GRID) {
            val layoutManager = GridLayoutManager(getContext(), 3)
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

    override fun setListeners() {
        super.setListeners()

        // 侧滑模式
        mBinding.swipeModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mOrientation = RecyclerView.VERTICAL
                mLayoutManagerType = LayoutManagerPopupWindow.TYPE_LINEAR
                mAdapter.setOrientation(mOrientation)
                mAdapter.setLayoutManagerType(mLayoutManagerType)
                mBinding.recyclerView.layoutManager = getLayoutManager()
                mAdapter.onAttachedToRecyclerView(mBinding.recyclerView)
                mAdapter.notifyDataSetChanged()
            }
            mRecyclerViewDragHelper.setUseDrag(!isChecked)
            mRecyclerViewDragHelper.setVibrateEnabled(!isChecked)
            mRecyclerViewDragHelper.setSwipeEnabled(isChecked)
        }

        // 触摸拖拽
        mBinding.touchDragSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mRecyclerViewDragHelper.setLongPressDragEnabled(!isChecked)
            mAdapter.setItemTouchHelper(if (isChecked) mRecyclerViewDragHelper.getItemTouchHelper() else null)
            mBinding.recyclerView.scrollToPosition(0)
            mAdapter.notifyDataSetChanged()
        }

        // 匀速拖拽
        mBinding.constantSpeedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            mCallback.isLimit = isChecked
        }

        // 方向
        mBinding.orientationBtn.setOnClickListener { view ->
            if (mBinding.swipeModeSwitch.isChecked){
                toastShort(R.string.rvdrag_orientation_unenable)
                return@setOnClickListener
            }
            showOrientationPopupWindow(view)
        }

        // 布局
        mBinding.layoutManagerBtn.setOnClickListener { view ->
            if (mBinding.swipeModeSwitch.isChecked){
                toastShort(R.string.rvdrag_layout_manager_unenable)
                return@setOnClickListener
            }
            showLayoutManagerPopupWindow(view)
        }

        mAdapter.setOnItemClickListener { viewHolder, item, position ->
            toastShort(item)
        }

        mRecyclerViewDragHelper.setListener { list ->
            PrintLog.d("testtag", list.toString())
        }
    }

    override fun initData() {
        super.initData()
        val list = getList()
        mAdapter.setData(list)
        mRecyclerViewDragHelper.setList(list)
        mAdapter.notifyDataSetChanged()
        showStatusCompleted()
    }

    private fun getList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..120) {
            list.add(i.toString())
        }
        return list
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
}