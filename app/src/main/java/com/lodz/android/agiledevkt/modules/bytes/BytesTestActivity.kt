package com.lodz.android.agiledevkt.modules.bytes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.ActivityBytesTestBinding
import com.lodz.android.agiledevkt.modules.main.MainActivity
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import okio.ByteString.Companion.toByteString
import java.util.*

/**
 * 字节转换测试类
 * @author zhouL
 * @date 2021/10/27
 */
class BytesTestActivity :BaseActivity(){

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, BytesTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val mBinding: ActivityBytesTestBinding by bindingLayout(ActivityBytesTestBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    private val mTitle by intentExtrasNoNull(MainActivity.EXTRA_TITLE_NAME, "")

    override fun findViews(savedInstanceState: Bundle?) {
        getTitleBarLayout().setTitleName(mTitle)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun setListeners() {
        super.setListeners()
        mBinding.numCltedit.setOnJumpClickListener {
            val numStr = mBinding.numCltedit.getContentText()
            if (numStr.isEmpty()){
                toastShort(R.string.bytes_input_num)
                return@setOnJumpClickListener
            }
            val num = numStr.toInt()
            intByteArrayTransform(num)
            arrayBufferTransform(num)
            byteArrayBaseTransform(num)
            shortArrayByteArray(num)
            try {
            } catch (e: Exception) {
                e.printStackTrace()
                toastShort(R.string.bytes_num_no_int)
            }
        }
    }

    private fun intByteArrayTransform(num: Int) {
        val byteArrayHf = num.toByteArrayHF()
        val byteArrayLf = num.toByteArrayLF()
        mBinding.intToBytearrayHfTv.text = getString(R.string.bytes_int_to_bytearray_hf).append(byteArrayHf.contentToString())
        mBinding.intToBytearrayLfTv.text = getString(R.string.bytes_int_to_bytearray_lf).append(byteArrayLf.contentToString())
        mBinding.bytearrayHfToIntTv.text = getString(R.string.bytes_bytearray_hf_to_int).append(byteArrayHf.toInt())
        mBinding.bytearrayLfToIntTv.text = getString(R.string.bytes_bytearray_lf_to_int).append(byteArrayLf.toInt())
        mBinding.mergeTv.text = getString(R.string.bytes_merge).append(byteArrayHf.merge(byteArrayLf).contentToString())
    }

    private fun arrayBufferTransform(num: Int) {
        val byteArray = num.toByteArray()
        val byteBuffer = byteArray.toByteBuffer()
        mBinding.arrayToBufferTv.text = getString(R.string.bytes_array_to_buffer).append(byteBuffer.toByteString())
        mBinding.bufferToArrayTv.text = getString(R.string.bytes_buffer_to_array).append(byteBuffer.array().contentToString())
    }

    private fun byteArrayBaseTransform(num: Int) {
        val intNum: Int = num * -1
        val shortNum: Short = (num % 9).toShort()
        val longNum = num + 10000000000
        val floatNum = num * 1.11f
        val doubleNum = num * 2.555555555555555555

        mBinding.arrayToIntTv.text = getString(R.string.bytes_array_to_int).append(intNum.toByteArray().toInt())
        mBinding.arrayToShortTv.text = getString(R.string.bytes_array_to_short).append(shortNum.toByteArray().toShort())
        mBinding.arrayToLongTv.text = getString(R.string.bytes_array_to_long).append(longNum.toByteArray().toLong())
        mBinding.arrayToFloatTv.text = getString(R.string.bytes_array_to_float).append(floatNum.toByteArray().toFloat())
        mBinding.arrayToDoubleTv.text = getString(R.string.bytes_array_to_double).append(doubleNum.toByteArray().toDouble())
        mBinding.arrayToShortarrayTv.text = getString(R.string.bytes_array_to_shortarray).append(num.toByteArray().toShortArray().contentToString())
    }

    private fun shortArrayByteArray(num: Int) {
        val byteArray = num.toByteArray()
        val shortArray = byteArray.toShortArray()
        mBinding.shortarrayToArrayTv.text = getString(R.string.bytes_shortarray_to_array).append(shortArray.contentToString())
    }

    override fun initData() {
        super.initData()
        showStatusCompleted()
    }
}