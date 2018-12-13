package com.lodz.android.agiledevkt.modules.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity

/**
 * Dialog测试
 * Created by zhouL on 2018/11/1.
 */
class DialogActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DialogActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_dialog

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        findViewById<Button>(R.id.normal_btn).setOnClickListener {
            NormalDialog(getContext()).show()
        }

        findViewById<Button>(R.id.center_btn).setOnClickListener {
            CenterDialog(getContext()).show()
        }

        findViewById<Button>(R.id.left_btn).setOnClickListener {
            LeftDialog(getContext()).show()
        }

        findViewById<Button>(R.id.right_btn).setOnClickListener {
            RightDialog(getContext()).show()
        }

        findViewById<Button>(R.id.bottom_btn).setOnClickListener {
            BottomDialog(getContext()).show()
        }

        findViewById<Button>(R.id.top_btn).setOnClickListener {
            TopDialog(getContext()).show()
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}