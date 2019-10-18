package com.lodz.android.agiledevkt.modules.rv.anim

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.dp2px
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.base.TitleBarLayout
import com.lodz.android.pandora.widget.rv.recycler.BaseRecyclerViewAdapter
import org.jetbrains.anko.textColor

/**
 * RV动画测试
 * Created by zhouL on 2018/11/23.
 */
class AnimRvActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AnimRvActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 列表 */
    private val mRecyclerView by bindView<RecyclerView>(R.id.recycler_view)
    /** 适配器 */
    private lateinit var mAdapter: AnimRvAdapter

    /** 当前动画类型 */
    private var mAnimType = BaseRecyclerViewAdapter.SCALE_IN

    override fun getLayoutId(): Int = R.layout.activity_anim_rv


    override fun findViews(savedInstanceState: Bundle?) {
        initTitleBar(getTitleBarLayout())
        initRecyclerView()
    }

    /** 初始化标题栏 */
    private fun initTitleBar(titleBarLayout: TitleBarLayout) {
        titleBarLayout.setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        titleBarLayout.needExpandView(true)
        titleBarLayout.addExpandView(getExpandView())
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(getContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        mAdapter = AnimRvAdapter(getContext())
        mAdapter.setOpenItemAnim(true)//开启动画
        mAdapter.setItemAnimStartPosition(7)//设置动画起始位置
        mAdapter.setAnimationType(mAnimType)//设置动画类型
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        mAdapter.setData(createList())
        mAdapter.notifyDataSetChanged()
        showStatusCompleted()
    }

    private fun createList(): ArrayList<String> {
        val list = ArrayList<String>()
        for (i in 1..120) {
            list.add(DateUtils.getCurrentFormatString(DateUtils.TYPE_10))
        }
        return list
    }

    /** 获取扩展view */
    private fun getExpandView(): View {
        val linearLayout = LinearLayout(getContext())
        linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val animTv = getTextView(R.string.rvanim_anim)//动画
        animTv.setOnClickListener { view ->
            showAnimPopupWindow(view)
        }
        linearLayout.addView(animTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val resetTv = getTextView(R.string.rvanim_reset)//重置
        resetTv.setOnClickListener {
            mRecyclerView.scrollToPosition(0)//滚动到顶部
            mAdapter.resetItemAnimPosition()//重置效果
        }
        linearLayout.addView(resetTv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return linearLayout
    }

    /** 获取TextView */
    private fun getTextView(@StringRes resId: Int): TextView {
        val tv = TextView(getContext())
        tv.setText(resId)
        tv.setPadding(dp2px(6), 0, dp2px(6), 0)
        tv.textColor = Color.WHITE
        return tv
    }

    /** 显示动画的PopupWindow */
    private fun showAnimPopupWindow(view: View) {
        val popupWindow = AnimPopupWindow(getContext())
        popupWindow.create()
        popupWindow.setAnimType(mAnimType)
        popupWindow.getPopup().showAsDropDown(view, -50, 20)
        popupWindow.setOnClickListener { popup, type ->
            mAnimType = type
            if (type == AnimPopupWindow.TYPE_CUSTOM) {
                mAdapter.setBaseAnimation(ScaleAlphaInAnimation())//设置自定义的淡入缩放效果
                popup.dismiss()
                return@setOnClickListener
            }
            mAdapter.setBaseAnimation(null)// 取消自定义动画效果
            mAdapter.setAnimationType(mAnimType)
            popup.dismiss()
        }
    }

}