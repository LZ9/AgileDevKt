package com.lodz.android.agiledevkt.modules.array

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.bindView
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.anko.group
import com.lodz.android.pandora.base.activity.BaseActivity

/**
 * 数组列表测试类
 * Created by zhouL on 2018/11/2.
 */
class ArrayTestActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ArrayTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val NUM_ARRAY = arrayOf(32, 4, 54, 76, 1, 23, 4, 4, 54, 87, 65)
    private val NUM_LIST = NUM_ARRAY.toList()
    private val NUM_ARRAY_NULL: Array<Int>? = null
    private val NUM_LIST_NULL: List<Int>? = null

    private val NUM_ARRAY_LONG = arrayOf(7986546, 12, 123, 3431, 22, 7986546L, 3431, 123L, 123, 458789)
    private val NUM_LIST_LONG = NUM_ARRAY_LONG.toList()
    private val NUM_ARRAY_LONG_NULL: Array<Long>? = null
    private val NUM_LIST_LONG_NULL: List<Long>? = null

    private val STR_ARRAY = arrayOf("Jack", "May", "Rose", "Lucy", "Lucy", "Owen", "Jack", "Jack", "Peter", "Park", "Lucy", "Peter", "May")
    private val STR_LIST = STR_ARRAY.toList()
    private val STR_ARRAY_NULL: Array<String>? = null
    private val STR_LIST_NULL: List<String>? = null

    private val NUM_GROUP = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    private val STR_GROUP = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    /** 数字int数组 */
    private val mNumIntTv by bindView<TextView>(R.id.num_int_tv)
    /** 数字long数组 */
    private val mNumLongTv by bindView<TextView>(R.id.num_long_tv)
    /** 字符数组 */
    private val mStrArrayTv by bindView<TextView>(R.id.str_array_tv)

    /** 结果 */
    private val mResultTv by bindView<TextView>(R.id.result_tv)

    /** 去重 */
    private val mDeduplicationBtn by bindView<Button>(R.id.deduplication_btn)
    /** 获取长度 */
    private val mGetSizeBtn by bindView<Button>(R.id.get_size_btn)
    /** 默认排序 */
    private val mdefaultSortBtn by bindView<Button>(R.id.default_sort_btn)
    /** 取最大值 */
    private val mGetMaxBtn by bindView<Button>(R.id.get_max_btn)
    /** 取最小值 */
    private val mGetMinBtn by bindView<Button>(R.id.get_min_btn)
    /** 数据分组 */
    private val mGroupBtn by bindView<Button>(R.id.group_btn)


    override fun getLayoutId(): Int = R.layout.activity_array_test

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME))
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 去重
        mDeduplicationBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.deduplication().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.deduplication().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.deduplication().toString())
            mResultTv.text = result
        }

        // 获取长度
        mGetSizeBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.getSize().toString()) + "\n" +
                    getString(R.string.array_toarray_null, NUM_ARRAY_NULL.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist_null, NUM_LIST_NULL.getSize().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.getSize().toString()) + "\n" +
                    getString(R.string.array_toarray_null, NUM_ARRAY_LONG_NULL.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist_null, NUM_LIST_LONG_NULL.getSize().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.getSize().toString()) + "\n" +
                    getString(R.string.array_toarray_null, STR_ARRAY_NULL.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.getSize().toString()) + "\n" +
                    getString(R.string.array_tolist_null, STR_LIST_NULL.getSize().toString())
            mResultTv.text = result
        }

        // 默认排序
        mdefaultSortBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.sorted().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.sorted().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.sorted().toString())
            mResultTv.text = result
        }

        // 取最大值
        mGetMaxBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.max().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.max().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.max().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.max().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.max().toString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.max().toString())
            mResultTv.text = result
        }

        // 取最小值
        mGetMinBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.min().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.min().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.min().toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.min().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.min().toString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.min().toString())
            mResultTv.text = result
        }

        // 数据分组
        mGroupBtn.setOnClickListener {
            val staffs = ArrayList<StaffBean>()
            for (i in 0..5) {
                val bean = StaffBean()
                bean.id = NUM_LIST_LONG.get(i)
                bean.name = STR_LIST.get(i)
                bean.age = NUM_LIST.get(i)
                staffs.add(bean)
            }

            val result = getString(R.string.array_source, staffs.toString()) + "\n" +
                    getString(R.string.array_group_result, staffs.group(STR_GROUP.toList()).toString())
            mResultTv.text = result

//            val result = getString(R.string.array_source, STR_ARRAY.contentToString()) + "\n" +
//                    getString(R.string.array_group_result, STR_ARRAY.group(STR_GROUP).contentToString())
//            mResultTv.text = result
        }
    }

    override fun initData() {
        super.initData()
        mNumIntTv.text = getString(R.string.array_num_int, NUM_ARRAY.contentToString())
        mNumLongTv.text = getString(R.string.array_num_long, NUM_ARRAY_LONG.contentToString())
        mStrArrayTv.text = getString(R.string.array_str, STR_ARRAY.contentToString())
        showStatusCompleted()
    }


}