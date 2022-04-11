package com.lodz.android.agiledevkt.modules.result

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.lodz.android.agiledevkt.R
import com.lodz.android.agiledevkt.databinding.DialogMimeTypeBinding
import com.lodz.android.corekt.anko.toArrays
import com.lodz.android.corekt.anko.toastShort
import com.lodz.android.pandora.utils.viewbinding.bindingLayout
import com.lodz.android.pandora.widget.dialog.BaseDialog

/**
 * 文件类型选择弹框
 * @author zhouL
 * @date 2022/4/11
 */
class MimeTypeDialog(context: Context, private val isSelected: Boolean) : BaseDialog(context, R.style.AppDialogTheme) {

    companion object {
        private const val MIME_AUDIO = "audio/*"
        private const val MIME_VIDEO = "video/*"
        private const val MIME_IMAGE = "image/*"
        private const val MIME_TEXT = "text/*"
        private const val MIME_APPLICATION = "application/*"
        private const val MIME_ALL = "*/*"
    }

    /** 点击 */
    private var mListener: ((dif: DialogInterface, mineTypes: Array<String>) -> Unit)? = null

    private val mBinding: DialogMimeTypeBinding by bindingLayout(DialogMimeTypeBinding::inflate)

    override fun getViewBindingLayout(): View = mBinding.root

    override fun findViews() {
        super.findViews()
        mBinding.submitBtn.visibility = if (isSelected) View.VISIBLE else View.GONE
    }

    override fun setListeners() {
        super.setListeners()

        mBinding.audioCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_AUDIO))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.videoCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_VIDEO))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.imageCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_IMAGE))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.textCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_TEXT))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.documentCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_APPLICATION))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.allCb.setOnCheckedChangeListener { compoundButton, b ->
            if (!isSelected){
                callback(arrayOf(MIME_ALL))
                return@setOnCheckedChangeListener
            }
        }

        mBinding.submitBtn.setOnClickListener {
            val mineTypes = arrayListOf<String>()
            if (mBinding.audioCb.isChecked){
                mineTypes.add(MIME_AUDIO)
            }
            if (mBinding.videoCb.isChecked){
                mineTypes.add(MIME_VIDEO)
            }
            if (mBinding.imageCb.isChecked){
                mineTypes.add(MIME_IMAGE)
            }
            if (mBinding.textCb.isChecked){
                mineTypes.add(MIME_TEXT)
            }
            if (mBinding.documentCb.isChecked){
                mineTypes.add(MIME_APPLICATION)
            }
            if (mBinding.allCb.isChecked){
                mineTypes.add(MIME_ALL)
            }
            if (mineTypes.isEmpty()){
                toastShort(R.string.arc_selected_hint)
                return@setOnClickListener
            }
            callback(mineTypes.toArrays())
        }
    }

    private fun callback(mineTypes: Array<String>){
        mListener?.invoke(getDialogInterface(), mineTypes)
    }

    fun setOnSelectedListener(listener: ((dif: DialogInterface, mineTypes: Array<String>) -> Unit)?) {
        mListener = listener
    }

}