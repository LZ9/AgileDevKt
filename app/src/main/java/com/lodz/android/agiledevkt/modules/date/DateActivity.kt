package com.lodz.android.agiledevkt.modules.date

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityDateBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import java.util.*


/**
 * 日期测试类
 * @author zhouL
 * @date 2019/4/1
 */
class DateActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, DateActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val FORMAT_TYPE_LIST = arrayListOf("yyyyMMddHHmmss", "HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmssSSS", "yyyyMMdd"
            , "yyyy-MM-dd", "yyyy-MM-dd-HH-mm-ss", "HH:mm:ss", "yyyy-MM-dd HH-mm-ss", "yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd HH:mm", "yyyyMM")

    private val mBinding: ActivityDateBinding by bindingLayout(ActivityDateBinding::inflate)

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

        // 日期格式选择控件
        mBinding.formatTypeClttv.setOnContentClickListener {
            val dialog = OptionsPickerBuilder(getContext()) { options1, options2, options3, v ->
                mBinding.formatTypeClttv.setContentText(FORMAT_TYPE_LIST[options1])
                mBinding.formatTypeClttv.setContentTag(options1.toString())
                updateUI()
            }
                .setSubmitText(getString(R.string.date_confirm))
                .setCancelText(getString(R.string.date_cancel))
                .setTitleText(getString(R.string.date_format_title))
                .setSelectOptions(mBinding.formatTypeClttv.getContentTag().toInt())
                .build<String>()
            dialog.setPicker(FORMAT_TYPE_LIST)
            dialog.show()
        }

        // 时间日期滚动选择框
        mBinding.timeWheelBtn.setOnClickListener {
            var calendar = DateUtils.parseFormatToCalendar(DateUtils.TYPE_2, mBinding.timeWheelTv.text.toString())
            if (calendar == null) {
                calendar = Calendar.getInstance()
            }

            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            startDate.set(startDate.get(Calendar.YEAR) - 100, Calendar.JANUARY, 1)
            endDate.set(endDate.get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31)

            val dialog = TimePickerBuilder(getContext()) { date, v ->
                mBinding.timeWheelTv.text =
                    if (date != null) DateUtils.getFormatString(DateUtils.TYPE_2, date) else ""
            }
                .setTitleText(getString(R.string.date_pick_title))
                .setDate(calendar)
                .setRangDate(startDate, endDate)
                .setType(booleanArrayOf(true, true, true, true, true, true))
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCyclic(true)
                .setSubmitColor(Color.BLUE)
                .setCancelColor(Color.BLUE)
                .build()
            dialog.show()
        }

        // 系统日期选择框
        mBinding.datePickerBtn.setOnClickListener {
            var calendar = Calendar.getInstance()
            val dateStr = mBinding.datePickerTv.text.toString()
            if (dateStr.isNotEmpty()) {
                val result = DateUtils.parseFormatToCalendar(DateUtils.TYPE_6, dateStr)
                if (result != null) {
                    calendar = result
                }
            }

            DateUtils.showDatePicker(getContext(),
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val c = Calendar.getInstance()
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, month)
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    mBinding.datePickerTv.text = DateUtils.parseFormatCalendar(DateUtils.TYPE_6, c)
                }, calendar)
        }

        // 系统时间选择框
        mBinding.timePickerBtn.setOnClickListener {
            var calendar = Calendar.getInstance()
            val dateStr = mBinding.timePickerTv.text.toString()
            if (dateStr.isNotEmpty()) {
                val result = DateUtils.parseFormatToCalendar(DateUtils.TYPE_8, dateStr)
                if (result != null) {
                    calendar = result
                }
            }

            DateUtils.showTimePicker(getContext(),
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    val c = Calendar.getInstance()
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    c.set(Calendar.MINUTE, minute)
                    mBinding.timePickerTv.text = DateUtils.parseFormatCalendar(DateUtils.TYPE_8, c)
                }, calendar)
        }
    }


    override fun initData() {
        super.initData()
        mBinding.formatTypeClttv.setContentText(FORMAT_TYPE_LIST[0])
        mBinding.formatTypeClttv.setContentTag("0")
        updateUI()
        showStatusCompleted()
    }

    /** 更新界面 */
    private fun updateUI() {
        val current = DateUtils.getCurrentFormatString(mBinding.formatTypeClttv.getContentText())
        mBinding.currentDateTv.text = current
        mBinding.beforeTv.text = DateUtils.getBeforeDay(mBinding.formatTypeClttv.getContentText(), current, 5)
        mBinding.afterTv.text = DateUtils.getAfterDay(mBinding.formatTypeClttv.getContentText(), current, 5)
    }
}