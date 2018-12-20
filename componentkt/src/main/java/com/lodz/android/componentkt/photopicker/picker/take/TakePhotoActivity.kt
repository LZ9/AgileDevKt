package com.lodz.android.componentkt.photopicker.picker.take

import android.content.Context
import android.content.Intent
import com.lodz.android.componentkt.R
import com.lodz.android.componentkt.base.activity.AbsActivity
import com.lodz.android.componentkt.photopicker.picker.PickerBean

/**
 * 拍照页面
 * Created by zhouL on 2018/12/20.
 */
internal class TakePhotoActivity : AbsActivity() {

    companion object {
        internal fun start(context: Context, pickerBean: PickerBean) {
            val intent = Intent(context, TakePhotoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getAbsLayoutId(): Int = R.layout.componentkt_activity_take_photo
}