package com.lodz.android.agiledevkt.modules.extras

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.bean.ParcelableBean
import com.lodz.android.agiledevkt.bean.SerializableBean
import com.lodz.android.agiledevkt.databinding.FragmentExtrasResultBinding
import com.lodz.android.corekt.anko.append
import com.lodz.android.corekt.anko.argumentsExtrasNoNull
import com.lodz.android.corekt.anko.argumentsParcelableExtrasNoNull
import com.lodz.android.corekt.anko.argumentsSerializableExtrasNoNull
import com.lodz.android.pandora.base.fragment.BaseFragment
import com.lodz.android.pandora.utils.viewbinding.bindingLayout

/**
 * 参数传递给Fragment
 * @author zhouL
 * @date 2021/11/1
 */
class ExtrasToFragment : BaseFragment() {

    companion object {
        fun newInstance(): ExtrasToFragment {
            val fragment = ExtrasToFragment()
            val bundle = Bundle()
            bundle.putInt(ExtrasToActivity.EXTRA_INT, 7)
            bundle.putShort(ExtrasToActivity.EXTRA_SHORT, Short.MIN_VALUE)
            bundle.putByte(ExtrasToActivity.EXTRA_BYTE, Byte.MIN_VALUE)
            bundle.putChar(ExtrasToActivity.EXTRA_CHAR, Char.MAX_VALUE)
            bundle.putBoolean(ExtrasToActivity.EXTRA_BOOLEAN, true)
            bundle.putLong(ExtrasToActivity.EXTRA_LONG, 1234567890L)
            bundle.putFloat(ExtrasToActivity.EXTRA_FLOAT, 1.5f)
            bundle.putDouble(ExtrasToActivity.EXTRA_DOUBLE, 5.155564)
            bundle.putString(ExtrasToActivity.EXTRA_STRING, "测试")
            bundle.putBundle(ExtrasToActivity.EXTRA_BUNDLE, bundleOf(ExtrasToActivity.EXTRA_INT to 99))
            bundle.putCharSequence(ExtrasToActivity.EXTRA_CHAR_SEQUENCE, ExtrasToActivity.DEFAULT_CS)
            bundle.putParcelable(ExtrasToActivity.EXTRA_PARCELABLEE, ParcelableBean("pb1"))
            bundle.putIntArray(ExtrasToActivity.EXTRA_INT_ARRAY, intArrayOf(1, 3, 5))
            bundle.putShortArray(ExtrasToActivity.EXTRA_SHORT_ARRAY, shortArrayOf(-1, -3, -5))
            bundle.putByteArray(ExtrasToActivity.EXTRA_BYTE_ARRAY, byteArrayOf(Byte.MAX_VALUE, Byte.MIN_VALUE))
            bundle.putCharArray(ExtrasToActivity.EXTRA_CHAR_ARRAY, charArrayOf(Char.MAX_VALUE, Char.MIN_VALUE))
            bundle.putBooleanArray(ExtrasToActivity.EXTRA_BOOLEAN_ARRAY, booleanArrayOf(true, false, true))
            bundle.putLongArray(ExtrasToActivity.EXTRA_LONG_ARRAY, longArrayOf(996L, 997L))
            bundle.putFloatArray(ExtrasToActivity.EXTRA_FLOAT_ARRAY, floatArrayOf(888.7f, 999.7f))
            bundle.putDoubleArray(ExtrasToActivity.EXTRA_DOUBLE_ARRAY, doubleArrayOf(45.454545, 69.696969))
            bundle.putStringArray(ExtrasToActivity.EXTRA_STRING_ARRAY, arrayOf("android10", "ios7"))
            bundle.putParcelableArray(ExtrasToActivity.EXTRA_PARCELABLE_ARRAY, arrayOf(ParcelableBean("F1"), ParcelableBean("F2")))
            val cs1: CharSequence = "CCCC123"
            val cs2: CharSequence = "DDDD567"
            bundle.putCharSequenceArray(ExtrasToActivity.EXTRA_CHAR_SEQUENCE_ARRAY, arrayOf(cs1, cs2))
            bundle.putIntegerArrayList(ExtrasToActivity.EXTRA_INT_LIST, arrayListOf(2, 4, 6))
            bundle.putStringArrayList(ExtrasToActivity.EXTRA_STRING_LIST, arrayListOf("up", "down"))
            val cs3: CharSequence = "home777"
            val cs4: CharSequence = "end666"
            bundle.putCharSequenceArrayList(ExtrasToActivity.EXTRA_CHAR_SEQUENCE_LIST, arrayListOf(cs3, cs4))
            bundle.putParcelableArrayList(ExtrasToActivity.EXTRA_PARCELABLE_LIST, arrayListOf(ParcelableBean("poo6"), ParcelableBean("pxx8")))
            bundle.putSerializable(ExtrasToActivity.EXTRA_SERIALIZABLE, SerializableBean("9888iiiqwe"))
            fragment.arguments = bundle
            return fragment
        }
    }

