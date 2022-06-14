package com.lodz.android.agiledevkt.modules.json
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.bean.MockBean
import com.lodz.android.agiledevkt.bean.SpotBean
import com.lodz.android.agiledevkt.bean.base.response.ResponseBean
import com.lodz.android.agiledevkt.databinding.ActivityJsonTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.intentExtrasNoNull
import com.lodz.android.corekt.anko.then
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.jackson.parseObject
import com.lodz.android.pandora.utils.jackson.toJsonString
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * Json测试类
 * @author zhouL
 * @date 2022/6/13
 */
class JsonTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, JsonTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    /** 默认对象Json字符 */
    private val mDefObjJson = "{\"name\":\"鼓浪屿\",\"score\":\"10分\"}"
    /** 默认数组Json字符 */
    private val mDefListJson = "[{\"name\":\"鼓浪屿\",\"score\":\"10分\"},{\"name\":\"南普陀\",\"score\":\"8分\"}]"
    /** 默认数组Json字符 */
    private val mDefObjListJson = "{\"code\":200,\"msg\":\"请求成功\",\"data\":[{\"name\":\"鼓浪屿\",\"score\":\"10分\"},{\"name\":\"南普陀\",\"score\":\"8分\"}]}"
    /** 默认泛型Json字符 */
    private val mDefTypeRefJson = "{\"code\":200,\"msg\":\"请求成功\",\"data\":{\"id\":1,\"loginName\":\"admin\",\"roles\":[{\"name\":\"鼓浪屿\",\"score\":\"10分\"},{\"name\":\"南普陀\",\"score\":\"8分\"}]}}"

    private val mBinding: ActivityJsonTestBinding by bindingLayout(ActivityJsonTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private val mTitle by intentExtrasNoNull(MainActivity.EXTRA_TITLE_NAME, "")

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(mTitle)
        setDefJsonString()
    }

    private fun setDefJsonString() {
        mBinding.defJsonTv.text = "1. ".append(mDefObjJson).append("\n\n")
            .append("2. ").append(mDefListJson).append("\n\n")
            .append("3. ").append(mDefObjListJson).append("\n\n")
            .append("4. ").append(mDefTypeRefJson)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.cleanBtn.setOnClickListener {
            cleanResult()
        }

        mBinding.jsonToObjBtn.setOnClickListener {
            cleanResult()
            val spotBean = mDefObjJson.parseObject<SpotBean>()
            addLog("SpotBean : ${spotBean.name} , ${spotBean.score}")

            val list = mDefListJson.parseObject<List<SpotBean>>()
            addLog("List<SpotBean> : ${list[0].name} , ${list[0].score} ; ${list[1].name} , ${list[1].score}")

            val responseBean = mDefObjListJson.parseObject<ResponseBean<List<SpotBean>>>()
            addLog("ResponseBean<List<SpotBean>> : ${responseBean.code} , ${responseBean.msg} ; " +
                    "${responseBean.data?.get(0)?.name} , ${responseBean.data?.get(0)?.score} ; " +
                    "${responseBean.data?.get(1)?.name} , ${responseBean.data?.get(1)?.score}")

            val resMockBean = mDefTypeRefJson.parseObject<ResponseBean<MockBean<SpotBean>>>()
            addLog("ResponseBean<MockBean<SpotBean>> : ${resMockBean.code} , ${responseBean.msg} ; " +
                    "${resMockBean.data?.id} , ${resMockBean.data?.loginName} ; " +
                    "${resMockBean.data?.roles?.get(0)?.name} , ${resMockBean.data?.roles?.get(0)?.score} ; " +
                    "${resMockBean.data?.roles?.get(1)?.name} , ${resMockBean.data?.roles?.get(1)?.score}")
        }

        mBinding.objToJsonBtn.setOnClickListener {
            cleanResult()
            val spotBeanXD = SpotBean()
            spotBeanXD.name = "厦大"
            spotBeanXD.score = "10分"
            val spotBeanZSL = SpotBean()
            spotBeanZSL.name = "中山路"
            spotBeanZSL.score = "8分"
            val list = arrayListOf(spotBeanXD, spotBeanZSL)
            val mockBean = MockBean<SpotBean>()
            mockBean.id = 123
            mockBean.loginName = "root"
            mockBean.roles = list

            val responseBean = ResponseBean.createSuccess("成功", spotBeanZSL)
            val resListBean = ResponseBean.createSuccess("成功", list)
            val resMockBean = ResponseBean.createSuccess("成功", mockBean)

            addLog("SpotBean : ${spotBeanXD.toJsonString()}")
            addLog("List<SpotBean> : ${list.toJsonString()}")
            addLog("ResponseBean<SpotBean> : ${responseBean.toJsonString()}")
            addLog("ResponseBean<List<SpotBean>> : ${resListBean.toJsonString()}")
            addLog("ResponseBean<MockBean<SpotBean>> : ${resMockBean.toJsonString()}")
        }
    }

    private fun addLog(log: String) {
        val str = mBinding.resultTv.text
        mBinding.resultTv.text = str.isEmpty().then { log } ?: str.append("\n\n").append(log)
    }

    private fun cleanResult() {
        mBinding.resultTv.text = ""
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}