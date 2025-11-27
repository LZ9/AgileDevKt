package com.lodz.android.pandora.widget.collect.dynitems

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.lodz.android.pandora.databinding.PandoraItemCltDynEditBinding
import com.lodz.android.pandora.widget.collect.CltEditView
import com.lodz.android.pandora.widget.rv.recycler.base.BaseVbRvAdapter
import com.lodz.android.pandora.widget.rv.recycler.vh.DataVBViewHolder

/**
 * 动态条目输入框列表适配器
 * @author zhouL
 * @date 2025/11/26
 */
class CltItemsEditAdapter(context: Context) : BaseVbRvAdapter<CltItemsEditBean>(context) {

    /** 编辑按钮点击监听器 */
    private var mPdrEditBtnClickListener: ((bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 删除按钮点击监听器 */
    private var mPdrDeleteBtnClickListener: ((bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 文字限制监听器 */
    private var mPdrLimitListener: ((s: CharSequence, max: Int, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    /** 文字变化监听器 */
    private var mPdrTextChangedListener: ((s: CharSequence, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit)? = null

    override fun getVbInflate(): (LayoutInflater, parent: ViewGroup, attachToRoot: Boolean) -> ViewBinding = PandoraItemCltDynEditBinding::inflate

    override fun onBind(holder: DataVBViewHolder, position: Int) {
        val bean = getItem(position) ?: return
        holder.itemView.tag = bean.id // 通过ID来控制数据变化回调
        holder.getVB<PandoraItemCltDynEditBinding>().let { vb->
            if (bean.itemHeight > 0) {
                setItemViewHeight(holder.itemView, bean.itemHeight)
            }
            showTitleView(bean, vb) // 标题栏
            showUnitView(bean, vb) // 单位文字
            showEditBtnView(bean, vb, holder, position) // 编辑按钮
            showDeleteBtnView(bean, vb, holder, position) // 删除按钮
            vb.pdrLimitTv.visibility = bean.itemEtLimitVisibility // 输入限制提醒
            showEditTextView(bean, vb, holder, position) // 输入框
        }
    }

    /** 显示标题部分界面 */
    private fun showTitleView(bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding) {
        vb.pdrTitleTv.let {
            it.visibility = if (bean.itemTitleText.isEmpty()) View.GONE else View.VISIBLE
            it.text = bean.itemTitleText
            if (bean.itemTitleTextColor != null) {
                it.setTextColor(bean.itemTitleTextColor)
            }
            if (bean.itemTitleTextSizeSp > 0) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.itemTitleTextSizeSp)
            }
            if (bean.itemTitleWidthPx > 0) {
                it.layoutParams.width = bean.itemTitleWidthPx
            }
            if (bean.itemTitleBackgroundDrawable != null) {
                it.background = bean.itemTitleBackgroundDrawable
            }
        }
    }

    /** 显示单位文字部分界面 */
    private fun showUnitView(bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding) {
        vb.pdrUnitTv.let {
            it.visibility = if (bean.needItemUnit) View.VISIBLE else View.GONE
            it.text = bean.itemUnitText
            if (bean.itemUnitTextColor != null){
                it.setTextColor(bean.itemUnitTextColor)
            }
            if (bean.itemUnitTextSizeSp > 0) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.itemUnitTextSizeSp)
            }
        }
    }

    /** 显示编辑按钮部分界面 */
    private fun showEditBtnView(bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) {
        vb.pdrEditBtn.let {
            it.isEnabled = !bean.readOnly
            if (bean.itemEditBtnDrawable != null){
                it.setImageDrawable(bean.itemEditBtnDrawable)
            }
            if (bean.itemEditBtnSidePx > 0) {
                it.layoutParams.width = bean.itemEditBtnSidePx
                it.layoutParams.height = bean.itemEditBtnSidePx
            }

            if (bean.itemEditBtnScaleType != null){
                it.scaleType = bean.itemEditBtnScaleType
            }
            it.visibility = bean.itemEditBtnVisibility
            it.setOnClickListener {
                val id = holder.itemView.tag?.toString() ?: ""
                if (id != bean.id){
                    return@setOnClickListener
                }
                mPdrEditBtnClickListener?.invoke(bean, vb, holder, position)
            }
        }
    }

    /** 显示编辑按钮部分界面 */
    private fun showDeleteBtnView(bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) {
        vb.pdrDeleteBtn.let {
            it.isEnabled = !bean.readOnly
            if (bean.itemDeleteBtnDrawable != null){
                it.setImageDrawable(bean.itemDeleteBtnDrawable)
            }
            if (bean.itemDeleteBtnSidePx > 0) {
                it.layoutParams.width = bean.itemDeleteBtnSidePx
                it.layoutParams.height = bean.itemDeleteBtnSidePx
            }

            if (bean.itemDeleteBtnScaleType != null){
                it.scaleType = bean.itemDeleteBtnScaleType
            }
            it.visibility = bean.itemDeleteBtnVisibility
            it.setOnClickListener {
                val id = holder.itemView.tag?.toString() ?: ""
                if (id != bean.id){
                    return@setOnClickListener
                }
                mPdrDeleteBtnClickListener?.invoke(bean, vb, holder, position)
            }
        }
    }

    /** 显示输入框部分界面 */
    private fun showEditTextView(bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) {
        vb.pdrContentEdit.let {
            it.isEnabled = !bean.readOnly
            it.setText(bean.itemEtText)

            if (bean.itemEtBackgroundDrawable != null) {
                vb.pdrContentLayout.background = bean.itemEtBackgroundDrawable
            }
            if (bean.itemEtTextColor != null) {
                it.setTextColor(bean.itemEtTextColor)
            }
            if (bean.itemEtTextSizeSp > 0) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, bean.itemEtTextSizeSp)
            }
            it.setHint(bean.itemEtHintText)
            if (bean.itemEtHintTextColor != null) {
                it.setHintTextColor(bean.itemEtHintTextColor)
            }
            it.gravity = bean.itemEtGravity
            if (bean.itemEtSingleLine){
                it.isSingleLine = bean.itemEtSingleLine
            }
            if (bean.itemEtMaxCount > 0){
                it.filters = arrayOf(InputFilter.LengthFilter(bean.itemEtMaxCount))
            }
            if (bean.itemEtMinLines > 0) {
                it.minLines = bean.itemEtMinLines
            }
            if (bean.itemEtMaxLines > 0) {
                it.maxLines = bean.itemEtMaxLines
            }

            when (bean.itemEtInputType) {
                CltEditView.TYPE_ID_CARD -> {
                    // 输入身份证号
                    it.inputType = InputType.TYPE_CLASS_NUMBER
                    it.keyListener = DigitsKeyListener.getInstance("1234567890xX")
                    it.transformationMethod = CltEditView.UpperCaseTransformation()
                }
                CltEditView.TYPE_PHONE -> it.inputType = InputType.TYPE_CLASS_PHONE// 输入手机号
                CltEditView.TYPE_NUMBER -> it.inputType = InputType.TYPE_CLASS_NUMBER// 输入数字
                CltEditView.TYPE_NUMBER_DECIMAL -> {
                    // 输入小数
                    it.inputType = InputType.TYPE_CLASS_NUMBER
                    it.keyListener = DigitsKeyListener.getInstance("1234567890.")
                }
                CltEditView.TYPE_FOREIGN_CERT -> {
                    // 输入国外证件号
                    it.inputType = InputType.TYPE_CLASS_NUMBER
                    it.keyListener = DigitsKeyListener.getInstance("1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
                    it.transformationMethod = CltEditView.UpperCaseTransformation()
                }
            }
            it.addTextChangedListener(DataTextWatcher(bean, vb, holder, position))
        }
    }

    /** 监听输入框文本变化 */
    private inner class DataTextWatcher(private val bean: CltItemsEditBean, private val vb: PandoraItemCltDynEditBinding, private val holder: DataVBViewHolder, private val position: Int) : TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val id = holder.itemView.tag?.toString() ?: ""
            if (id != bean.id){
                return
            }
            if (bean.itemEtInputType == CltEditView.TYPE_NUMBER_DECIMAL) {
                val index = vb.pdrContentEdit.text.toString().indexOf(".")
                val lastIndex = vb.pdrContentEdit.text.toString().lastIndexOf(".")
                if (index == 0) {
                    // 第一位输入点则默认头部加0
                    vb.pdrContentEdit.setText("0.")
                    vb.pdrContentEdit.setSelection(vb.pdrContentEdit.text.toString().length)
                } else if (index != lastIndex) {
                    // 输入了多个小数点只保留第一个
                    val str = vb.pdrContentEdit.text.toString().take(lastIndex)
                    vb.pdrContentEdit.setText(str)
                    vb.pdrContentEdit.setSelection(str.length)
                }
            }
            if (bean.itemEtMaxCount > 0) {
                // 限制输入字数
                val length = vb.pdrContentEdit.text.toString().length
                if (length == bean.itemEtMaxCount && start != 0) {
                    mPdrLimitListener?.invoke(s ?: "", bean.itemEtMaxCount, bean, vb, holder, position)
                }
                vb.pdrLimitTv.text = StringBuilder().append(length).append("/").append(bean.itemEtMaxCount)
            }
            mPdrTextChangedListener?.invoke(s ?: "", bean, vb, holder, position)
        }
    }

    /** 设置编辑按钮点击监听器[listener] */
    fun setOnItemEditBtnClickListener(listener: (bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrEditBtnClickListener = listener
    }

    /** 设置删除按钮点击监听器[listener] */
    fun setOnItemDeleteBtnClickListener(listener: (bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrDeleteBtnClickListener = listener
    }

    /** 设置文字限制监听器[listener] */
    fun setOnItemLimitListener(listener: (s: CharSequence, max: Int, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrLimitListener = listener
    }

    /** 设置文字变化监听器[listener] */
    fun setOnItemTextChangedListener(listener: (s: CharSequence, bean: CltItemsEditBean, vb: PandoraItemCltDynEditBinding, holder: DataVBViewHolder, position: Int) -> Unit) {
        mPdrTextChangedListener = listener
    }
}