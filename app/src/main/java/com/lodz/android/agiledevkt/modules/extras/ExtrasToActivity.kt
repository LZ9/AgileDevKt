package com.lodz.android.agiledevkt.modules.extras

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.ParcelableBean
import com.lodz.android.agiledevkt.bean.SerializableBean
import com.lodz.android.agiledevkt.databinding.ActivityExtrasResultBinding
import com.lodz.android.corekt.anko.*
import com.lodz.android.pandora.base.activity.BaseActivity
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 参数传递给Activity
 * @author zhouL
 * @date 2021/11/1
 */
class ExtrasToActivity : BaseActivity() {

    companion object {
        private val DEFAULT_CS: CharSequence = "abc123"

        private const val EXTRA_INT = "extra_int"
        private const val EXTRA_SHORT = "extra_short"
        private const val EXTRA_BYTE = "extra_byte"
        private const val EXTRA_CHAR = "extra_char"
        private const val EXTRA_BOOLEAN = "extra_boolean"
        private const val EXTRA_LONG = "extra_long"
        private const val EXTRA_FLOAT = "extra_float"
        private const val EXTRA_DOUBLE = "extra_double"
        private const val EXTRA_STRING = "extra_string"
        private const val EXTRA_BUNDLE = "extra_bundle"
        private const val EXTRA_CHAR_SEQUENCE = "extra_char_sequence"
        private const val EXTRA_PARCELABLEE = "extra_parcelablee"
        private const val EXTRA_INT_ARRAY = "extra_int_array"
        private const val EXTRA_SHORT_ARRAY = "extra_short_array"
        private const val EXTRA_BYTE_ARRAY = "extra_byte_array"
        private const val EXTRA_CHAR_ARRAY = "extra_char_array"
        private const val EXTRA_BOOLEAN_ARRAY = "extra_boolean_array"
        private const val EXTRA_LONG_ARRAY = "extra_long_array"
        private const val EXTRA_FLOAT_ARRAY = "extra_float_array"
        private const val EXTRA_DOUBLE_ARRAY = "extra_double_array"
        private const val EXTRA_STRING_ARRAY = "extra_string_array"
        private const val EXTRA_PARCELABLE_ARRAY = "extra_parcelable_array"
        private const val EXTRA_CHAR_SEQUENCE_ARRAY = "extra_char_sequence_array"
        private const val EXTRA_INT_LIST = "extra_int_list"
        private const val EXTRA_STRING_LIST = "extra_string_list"
        private const val EXTRA_CHAR_SEQUENCE_LIST = "extra_char_sequence_list"
        private const val EXTRA_PARCELABLE_LIST = "extra_parcelable_list"
        private const val EXTRA_SERIALIZABLE = "extra_serializable"

        fun start(context: Context){
            val intent = Intent(context, ExtrasToActivity::class.java)
            intent.putExtra(EXTRA_INT, 7)
            intent.putExtra(EXTRA_SHORT, Short.MIN_VALUE)
            intent.putExtra(EXTRA_BYTE, Byte.MIN_VALUE)
            intent.putExtra(EXTRA_CHAR, Char.MAX_VALUE)
            intent.putExtra(EXTRA_BOOLEAN, true)
            intent.putExtra(EXTRA_LONG, 1234567890L)
            intent.putExtra(EXTRA_FLOAT, 1.5f)
            intent.putExtra(EXTRA_DOUBLE, 5.155564)
            intent.putExtra(EXTRA_STRING, "测试")
            intent.putExtra(EXTRA_BUNDLE, bundleOf(EXTRA_INT to 99))
            intent.putExtra(EXTRA_CHAR_SEQUENCE, DEFAULT_CS)
            intent.putExtra(EXTRA_PARCELABLEE, ParcelableBean("pb1"))
            intent.putExtra(EXTRA_INT_ARRAY, intArrayOf(1, 3, 5))
            intent.putExtra(EXTRA_SHORT_ARRAY, shortArrayOf(-1, -3, -5))
            intent.putExtra(EXTRA_BYTE_ARRAY, byteArrayOf(Byte.MAX_VALUE, Byte.MIN_VALUE))
            intent.putExtra(EXTRA_CHAR_ARRAY, charArrayOf(Char.MAX_VALUE, Char.MIN_VALUE))
            intent.putExtra(EXTRA_BOOLEAN_ARRAY, booleanArrayOf(true, false, true))
            intent.putExtra(EXTRA_LONG_ARRAY, longArrayOf(996L, 997L))
            intent.putExtra(EXTRA_FLOAT_ARRAY, floatArrayOf(888.7f, 999.7f))
            intent.putExtra(EXTRA_DOUBLE_ARRAY, doubleArrayOf(45.454545, 69.696969))
            intent.putExtra(EXTRA_STRING_ARRAY, arrayOf("android10", "ios7"))
            intent.putExtra(EXTRA_PARCELABLE_ARRAY, arrayOf(ParcelableBean("F1"), ParcelableBean("F2")))
            val cs1: CharSequence = "CCCC123"
            val cs2: CharSequence = "DDDD567"
            intent.putExtra(EXTRA_CHAR_SEQUENCE_ARRAY, arrayOf(cs1, cs2))
            intent.putIntegerArrayListExtra(EXTRA_INT_LIST, arrayListOf(2, 4, 6))
            intent.putStringArrayListExtra(EXTRA_STRING_LIST, arrayListOf("up", "down"))
            val cs3: CharSequence = "home777"
            val cs4: CharSequence = "end666"
            intent.putCharSequenceArrayListExtra(EXTRA_CHAR_SEQUENCE_LIST, arrayListOf(cs3, cs4))
            intent.putParcelableArrayListExtra(EXTRA_PARCELABLE_LIST, arrayListOf(ParcelableBean("poo6"), ParcelableBean("pxx8")))
            intent.putExtra(EXTRA_SERIALIZABLE, SerializableBean("9888iiiqwe"))
            context.startActivity(intent)
        }
    }

