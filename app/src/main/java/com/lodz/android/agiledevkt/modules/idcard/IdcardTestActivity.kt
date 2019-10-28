package com.lodz.android.agiledevkt.modules.idcard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.SearchRecomBean
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.IdCardUtils
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.widget.search.OnSearchRecomdListener
import com.lodz.android.pandora.widget.search.RecomdData
import com.lodz.android.pandora.widget.search.SearchTitleBarLayout
import java.util.*

/**
 * 身份证号码测试类
 * Created by zhouL on 2018/7/17.
 */
class IdcardTestActivity : AbsActivity() {

    companion object {
        val CACHE = arrayListOf(
            "650202199109251234",
            "653222199010111234",
            "350624197711251234",
            "142730198204021234",
            "513002199007111234",
            "350627199311021234",
            "422128197608291234",
            "372925197710241234",
            "350322198802051234",
            "350122197812161234",
            "130823197012151234",
            "350211199311021234",
            "513001198604041234",
            "130221196302031234"
        )

        fun start(context: Context) {
            val intent = Intent(context, IdcardTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 搜索标题框 */
    private val mSearchTitleBarLayout by bindView<SearchTitleBarLayout>(R.id.search_title_layout)
    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result)

    override fun getAbsLayoutId(): Int = R.layout.activity_id_card_test

    override fun setListeners() {
        super.setListeners()

        // 返回按钮
        mSearchTitleBarLayout.setOnBackBtnClickListener {
            finish()
        }

        // 搜索按钮
        mSearchTitleBarLayout.setOnSearchClickListener {
            val idcard = mSearchTitleBarLayout.getInputText()
            if (idcard.isEmpty()){
                mResultTv.setTextColor(Color.RED)
                mResultTv.setText(R.string.idcard_empty_tips)
                return@setOnSearchClickListener
            }
            if (idcard.length != 18){
                mResultTv.setTextColor(Color.RED)
                mResultTv.setText(R.string.idcard_length_error)
                return@setOnSearchClickListener
            }
            checkIdcard(idcard)
        }

        // 推荐列表监听器
        mSearchTitleBarLayout.setOnSearchRecomdListener(object :OnSearchRecomdListener{
            override fun onInputTextChange(text: String) {
                mSearchTitleBarLayout.setRecomListData(getCacheList(text))
            }

            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, data: RecomdData, position: Int) {
                checkIdcard(data.getTitleText())
            }
        })
    }

    /** 校验身份证号[idcard] */
    private fun checkIdcard(idcard: String) {
        if (!IdCardUtils.validateIdCard(idcard)){
            mResultTv.setText(R.string.idcard_illegal)
            mResultTv.setTextColor(Color.RED)
            return
        }

        mResultTv.setTextColor(Color.BLACK)
        val result = StringBuilder()
        result.append(getString(R.string.idcard_check_success)).append("\n")
                .append(getString(R.string.idcard_province)).append(IdCardUtils.getProvince(idcard)).append("\n")
                .append(getString(R.string.idcard_sex)).append(IdCardUtils.getSexStr(idcard)).append("\n")
                .append(getString(R.string.idcard_birth)).append(IdCardUtils.getBirth(idcard, DateUtils.TYPE_6)).append("\n")
                .append(getString(R.string.idcard_year)).append(IdCardUtils.getYear(idcard)).append("\n")
                .append(getString(R.string.idcard_month)).append(IdCardUtils.getMonth(idcard)).append("\n")
                .append(getString(R.string.idcard_day)).append(IdCardUtils.getDay(idcard)).append("\n")
                .append(getString(R.string.idcard_age)).append(IdCardUtils.getAge(idcard)).append("\n")

        mResultTv.text = result
    }

    private fun getCacheList(text: String): MutableList<RecomdData> {
        val list = ArrayList<RecomdData>()
        if (text.isEmpty()) {
            return list
        }
        CACHE.forEach { cache ->
            if (cache.contains(text)) {
                list.add(SearchRecomBean(cache))
            }
        }
        return list
    }
}