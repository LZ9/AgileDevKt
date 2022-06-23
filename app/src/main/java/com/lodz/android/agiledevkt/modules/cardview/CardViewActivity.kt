package com.lodz.android.agiledevkt.modules.cardview

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityCardViewBinding
import com.lodz.android.agiledevkt.databinding.RvItemCardViewBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.getColorCompat
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.SnackbarUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.rv.anko.linear
import com.lodz.android.pandora.widget.rv.anko.setupVB

/**
 * CardView测试类
 * @author zhouL
 * @date 2019/6/4
 */
class CardViewActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CardViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCardViewBinding by bindingLayout(ActivityCardViewBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
        initRecyclerView()
    }

    private fun initRecyclerView() {
         mBinding.recyclerView
             .linear()
             .setupVB<String, RvItemCardViewBinding>(RvItemCardViewBinding::inflate) { vb, holder, position ->
                 val str = getItem(position) ?: return@setupVB
                 vb.titleTv.text = str
                 var content = str
                 for (i in 0..str.length) {
                     content += str
                 }
                 vb.contentTv.text = content
                 vb.dateTv.text = DateUtils.getCurrentFormatString(DateUtils.TYPE_2)
             }.apply {
                 this.setOnItemClickListener{viewHolder, item, position ->
                     SnackbarUtils.createShort(mBinding.recyclerView, item)
                         .setBackgroundColor(getColorCompat(R.color.color_00a0e9))
                         .setTextColor(Color.WHITE)
                         .show()
                 }
             }
             .setData(arrayListOf("欧冠最佳进球新", "37岁姚笛近照曝光", "5G商用牌照将发布", "纳达尔横扫锦织圭新", "章莹颖失踪案开庭"
                 , "优衣库联名遭疯抢", "文言文写钢铁侠传", "校长表白毕业生", "世界环境日新", "内马尔或将被起诉"))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}