    private val mIntValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_INT, 455)
    private val mShortValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_SHORT, Short.MAX_VALUE)
    private val mByteValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_BYTE, Byte.MAX_VALUE)
    private val mCharValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_CHAR, Char.MAX_VALUE)
    private val mBooleanValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_BOOLEAN, false)
    private val mLongValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_LONG, 9876543210L)
    private val mFloatValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_FLOAT, 0.1f)
    private val mDoubleValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_DOUBLE, 0.2)
    private val mStringValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_STRING, "默认")
    private val mBundleValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_BUNDLE, bundleOf(ExtrasToActivity.EXTRA_INT to 11))
    private val mCharSequenceValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_CHAR_SEQUENCE, ExtrasToActivity.DEFAULT_CS)
    private val mParcelableeValue by argumentsParcelableExtrasNoNull(ExtrasToActivity.EXTRA_PARCELABLEE, ParcelableBean("def"))
    private val mIntArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_INT_ARRAY, intArrayOf(5, 3, 1))
    private val mShortArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_SHORT_ARRAY, shortArrayOf(-5, -3, -1))
    private val mByteArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_BYTE_ARRAY, byteArrayOf(Byte.MAX_VALUE, Byte.MAX_VALUE))
    private val mCharArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_CHAR_ARRAY, charArrayOf(Char.MAX_VALUE, Char.MAX_VALUE))
    private val mBooleanArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_BOOLEAN_ARRAY, booleanArrayOf(false, false, false))
    private val mLongArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_LONG_ARRAY, longArrayOf(1L, 2L))
    private val mFloatArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_FLOAT_ARRAY, floatArrayOf(1.1f, 1.2f))
    private val mDoubleArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_DOUBLE_ARRAY, doubleArrayOf(11.1111111, 22.2222222))
    private val mStringArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_STRING_ARRAY, arrayOf("******", "----"))
    private val mParcelableArrayValue by argumentsParcelableExtrasNoNull(ExtrasToActivity.EXTRA_PARCELABLE_ARRAY, arrayOf(ParcelableBean("*-"), ParcelableBean("-*")))
    private val mCharSequenceArrayValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_CHAR_SEQUENCE_ARRAY, arrayOf(ExtrasToActivity.DEFAULT_CS, ExtrasToActivity.DEFAULT_CS))
    private val mIntListValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_INT_LIST, arrayListOf(6, 4, 2))
    private val mStringListValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_STRING_LIST, arrayListOf("def", "def"))
    private val mCharsequenceListValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_CHAR_SEQUENCE_LIST, arrayListOf(ExtrasToActivity.DEFAULT_CS, ExtrasToActivity.DEFAULT_CS))
    private val mParcelableListValue by argumentsExtrasNoNull(ExtrasToActivity.EXTRA_PARCELABLE_LIST, arrayListOf(ParcelableBean("def"), ParcelableBean("def")))
    private val mSerializableValue by argumentsSerializableExtrasNoNull(ExtrasToActivity.EXTRA_SERIALIZABLE, SerializableBean("def"))

    private val mBinding: FragmentExtrasResultBinding by bindingLayout(FragmentExtrasResultBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun initData(view: View) {
        super.initData(view)
        updateView()
        showStatusCompleted()
    }

    private fun updateView(){
        mBinding.intTv.text = getString(R.string.extras_to_int).append(mIntValue)
        mBinding.shortTv.text = getString(R.string.extras_to_short).append(mShortValue)
        mBinding.byteTv.text = getString(R.string.extras_to_byte).append(mByteValue)
        mBinding.charTv.text = getString(R.string.extras_to_char).append(mCharValue.toInt().toString())
        mBinding.booleanTv.text = getString(R.string.extras_to_boolean).append(mBooleanValue)
        mBinding.longTv.text = getString(R.string.extras_to_long).append(mLongValue)
        mBinding.floatTv.text = getString(R.string.extras_to_float).append(mFloatValue)
        mBinding.doubleTv.text = getString(R.string.extras_to_double).append(mDoubleValue)
        mBinding.stringTv.text = getString(R.string.extras_to_string).append(mStringValue)
        mBinding.bundleTv.text = getString(R.string.extras_to_bundle).append(mBundleValue.getInt(ExtrasToActivity.EXTRA_INT))
        mBinding.charSequenceTv.text = getString(R.string.extras_to_charsequence).append(mCharSequenceValue )
        mBinding.parcelableeTv.text = getString(R.string.extras_to_parcelablee).append(mParcelableeValue )
        mBinding.intArrayTv.text = getString(R.string.extras_to_intarray).append(mIntArrayValue.contentToString() )
        mBinding.shortArrayTv.text = getString(R.string.extras_to_shortarray).append(mShortArrayValue.contentToString() )
        mBinding.byteArrayTv.text = getString(R.string.extras_to_bytearray).append(mByteArrayValue.contentToString() )
        val charArray = mCharArrayValue
        val charToIntArray = IntArray(charArray.size)
        charArray.forEachIndexed { index, c ->
            charToIntArray[index] = c.toInt()
        }
        mBinding.charArrayTv.text = getString(R.string.extras_to_chararray).append(charToIntArray.contentToString())
        mBinding.booleanArrayTv.text = getString(R.string.extras_to_booleanarray).append(mBooleanArrayValue.contentToString() )
        mBinding.longArrayTv.text = getString(R.string.extras_to_longarray).append(mLongArrayValue.contentToString() )
        mBinding.floatArrayTv.text = getString(R.string.extras_to_floatarray).append(mFloatArrayValue.contentToString() )
        mBinding.doubleArrayTv.text = getString(R.string.extras_to_doublearray).append(mDoubleArrayValue.contentToString() )
        mBinding.stringArrayTv.text = getString(R.string.extras_to_stringarray).append(mStringArrayValue.contentToString() )
        mBinding.parcelableArrayTv.text = getString(R.string.extras_to_parcelablearray).append(mParcelableArrayValue.contentToString() )
        mBinding.charSequenceArrayTv.text = getString(R.string.extras_to_charsequencearray).append(mCharSequenceArrayValue.contentToString() )
        mBinding.intListTv.text = getString(R.string.extras_to_integerarraylist).append(mIntListValue)
        mBinding.stringListTv.text = getString(R.string.extras_to_stringarraylist).append(mStringListValue)
        mBinding.charsequenceListTv.text = getString(R.string.extras_to_charsequencearraylist).append(mCharsequenceListValue)
        mBinding.parcelableListTv.text = getString(R.string.extras_to_parcelablearraylist).append(mParcelableListValue)
        mBinding.serializableTv.text = getString(R.string.extras_to_serializable).append(mSerializableValue)
    }


}