package com.lodz.android.agiledevkt.modules.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.PersonTypeBean
import com.lodz.android.agiledevkt.databinding.ActivityCollectBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.collect.radio.Radioable
import kotlinx.coroutines.MainScope
import java.util.*
import kotlin.collections.ArrayList

/**
 * 采集测试页
 * @author zhouL
 * @date 2019/3/18
 */
class CollectActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CollectActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityCollectBinding by bindingLayout(ActivityCollectBinding::inflate)

    /** 姓名列表 */
    private val NAME_LIST = arrayListOf("张三", "李四", "王五", "赵四")
    /** 性别列表 */
    private val SEX_LIST = arrayListOf("男", "女", "未知")
    /** 人员类型列表 */
    private val PERSON_TYPE_LIST = arrayListOf("户籍人员", "流动人员", "境外人员")
    /** 运动类型列表 */
    private val SPORT_TYPE_LIST = arrayListOf("唱", "跳", "rap", "篮球")

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        // 姓名
        mBinding.nameCltv.setOnJumpClickListener {
            val dialog = OptionsPickerBuilder(getContext()) { options1, options2, options3, v ->
                mBinding.nameCltv.setContentText(NAME_LIST[options1])
            }
                .setSubmitText(getString(R.string.clt_confirm))
                .setCancelText(getString(R.string.clt_cancel))
                .setTitleText(getString(R.string.clt_name_title))
                .build<String>()
            dialog.setPicker(NAME_LIST)
            dialog.show()
        }

        // 性别
        mBinding.sexCltv.setOnContentClickListener {
            val dialog = OptionsPickerBuilder(getContext()) { options1, options2, options3, v ->
                mBinding.sexCltv.setContentText(SEX_LIST[options1])
            }
                .setSubmitText(getString(R.string.clt_confirm))
                .setCancelText(getString(R.string.clt_cancel))
                .setTitleText(getString(R.string.clt_sex_title))
                .build<String>()
            dialog.setPicker(SEX_LIST)
            dialog.show()
        }

        // 出生日期
        mBinding.birthCltv.setOnContentClickListener {
            var calendar = DateUtils.parseFormatToCalendar(DateUtils.TYPE_13, mBinding.birthCltv.getContentText())
            if (calendar == null) {
                calendar = Calendar.getInstance()
            }
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            startDate.set(startDate.get(Calendar.YEAR) - 100, Calendar.JANUARY, 1)
            endDate.set(endDate.get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31)

            val dialog = TimePickerBuilder(getContext()) { date, v ->
                val time = if (date != null) DateUtils.getFormatString(DateUtils.TYPE_13, date) else ""
                if (time.isNotEmpty()) {
                    mBinding.birthCltv.setContentText(time)
                    val age = DateUtils.getCurrentFormatString(DateUtils.TYPE_14).toInt() -
                            DateUtils.changeFormatString(DateUtils.TYPE_13, DateUtils.TYPE_14, time).toInt()
                    mBinding.ageCltv.setContentText(if (age >= 0) age.toString() else "")
                }
            }
                .setTitleText(getString(R.string.clt_birth_title))
                .setDate(calendar)
                .setRangDate(startDate, endDate)
                .setType(booleanArrayOf(true, true, false, false, false, false))
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCyclic(true)
                .build()
            dialog.show()
        }

        // 备注
        mBinding.remarkCedit.setOnInputTextLimit { s, start, before, count, max ->
            toastShort("只能输入${max}字")
        }

        // 提交按钮
        mBinding.submitBtn.setOnClickListener {
            if (mBinding.nameCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_name_tips)
                return@setOnClickListener
            }
            if (mBinding.sexCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_sex_hint)
                return@setOnClickListener
            }
            if (mBinding.birthCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_birth_hint)
                return@setOnClickListener
            }
            if (mBinding.hobbyCedit.getContentText().isEmpty()) {
                toastShort(R.string.clt_hobby_tips)
                return@setOnClickListener
            }
            if (!mBinding.personTypeCrg.isSelectedId()){
                toastShort(R.string.clt_person_type_tips)
                return@setOnClickListener
            }
            toastShort(R.string.clt_success)
            MainScope().runOnMainDelay(500) {
                toastShort("提交完成")
                finish()
            }
        }
    }

    override fun initData() {
        super.initData()
        mBinding.personTypeCrg.setDataList(getPersonTypeList())
        mBinding.sportTypeCrg.setDataList(getSportTypeList())
        showStatusCompleted()
    }

    /** 获取人员类型列表 */
    private fun getPersonTypeList(): MutableList<Radioable> {
        val list: MutableList<Radioable> = ArrayList()
        PERSON_TYPE_LIST.forEachIndexed { index, type ->
            val personTypeBean = PersonTypeBean()
            personTypeBean.id = index.toString()
            personTypeBean.name = type
            list.add(personTypeBean)
        }
        return list
    }

    /** 获取运动类型列表 */
    private fun getSportTypeList(): MutableList<Radioable> {
        val list: MutableList<Radioable> = ArrayList()
        SPORT_TYPE_LIST.forEachIndexed { index, type ->
            val personTypeBean = PersonTypeBean()
            personTypeBean.id = index.toString()
            personTypeBean.name = type
            list.add(personTypeBean)
        }
        return list
    }

}