    private val mIntValue by intentExtras<Int>(EXTRA_INT)
    private val mShortValue by intentExtras<Short>(EXTRA_SHORT)
    private val mByteValue by intentExtras<Byte>(EXTRA_BYTE)
    private val mCharValue by intentExtras<Char>(EXTRA_CHAR)
    private val mBooleanValue by intentExtras<Boolean>(EXTRA_BOOLEAN)
    private val mLongValue by intentExtras<Long>(EXTRA_LONG)
    private val mFloatValue by intentExtras<Float>(EXTRA_FLOAT)
    private val mDoubleValue by intentExtras<Double>(EXTRA_DOUBLE)
    private val mStringValue by intentExtras<String>(EXTRA_STRING)
    private val mBundleValue by intentExtras<Bundle>(EXTRA_BUNDLE)
    private val mCharSequenceValue by intentExtras<CharSequence>(EXTRA_CHAR_SEQUENCE)
    private val mParcelableeValue by intentParcelableExtras<ParcelableBean>(EXTRA_PARCELABLEE)
    private val mIntArrayValue by intentExtras<IntArray>(EXTRA_INT_ARRAY)
    private val mShortArrayValue by intentExtras<ShortArray>(EXTRA_SHORT_ARRAY)
    private val mByteArrayValue by intentExtras<ByteArray>(EXTRA_BYTE_ARRAY)
    private val mCharArrayValue by intentExtras<CharArray>(EXTRA_CHAR_ARRAY)
    private val mBooleanArrayValue by intentExtras<BooleanArray>(EXTRA_BOOLEAN_ARRAY)
    private val mLongArrayValue by intentExtras<LongArray>(EXTRA_LONG_ARRAY)
    private val mFloatArrayValue by intentExtras<FloatArray>(EXTRA_FLOAT_ARRAY)
    private val mDoubleArrayValue by intentExtras<DoubleArray>(EXTRA_DOUBLE_ARRAY)
    private val mStringArrayValue by intentExtras<Array<String>>(EXTRA_STRING_ARRAY)
    private val mParcelableArrayValue by intentParcelableArrayExtras(EXTRA_PARCELABLE_ARRAY)
    private val mCharSequenceArrayValue by intentExtras<Array<CharSequence>>(EXTRA_CHAR_SEQUENCE_ARRAY)
    private val mIntListValue by intentListExtras<Int>(EXTRA_INT_LIST)
    private val mStringListValue by intentListExtras<String>(EXTRA_STRING_LIST)
    private val mCharsequenceListValue by intentListExtras<CharSequence>(EXTRA_CHAR_SEQUENCE_LIST)
    private val mParcelableListValue by intentListExtras<ParcelableBean>(EXTRA_PARCELABLE_LIST)
    private val mSerializableValue by intentSerializableExtras<SerializableBean>(EXTRA_SERIALIZABLE)


