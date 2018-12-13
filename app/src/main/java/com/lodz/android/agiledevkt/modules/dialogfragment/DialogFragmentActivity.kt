package com.lodz.android.agiledevkt.modules.dialogfragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.componentkt.base.activity.BaseActivity

/**
 * DialogFragment测试类
 * Created by zhouL on 2018/12/13.
 */
class DialogFragmentActivity :BaseActivity(){

    companion object {
        fun start(context: Context){
            val intent = Intent(context, DialogFragmentActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_dialog_fragment

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        findViewById<Button>(R.id.normal_df_btn).setOnClickListener {
            NormalDialogFragment().show(supportFragmentManager, "NormalDialogFragment")
        }

        findViewById<Button>(R.id.center_df_btn).setOnClickListener {
            CenterDialogFragment().show(supportFragmentManager, "CenterDialogFragment")
        }

        findViewById<Button>(R.id.left_df_btn).setOnClickListener {
            LeftDialogFragment().show(supportFragmentManager, "LeftDialogFragment")
        }

        findViewById<Button>(R.id.right_df_btn).setOnClickListener {
            RightDialogFragment().show(supportFragmentManager, "RightDialogFragment")
        }

        findViewById<Button>(R.id.bottom_df_btn).setOnClickListener {
            BottomDialogFragment().show(supportFragmentManager, "BottomDialogFragment")
        }

        findViewById<Button>(R.id.top_df_btn).setOnClickListener {
            TopDialogFragment().show(supportFragmentManager, "TopDialogFragment")
        }
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}