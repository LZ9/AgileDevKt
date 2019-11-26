package com.lodz.android.agiledevkt.modules.date

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.google.android.material.button.MaterialButton
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.utils.DateUtils
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.widget.collect.CltTextView
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

    /** 日期格式选择控件 */
    private val mFormatTypeClttv by bindView<CltTextView>(R.id.format_type_clttv)
    /** 当前日期 */
    private val mCurrentDateTv by bindView<TextView>(R.id.current_date_tv)
    /** 当前日期之前5天 */
    private val mBeforeTv by bindView<TextView>(R.id.before_tv)
    /** 当前日期之后5天 */
    private val mAfterTv by bindView<TextView>(R.id.after_tv)
    /** 系统日期选择框 */
    private val mDatePickerBtn by bindView<MaterialButton>(R.id.date_picker_btn)
    /** 选择日期 */
    private val mDatePickerTv by bindView<TextView>(R.id.date_picker_tv)
    /** 系统时间选择框 */
    private val mTimePickerBtn by bindView<MaterialButton>(R.id.time_picker_btn)
    /** 选择时间 */
    private val mTimePickerTv by bindView<TextView>(R.id.time_picker_tv)
    /** 时间日期滚动选择框 */
    private val mTimeWheelBtn by bindView<MaterialButton>(R.id.time_wheel_btn)
    /** 选择时间日期 */
    private val mTimeWheelTv by bindView<TextView>(R.id.time_wheel_tv)

    override fun getLayoutId(): Int = R.layout.activity_date

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
        mFormatTypeClttv.setOnContentClickListener {
            val dialog = OptionsPickerBuilder(getContext(),
                OnOptionsSelectListener { options1, options2, options3, v ->
                    mFormatTypeClttv.setContentText(FORMAT_TYPE_LIST[options1])
                    mFormatTypeClttv.setContentTag(options1.toString())
                    updateUI()
                })
                    .setSubmitText(getString(R.string.date_confirm))
                    .setCancelText(getString(R.string.date_cancel))
                    .setTitleText(getString(R.string.date_format_title))
                    .setSelectOptions(mFormatTypeClttv.getContentTag().toInt())
                    .build<String>()
            dialog.setPicker(FORMAT_TYPE_LIST)
            dialog.show()
        }

        mTimeWheelBtn.setOnClickListener {
            var calendar = DateUtils.parseFormatToCalendar(DateUtils.TYPE_2, mTimeWheelTv.text.toString())
            if (calendar == null) {
                calendar = Calendar.getInstance()
            }

            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            startDate.set(startDate.get(Calendar.YEAR) - 100, Calendar.JANUARY, 1)
            endDate.set(endDate.get(Calendar.YEAR) + 100, Calendar.DECEMBER, 31)

            val dialog = TimePickerBuilder(getContext(),
                OnTimeSelectListener { date, v -> mTimeWheelTv.text = if (date != null) DateUtils.getFormatString(DateUtils.TYPE_2, date) else "" })
                    .setTitleText(getString(R.string.date_pick_title))
                    .setDate(calendar)
                    .setRangDate(startDate, endDate)
                    .setType(booleanArrayOf(true, true, true, true, true, true))
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCyclic(true)
                    .build()
            dialog.show()
        }

        mDatePickerBtn.setOnClickListener {
            var calendar = Calendar.getInstance()
            val dateStr = mDatePickerTv.text.toString()
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
                    mDatePickerTv.text = DateUtils.parseFormatCalendar(DateUtils.TYPE_6, c)
                }, calendar)
        }

        mTimePickerBtn.setOnClickListener {
            var calendar = Calendar.getInstance()
            val dateStr = mTimePickerTv.text.toString()
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
                    mTimePickerTv.text = DateUtils.parseFormatCalendar(DateUtils.TYPE_8, c)
                }, calendar)
        }
    }


    override fun initData() {
        super.initData()
        mFormatTypeClttv.setContentText(FORMAT_TYPE_LIST[0])
        mFormatTypeClttv.setContentTag("0")
        updateUI()
        showStatusCompleted()
    }

    /** 更新界面 */
    private fun updateUI() {
        val current = DateUtils.getCurrentFormatString(mFormatTypeClttv.getContentText())
        mCurrentDateTv.text = current
        mBeforeTv.text = DateUtils.getBeforeDay(mFormatTypeClttv.getContentText(), current, 5)
        mAfterTv.text = DateUtils.getAfterDay(mFormatTypeClttv.getContentText(), current, 5)
    }
}