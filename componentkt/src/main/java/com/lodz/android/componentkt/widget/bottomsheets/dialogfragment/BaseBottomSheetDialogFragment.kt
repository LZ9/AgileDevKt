package com.lodz.android.componentkt.widget.bottomsheets.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * BottomSheetDialogFragment基类
 * Created by zhouL on 2018/12/11.
 */
abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    @LayoutRes
    abstract protected fun getLayoutId(): Int


}