    //    private val mIntValue by intentExtrasNoNull(EXTRA_INT, 455)
    //    private val mShortValue by intentExtrasNoNull(EXTRA_SHORT, Short.MAX_VALUE)
    //    private val mByteValue by intentExtrasNoNull(EXTRA_BYTE, Byte.MAX_VALUE)
    //    private val mCharValue by intentExtrasNoNull(EXTRA_CHAR, Char.MAX_VALUE)
    //    private val mBooleanValue by intentExtrasNoNull(EXTRA_BOOLEAN, false)
    //    private val mLongValue by intentExtrasNoNull(EXTRA_LONG, 9876543210L)
    //    private val mFloatValue by intentExtrasNoNull(EXTRA_FLOAT, 0.1f)
    //    private val mDoubleValue by intentExtrasNoNull(EXTRA_DOUBLE, 0.2)
    //    private val mStringValue by intentExtrasNoNull(EXTRA_STRING, "默认")
    //    private val mBundleValue by intentExtrasNoNull(EXTRA_BUNDLE, bundleOf(EXTRA_INT to 11))
    //    private val mCharSequenceValue by intentExtrasNoNull(EXTRA_CHAR_SEQUENCE, DEFAULT_CS)
    //    private val mParcelableeValue by intentParcelableExtrasNoNull(EXTRA_PARCELABLEE, ParcelableBean("def"))
    //    private val mIntArrayValue by intentExtrasNoNull(EXTRA_INT_ARRAY, intArrayOf(5, 3, 1))
    //    private val mShortArrayValue by intentExtrasNoNull(EXTRA_SHORT_ARRAY, shortArrayOf(-5, -3, -1))
    //    private val mByteArrayValue by intentExtrasNoNull(EXTRA_BYTE_ARRAY, byteArrayOf(Byte.MAX_VALUE, Byte.MAX_VALUE))
    //    private val mCharArrayValue by intentExtrasNoNull(EXTRA_CHAR_ARRAY, charArrayOf(Char.MAX_VALUE, Char.MAX_VALUE))
    //    private val mBooleanArrayValue by intentExtrasNoNull(EXTRA_BOOLEAN_ARRAY, booleanArrayOf(false, false, false))
    //    private val mLongArrayValue by intentExtrasNoNull(EXTRA_LONG_ARRAY, longArrayOf(1L, 2L))
    //    private val mFloatArrayValue by intentExtrasNoNull(EXTRA_FLOAT_ARRAY, floatArrayOf(1.1f, 1.2f))
    //    private val mDoubleArrayValue by intentExtrasNoNull(EXTRA_DOUBLE_ARRAY, doubleArrayOf(11.1111111, 22.2222222))
    //    private val mStringArrayValue by intentExtrasNoNull(EXTRA_STRING_ARRAY, arrayOf("******", "----"))
    //    private val mParcelableArrayValue by intentParcelableExtrasNoNull(EXTRA_PARCELABLE_ARRAY, arrayOf(ParcelableBean("*-"), ParcelableBean("-*")))
    //    private val mCharSequenceArrayValue by intentExtrasNoNull(EXTRA_CHAR_SEQUENCE_ARRAY, arrayOf(DEFAULT_CS, DEFAULT_CS))
    //    private val mIntListValue by intentExtrasNoNull(EXTRA_INT_LIST, arrayListOf(6, 4, 2))
    //    private val mStringListValue by intentExtrasNoNull(EXTRA_STRING_LIST, arrayListOf("def", "def"))
    //    private val mCharsequenceListValue by intentExtrasNoNull(EXTRA_CHAR_SEQUENCE_LIST, arrayListOf(DEFAULT_CS, DEFAULT_CS))
    //    private val mParcelableListValue by intentExtrasNoNull(EXTRA_PARCELABLE_LIST, arrayListOf(ParcelableBean("def"), ParcelableBean("def")))
    //    private val mSerializableValue by intentSerializableExtrasNoNull(EXTRA_SERIALIZABLE, SerializableBean("def"))

