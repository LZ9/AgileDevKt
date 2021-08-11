package com.lodz.android.agiledevkt.modules.array

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityArrayTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.deduplication
import com.lodz.android.corekt.anko.getSize
import com.lodz.android.corekt.anko.group
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

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

    private val mBinding: ActivityArrayTestBinding by bindingLayout(ActivityArrayTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(intent.getStringExtra(MainActivity.EXTRA_TITLE_NAME) ?: "")
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()

        // 去重
        mBinding.deduplicationBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.deduplication().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.deduplication().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.deduplication().contentToString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.deduplication().toString())
            mBinding.resultTv.text = result
        }

        // 获取长度
        mBinding.getSizeBtn.setOnClickListener {
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
            mBinding.resultTv.text = result
        }

        // 默认排序
        mBinding.defaultSortBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.sorted().toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.sorted().toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.sortedArray().contentToString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.sorted().toString())
            mBinding.resultTv.text = result
        }

        // 取最大值
        mBinding.getMaxBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.maxOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.maxOrNull()?.toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.maxOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.maxOrNull()?.toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.maxOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.maxOrNull()?.toString())
            mBinding.resultTv.text = result
        }

        // 取最小值
        mBinding.getMinBtn.setOnClickListener {
            val result = "---数字Int---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY.minOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST.minOrNull()?.toString()) + "\n" +
                    "\n---数字Long---\n" +
                    getString(R.string.array_toarray, NUM_ARRAY_LONG.minOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, NUM_LIST_LONG.minOrNull()?.toString()) + "\n" +
                    "\n---字符---\n" +
                    getString(R.string.array_toarray, STR_ARRAY.minOrNull()?.toString()) + "\n" +
                    getString(R.string.array_tolist, STR_LIST.minOrNull()?.toString())
            mBinding.resultTv.text = result
        }

        // 数据分组
        mBinding.groupBtn.setOnClickListener {
            val staffs = ArrayList<StaffBean>()
            for (i in 0..5) {
                val bean = StaffBean()
                bean.id = NUM_LIST_LONG[i]
                bean.name = STR_LIST[i]
                bean.age = NUM_LIST[i]
                staffs.add(bean)
            }

            val result = getString(R.string.array_source, staffs.toString()) + "\n" +
                    getString(R.string.array_group_result, staffs.group(STR_GROUP.toList()).toString())
            mBinding.resultTv.text = result

//            val result = getString(R.string.array_source, STR_ARRAY.contentToString()) + "\n" +
//                    getString(R.string.array_group_result, STR_ARRAY.group(STR_GROUP).contentToString())
//            mResultTv.text = result
        }
    }

    override fun initData() {
        super.initData()
        mBinding.numIntTv.text = getString(R.string.array_num_int, NUM_ARRAY.contentToString())
        mBinding.numLongTv.text = getString(R.string.array_num_long, NUM_ARRAY_LONG.contentToString())
        mBinding.strArrayTv.text = getString(R.string.array_str, STR_ARRAY.contentToString())
        showStatusCompleted()
    }


}