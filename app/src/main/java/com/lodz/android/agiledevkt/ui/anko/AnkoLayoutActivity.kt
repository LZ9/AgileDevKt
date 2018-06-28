package com.lodz.android.agiledevkt.ui.anko

import android.os.Bundle
import com.lodz.android.componentkt.base.activity.BaseActivity
import com.lodz.android.componentkt.base.activity.UseAnkoLayout
import org.jetbrains.anko.*

/**
 * AnkoLayout测试类
 * Created by zhouL on 2018/6/28.
 */
@UseAnkoLayout
class AnkoLayoutActivity : BaseActivity() {
    override fun getLayoutId() = 0

    override fun findViews(savedInstanceState: Bundle?) {
        verticalLayout {
            padding = dip(30)
            editText {
                hint = "Name"
                textSize = 24f
            }
            editText {
                hint = "Password"
                textSize = 24f
            }
            button("Login") {
                textSize = 26f
            }
        }
    }
}