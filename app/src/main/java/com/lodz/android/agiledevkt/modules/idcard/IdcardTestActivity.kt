package com.lodz.android.agiledevkt.modules.idcard

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.SearchRecomBean
import com.lodz.android.agiledevkt.databinding.ActivityIdCardTestBinding
import com.lodz.android.corekt.anko.toArrayList
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.corekt.utils.IdCardUtils
import com.lodz.android.pandora.base.activity.AbsActivity
import com.lodz.android.pandora.utils.acache.ACacheUtils
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.search.OnSearchRecomdListener
import com.lodz.android.pandora.widget.search.RecomdData
import kotlin.random.Random

/**
 * 身份证号码测试类
 * Created by zhouL on 2018/7/17.
 */
class IdcardTestActivity : AbsActivity() {

    companion object {
        const val CACHE_KEY = "idcard_cache_key"

        fun start(context: Context) {
            val intent = Intent(context, IdcardTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityIdCardTestBinding by bindingLayout(ActivityIdCardTestBinding::inflate)

    override fun getAbsViewBindingLayout(): View = mBinding.root

    override fun setListeners() {
        super.setListeners()

        // 返回按钮
        mBinding.searchTitleLayout.setOnBackBtnClickListener {
            finish()
        }

        // 搜索按钮
        mBinding.searchTitleLayout.setOnSearchClickListener {
            val idcard = mBinding.searchTitleLayout.getInputText()
            if (idcard.isEmpty()){
                mBinding.resultTv.setTextColor(Color.RED)
                mBinding.resultTv.setText(R.string.idcard_empty_tips)
                return@setOnSearchClickListener
            }
            if (idcard.length != 18){
                mBinding.resultTv.setTextColor(Color.RED)
                mBinding.resultTv.setText(R.string.idcard_length_error)
                return@setOnSearchClickListener
            }
            mBinding.searchTitleLayout.setRecomListData(null)
            checkIdcard(idcard)
        }

        // 推荐列表监听器
        mBinding.searchTitleLayout.setOnSearchRecomdListener(object :OnSearchRecomdListener{
            override fun onInputTextChange(text: String) {
                mBinding.searchTitleLayout.setRecomListData(getCacheList(text))
            }

            override fun onItemClick(viewHolder: RecyclerView.ViewHolder, data: RecomdData, position: Int) {
                checkIdcard(data.getTitleText())
            }
        })
    }


    /** 校验身份证号[idcard] */
    private fun checkIdcard(idcard: String) {
        if (!IdCardUtils.validateIdCard(idcard)){
            mBinding.resultTv.setText(R.string.idcard_illegal)
            mBinding.resultTv.setTextColor(Color.RED)
            return
        }
        putCache(idcard)
        mBinding.resultTv.setTextColor(Color.BLACK)
        val result = StringBuilder()
        result.append(getString(R.string.idcard_check_success)).append("\n")
                .append(getString(R.string.idcard_province)).append(IdCardUtils.getProvince(idcard)).append("\n")
                .append(getString(R.string.idcard_sex)).append(IdCardUtils.getSexStr(idcard)).append("\n")
                .append(getString(R.string.idcard_birth)).append(IdCardUtils.getBirth(idcard, DateUtils.TYPE_6)).append("\n")
                .append(getString(R.string.idcard_year)).append(IdCardUtils.getYear(idcard)).append("\n")
                .append(getString(R.string.idcard_month)).append(IdCardUtils.getMonth(idcard)).append("\n")
                .append(getString(R.string.idcard_day)).append(IdCardUtils.getDay(idcard)).append("\n")
                .append(getString(R.string.idcard_age)).append(IdCardUtils.getAge(idcard)).append("\n")

        mBinding.resultTv.text = result
    }

    private fun getCacheList(text: String): MutableList<SearchRecomBean> {
        val list = ArrayList<SearchRecomBean>()
        val json = ACacheUtils.get().create().getAsString(CACHE_KEY)
        val cacheList = if (json.isEmpty()) ArrayList<SearchRecomBean>() else JSON.parseArray(json, SearchRecomBean::class.java).toArrayList()
        if (text.isEmpty()) {
            return list
        }
        cacheList.forEach { cache ->
            if (cache.getTitleText().contains(text)) {
                list.add(cache)
            }
        }
        return list
    }

    private fun putCache(text: String) {
        val json = ACacheUtils.get().create().getAsString(CACHE_KEY)
        val list = if (json.isEmpty()) ArrayList<SearchRecomBean>() else JSON.parseArray(json, SearchRecomBean::class.java).toArrayList()
        var hasCache = false
        for (bean in list) {
            if (bean.getTitleText() == text) {
                hasCache = true
                break
            }
        }
        if (!hasCache) {
            val bean = SearchRecomBean()
            bean.text = text
            val index = Random.nextInt(6)
            if (index > 1){
                bean.desc = bean.text.hashCode().toString()
            }
            if (index > 2){
                bean.titleTag = "认证"
            }
            if (index > 3){
                bean.firstTag = "身份证"
            }
            if (index > 4){
                bean.tips = index.toString()
            }
            if (index > 5){
                bean.secondTag = "证件号"
            }
            list.add(bean)
        }
        ACacheUtils.get().create().put(CACHE_KEY, JSON.toJSONString(list))
    }
}