    private val mBinding: ActivityExtrasResultBinding by bindingLayout(ActivityExtrasResultBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews(savedInstanceState: Bundle?) {
        super.findViews(savedInstanceState)
        getTitleBarLayout().setTitleName(R.string.extras_to_boolean)
    }

    override fun onClickBackBtn() {
        super.onClickBackBtn()
        finish()
    }

    override fun initData() {
        super.initData()
        updateView()
        showStatusCompleted()
    }

    private fun updateView(){
        mBinding.intTv.text = getString(R.string.extras_to_int).append(mIntValue ?: "null")
        mBinding.shortTv.text = getString(R.string.extras_to_short).append(mShortValue ?: "null")
        mBinding.byteTv.text = getString(R.string.extras_to_byte).append(mByteValue?: "null")
        mBinding.charTv.text = getString(R.string.extras_to_char).append(mCharValue?.toInt()?.toString() ?: "null")
        mBinding.booleanTv.text = getString(R.string.extras_to_boolean).append(mBooleanValue?: "null")
        mBinding.longTv.text = getString(R.string.extras_to_long).append(mLongValue?: "null")
        mBinding.floatTv.text = getString(R.string.extras_to_float).append(mFloatValue?: "null")
        mBinding.doubleTv.text = getString(R.string.extras_to_double).append(mDoubleValue?: "null")
        mBinding.stringTv.text = getString(R.string.extras_to_string).append(mStringValue?: "null")
        mBinding.bundleTv.text = getString(R.string.extras_to_bundle).append(mBundleValue?.getInt(EXTRA_INT)?: "null")
        mBinding.charSequenceTv.text = getString(R.string.extras_to_charsequence).append(mCharSequenceValue ?: "null")
        mBinding.parcelableeTv.text = getString(R.string.extras_to_parcelablee).append(mParcelableeValue ?: "null")
        mBinding.intArrayTv.text = getString(R.string.extras_to_intarray).append(mIntArrayValue?.contentToString() ?: "null")
        mBinding.shortArrayTv.text = getString(R.string.extras_to_shortarray).append(mShortArrayValue?.contentToString() ?: "null")
        mBinding.byteArrayTv.text = getString(R.string.extras_to_bytearray).append(mByteArrayValue?.contentToString() ?: "null")
        val charArray = mCharArrayValue
        if (charArray != null){
            val charToIntArray = IntArray(charArray.size)
            charArray.forEachIndexed { index, c ->
                charToIntArray[index] = c.toInt()
            }
            mBinding.charArrayTv.text = getString(R.string.extras_to_chararray).append(charToIntArray.contentToString())
        } else {
            mBinding.charArrayTv.text = getString(R.string.extras_to_chararray).append("null")
        }
        mBinding.booleanArrayTv.text = getString(R.string.extras_to_booleanarray).append(mBooleanArrayValue?.contentToString() ?: "null")
        mBinding.longArrayTv.text = getString(R.string.extras_to_longarray).append(mLongArrayValue?.contentToString() ?: "null")
        mBinding.floatArrayTv.text = getString(R.string.extras_to_floatarray).append(mFloatArrayValue?.contentToString() ?: "null")
        mBinding.doubleArrayTv.text = getString(R.string.extras_to_doublearray).append(mDoubleArrayValue?.contentToString() ?: "null")
        mBinding.stringArrayTv.text = getString(R.string.extras_to_stringarray).append(mStringArrayValue?.contentToString() ?: "null")
        mBinding.parcelableArrayTv.text = getString(R.string.extras_to_parcelablearray).append(mParcelableArrayValue?.contentToString() ?: "null")
        mBinding.charSequenceArrayTv.text = getString(R.string.extras_to_charsequencearray).append(mCharSequenceArrayValue?.contentToString() ?: "null")
        mBinding.intListTv.text = getString(R.string.extras_to_integerarraylist).append(mIntListValue ?: "null")
        mBinding.stringListTv.text = getString(R.string.extras_to_stringarraylist).append(mStringListValue ?: "null")
        mBinding.charsequenceListTv.text = getString(R.string.extras_to_charsequencearraylist).append(mCharsequenceListValue ?: "null")
        mBinding.parcelableListTv.text = getString(R.string.extras_to_parcelablearraylist).append(mParcelableListValue ?: "null")
        mBinding.serializableTv.text = getString(R.string.extras_to_serializable).append(mSerializableValue ?: "null")
    }

}