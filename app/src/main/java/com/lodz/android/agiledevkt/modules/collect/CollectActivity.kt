package com.lodz.android.agiledevkt.modules.collect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.PersonTypeBean
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.runOnMainDelay
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.collect.CltTextView
import com.lodz.android.pandora.widget.collect.radio.CltRadioGroup
import com.lodz.android.pandora.widget.collect.radio.Radioable
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

    /** 姓名列表 */
    private val NAME_LIST = arrayListOf("张三", "李四", "王五", "赵四")
    /** 性别列表 */
    private val SEX_LIST = arrayListOf("男", "女", "未知")
    /** 人员类型列表 */
    private val PERSON_TYPE_LIST = arrayListOf("户籍人员", "流动人员", "境外人员")
    /** 运动类型列表 */
    private val SPORT_TYPE_LIST = arrayListOf("唱", "跳", "rap", "篮球")

    /** 姓名 */
    private val mNameCltv by bindView<CltTextView>(R.id.name_cltv)
    /** 性别 */
    private val mSexCltv by bindView<CltTextView>(R.id.sex_cltv)
    /** 出生日期 */
    private val mBirthCltv by bindView<CltTextView>(R.id.birth_cltv)
    /** 年龄 */
    private val mAgeCltv by bindView<CltTextView>(R.id.age_cltv)
    /** 兴趣 */
    private val mHobbyCedit by bindView<CltEditView>(R.id.hobby_cedit)
    /** 备注 */
    private val mRemarkCedit by bindView<CltEditView>(R.id.remark_cedit)
    /** 人员类型 */
    private val mPersonTypeCrg by bindView<CltRadioGroup>(R.id.person_type_crg)
    /** 运动类型 */
    private val mSportTypeCrg by bindView<CltRadioGroup>(R.id.sport_type_crg)
    /** 提交按钮 */
    private val mSubmitBtn by bindView<TextView>(R.id.submit_btn)

    override fun getLayoutId(): Int = R.layout.activity_collect

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mNameCltv.setOnJumpClickListener {
            val dialog = OptionsPickerBuilder(getContext(),
                    object : OnOptionsSelectListener {
                        override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                            mNameCltv.setContentText(NAME_LIST[options1])
                        }
                    })
                    .setSubmitText(getString(R.string.clt_confirm))
                    .setCancelText(getString(R.string.clt_cancel))
                    .setTitleText(getString(R.string.clt_name_title))
                    .build<String>()
            dialog.setPicker(NAME_LIST)
            dialog.show()
        }

        mSexCltv.setOnContentClickListener {
            val dialog = OptionsPickerBuilder(getContext(),
                    object : OnOptionsSelectListener {
                        override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
                            mSexCltv.setContentText(SEX_LIST[options1])
                        }
                    })
                    .setSubmitText(getString(R.string.clt_confirm))
                    .setCancelText(getString(R.string.clt_cancel))
                    .setTitleText(getString(R.string.clt_sex_title))
                    .build<String>()
            dialog.setPicker(SEX_LIST)
            dialog.show()
        }

        mBirthCltv.setOnContentClickListener {
            var calendar = DateUtils.parseFormatToCalendar(DateUtils.TYPE_13, mBirthCltv.getContentText())
            if (calendar == null) {
                calendar = Calendar.getInstance()
            }
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            startDate.set(startDate.get(Calendar.YEAR) - 100, Calendar.JANUARY, 1)
            endDate.set(endDate.get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31)

            val dialog = TimePickerBuilder(getContext(),
                    object : OnTimeSelectListener {
                        override fun onTimeSelect(date: Date?, v: View?) {
                            val time = if (date != null) DateUtils.getFormatString(DateUtils.TYPE_13, date) else ""
                            if (time.isNotEmpty()) {
                                mBirthCltv.setContentText(time)
                                val age = DateUtils.getCurrentFormatString(DateUtils.TYPE_14).toInt() - DateUtils.changeFormatString(DateUtils.TYPE_13, DateUtils.TYPE_14, time).toInt()
                                mAgeCltv.setContentText(if (age > 0) age.toString() else "")
                            }
                        }
                    })
                    .setTitleText(getString(R.string.clt_birth_title))
                    .setDate(calendar)
                    .setRangDate(startDate, endDate)
                    .setType(booleanArrayOf(true, true, false, false, false, false))
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCyclic(true)
                    .build()
            dialog.show()
        }

        mRemarkCedit.setOnInputTextLimit { s, start, before, count, max ->
            toastShort("只能输入${max}字")
        }

        mSubmitBtn.setOnClickListener {
            if (mNameCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_name_tips)
                return@setOnClickListener
            }
            if (mSexCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_sex_hint)
                return@setOnClickListener
            }
            if (mBirthCltv.getContentText().isEmpty()) {
                toastShort(R.string.clt_birth_hint)
                return@setOnClickListener
            }
            if (mHobbyCedit.getContentText().isEmpty()) {
                toastShort(R.string.clt_hobby_tips)
                return@setOnClickListener
            }
            if (!mPersonTypeCrg.isSelectedId()){
                toastShort(R.string.clt_person_type_tips)
                return@setOnClickListener
            }
            toastShort(R.string.clt_success)
            runOnMainDelay(500) {
                finish()
            }
        }
    }

    override fun initData() {
        super.initData()
        mPersonTypeCrg.setDataList(getPersonTypeList())
        mSportTypeCrg.setDataList(getSportTypeList())
        showStatusCompleted()
    }

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